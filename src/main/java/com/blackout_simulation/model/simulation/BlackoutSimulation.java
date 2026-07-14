package com.blackout_simulation.model.simulation;

import com.blackout_simulation.model.plant.PlantState;
import com.blackout_simulation.model.plant.PowerPlant;
import com.blackout_simulation.model.plant.PowerPlantException;
import com.blackout_simulation.model.plant.RenewablePlant;
import com.blackout_simulation.model.demand.MinuteDemand;
import com.blackout_simulation.model.plant.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class BlackoutSimulation {

    private final List<PowerPlant> allPlants;
    private final MinuteDemand[] minuteDemands;
    private static final double MIN_REQUIRED_STABILITY = 0.7;

    public BlackoutSimulation(List<PowerPlant> plants, MinuteDemand[] minuteDemands) {
        this.allPlants = new ArrayList<>(plants);
        this.minuteDemands = minuteDemands;
    }

    public List<SimulationResult> runSimulation(LocalDateTime blackoutStart) throws PowerPlantException {
        List<SimulationResult> results = new ArrayList<>();
        initializeBlackout(blackoutStart);

        for (int minute = 0; minute < 36 * 60; minute++) {
            LocalDateTime currentTime = blackoutStart.plusMinutes(minute);
            LocalTime currentTimeOfDay = currentTime.toLocalTime();
            int index = (currentTimeOfDay.toSecondOfDay()) / 60 % 1440;
            double currentDemand = minuteDemands[index].getMegawatts();

            updatePlantStates(currentTime);
            List<PowerPlant> availablePlants = getAvailableOnlinePlants(currentTimeOfDay);

            // Prioritize renewables by efficiency (descending), maintaining original order if tied
            List<PowerPlant> sortedPlants = availablePlants.stream()
                    .sorted((a, b) -> {
                        boolean aRen = a instanceof RenewablePlant;
                        boolean bRen = b instanceof RenewablePlant;

                        if (aRen && bRen) {
                            double effA = ((RenewablePlant) a).getEfficiency();
                            double effB = ((RenewablePlant) b).getEfficiency();
                            return Double.compare(effB, effA); // Most efficient first
                        } else if (aRen) {
                            return -1;
                        } else if (bRen) {
                            return 1;
                        }
                        return 0;
                    }).toList();

            Map<PowerPlant, Double> generatedByPlant = new LinkedHashMap<>();
            double totalGenerated = 0;

            for (PowerPlant plant : sortedPlants) {
                if (totalGenerated >= currentDemand) break;

                double capacity = plant instanceof RenewablePlant
                        ? plant.getMaxCapacityMW() * ((RenewablePlant) plant).getEfficiency()
                        : plant.getMaxCapacityMW();

                double remaining = currentDemand - totalGenerated;
                double toUse = Math.min(remaining, capacity);
                generatedByPlant.put(plant, toUse);
                totalGenerated += toUse;
            }

            // Proportional adjustment if overproducing
            if (totalGenerated > currentDemand) {
                double scale = currentDemand / totalGenerated;
                Map<PowerPlant, Double> scaledMap = new LinkedHashMap<>();
                for (Map.Entry<PowerPlant, Double> entry : generatedByPlant.entrySet()) {
                    scaledMap.put(entry.getKey(), entry.getValue() * scale);
                }
                generatedByPlant.clear();
                generatedByPlant.putAll(scaledMap);
                totalGenerated = currentDemand;
            }

            // Calculate current stability
            double currentStability = calculateWeightedStabilityByPlant(generatedByPlant) / totalGenerated;

            // If minimum stability is not reached, adjust:
            if (currentStability < MIN_REQUIRED_STABILITY) {
                // 1. Remove renewables starting from the least stable
                List<Map.Entry<PowerPlant, Double>> renewablesSorted = generatedByPlant.entrySet().stream()
                        .filter(e -> e.getKey() instanceof RenewablePlant)
                        .sorted(Comparator.comparingDouble(e -> e.getKey().getStability()))
                        .toList();

                for (Map.Entry<PowerPlant, Double> entry : renewablesSorted) {
                    PowerPlant renewable = entry.getKey();
                    double removed = entry.getValue();
                    generatedByPlant.remove(renewable);
                    totalGenerated -= removed;
                    currentStability = calculateWeightedStabilityByPlant(generatedByPlant) / totalGenerated;
                    if (currentStability >= MIN_REQUIRED_STABILITY) break;
                }

                // 2. Add nuclear energy
                List<PowerPlant> nuclear = availablePlants.stream()
                        .filter(p -> p.getType().equalsIgnoreCase("Nuclear") && !generatedByPlant.containsKey(p))
                        .collect(Collectors.toList());
                totalGenerated = addGeneration(nuclear, generatedByPlant, currentDemand, false, true);

                // 3. If still insufficient, add thermal (ordered by stability)
                currentStability = calculateWeightedStabilityByPlant(generatedByPlant) / totalGenerated;
                if (currentStability < MIN_REQUIRED_STABILITY) {
                    List<PowerPlant> thermals = availablePlants.stream()
                            .filter(p -> !(p instanceof RenewablePlant))
                            .filter(p -> !p.getType().equalsIgnoreCase("Nuclear"))
                            .filter(p -> !generatedByPlant.containsKey(p))
                            .sorted(Comparator.comparingDouble(PowerPlant::getStability).reversed())
                            .collect(Collectors.toList());

                    totalGenerated = addGeneration(thermals, generatedByPlant, currentDemand, false, false);
                }
            }

            Map<String, Double> generatedByType = new HashMap<>();
            for (Map.Entry<PowerPlant, Double> entry : generatedByPlant.entrySet()) {
                generatedByType.merge(entry.getKey().getType(), entry.getValue(), Double::sum);
            }

            double averageStability = totalGenerated != 0.0 ? calculateWeightedStabilityByPlant(generatedByPlant) / totalGenerated : 0.0;

            results.add(new SimulationResult(currentTime, totalGenerated, currentDemand,
                    averageStability, generatedByType));
        }

        return results;
    }

    private void initializeBlackout(LocalDateTime blackoutStart) throws PowerPlantException {
        for (PowerPlant plant : allPlants) {
            plant.setState(PlantState.OFFLINE);
            plant.setRestartInitiationTime(blackoutStart);
        }
    }

    private void updatePlantStates(LocalDateTime currentTime) throws PowerPlantException {
        for (PowerPlant plant : allPlants) {
            if (plant.getState() == PlantState.OFFLINE &&
                    plant.getRestartInitiationTime().plus(plant.getRestartDuration()).isBefore(currentTime)) {
                plant.setState(PlantState.ONLINE);
            }
        }
    }

    private List<PowerPlant> getAvailableOnlinePlants(LocalTime time) {
        return allPlants.stream()
                .filter(p -> p.getState() == PlantState.ONLINE)
                .filter(p -> !time.isBefore(p.getAvailableFromTime()) && !time.isAfter(p.getAvailableToTime()))
                .collect(Collectors.toList());
    }

    private double addGeneration(List<PowerPlant> plants, Map<PowerPlant, Double> map, double demand,
                                 boolean onlyRenewables, boolean onlyNuclear) {
        double total = map.values().stream().mapToDouble(Double::doubleValue).sum();
        for (PowerPlant plant : plants) {
            if (map.containsKey(plant)) continue;

            boolean isRenewable = plant instanceof RenewablePlant;
            if ((onlyRenewables && !isRenewable)
                    || (onlyNuclear && !plant.getType().equalsIgnoreCase("Nuclear"))
                    || (!onlyRenewables && !onlyNuclear && (isRenewable || plant.getType().equalsIgnoreCase("Nuclear")))) {
                continue;
            }

            double capacity = isRenewable
                    ? plant.getMaxCapacityMW() * ((RenewablePlant) plant).getEfficiency()
                    : plant.getMaxCapacityMW();
            double remaining = demand - total;
            if (remaining <= 0) break;

            double toUse = Math.min(remaining, capacity);
            map.put(plant, toUse);
            total += toUse;
        }
        return total;
    }

    private double calculateWeightedStabilityByPlant(Map<PowerPlant, Double> map) {
        return map.entrySet().stream()
                .mapToDouble(e -> e.getKey().getStability() * e.getValue())
                .sum();
    }
}
