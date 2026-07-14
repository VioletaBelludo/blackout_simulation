package com.blackout_simulation.controller;

import com.blackout_simulation.model.demand.MinuteDemand;
import com.blackout_simulation.model.plant.*;
import com.blackout_simulation.model.simulation.BlackoutSimulation;
import com.blackout_simulation.model.simulation.SimulationResult;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

public class Controller {

    //List of power plants in the system.
    private List<PowerPlant> powerPlants;

    //List of hourly demands.
    private MinuteDemand[] minuteDemands;

    //List of simulation results.
    private List<SimulationResult> simulationResults;


    public Controller(String plansFile, String demandFile) {
        powerPlants = new LinkedList<>();
        minuteDemands = new MinuteDemand[1440];
        simulationResults = new LinkedList<>();

        // Load plants from file
        loadPlants(plansFile);
        // Load hourly demand
        loadMinuteDemand(demandFile);
    }

    /**
     * Load plants from file.
     * @param filename The name of the file to load the plants from.
     */
    private void loadPlants(String filename) {
        try (InputStream is = getClass().getResourceAsStream("/data/" + filename)) {
            assert is != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    // Skip comments or empty lines
                    if (line.startsWith("#") || line.trim().isEmpty()) {
                        continue;
                    }

                    // Split values
                    int columns = 6;
                    String[] parts = line.split(",", columns);
                    if (parts.length < columns) {
                        continue; // Skip malformed lines
                    }

                    String type = parts[0].trim();
                    String name = parts[1].trim();
                    double latitude = Double.parseDouble(parts[2].trim());
                    double longitude = Double.parseDouble(parts[3].trim());
                    String city = parts[4].trim();
                    double maxCapacityMW = Double.parseDouble(parts[5].trim());
                    double efficiency = 1.0;

                    addPlant(type, name, latitude, longitude, city, maxCapacityMW, efficiency);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Load minute demand from file.
     * @param filename The name of the file to load the minute demand from.
     */
    private void loadMinuteDemand(String filename) {
        try (InputStream is = getClass().getResourceAsStream("/data/" + filename)) {
            assert is != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("#") || line.trim().isEmpty()) continue;

                    String[] parts = line.split(",", 2);
                    if (parts.length != 2) continue;

                    LocalTime time = LocalTime.parse(parts[0].trim());
                    double demand = Double.parseDouble(parts[1].trim());

                    addMinuteDemand(time, demand);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading demand forecast file: " + e.getMessage());
        }
    }

    /**
     * Add new power plant to system.
     * @param type Type of plant (e.g., "NUCLEAR", "HYDRO", etc.)
     * @param name Name of plant
     * @param latitude Latitude of plant
     * @param longitude Longitude of plant
     * @param city City where plant is located
     * @param maxCapacityMW Max generation capacity of plant in MW
     * @param efficiency Efficiency of plant (0.0 to 1.0)
     */
    private void addPlant(String type, String name, double latitude, double longitude, String city, double maxCapacityMW, double efficiency) {
        try {
            PowerPlant plant = switch (type) {
                case "NUCLEAR" -> new NuclearPlant(name, latitude, longitude, city, maxCapacityMW);
                case "HYDRO" -> new HydroPlant(name, latitude, longitude, city, maxCapacityMW, efficiency);
                case "COMBINED_CYCLE" -> new CombinedCyclePlant(name, latitude, longitude, city, maxCapacityMW);
                case "COAL" -> new CoalPlant(name, latitude, longitude, city, maxCapacityMW);
                case "WIND" -> new WindPlant(name, latitude, longitude, city, maxCapacityMW, efficiency);
                case "SOLAR" -> new SolarPlant(name, latitude, longitude, city, maxCapacityMW, efficiency);
                case "BIOMASS" -> new BiomassPlant(name, latitude, longitude, city, maxCapacityMW);
                case "GEOTHERMAL" -> new GeothermalPlant(name, latitude, longitude, city, maxCapacityMW, efficiency);
                case "FUEL_GAS" -> new FuelGasPlant(name, latitude, longitude, city, maxCapacityMW);
                default -> throw new IllegalArgumentException("Unsupported plant type: " + type);
            };
            powerPlants.add(plant);
        } catch (Exception e) {
            System.err.println("Error creating plant: " + e.getMessage());
        }
    }

    /**
     * Add new minute demand to the system.
     * @param time Time of demand
     * @param demand Demand value
     */
    private void addMinuteDemand(LocalTime time, double demand) {
        try {
            int index = time.getHour() * 60 + time.getMinute();
            minuteDemands[index] = new MinuteDemand(time, demand);
        } catch (Exception e) {
            System.err.println("Error creating minute demand: " + e.getMessage());
        }
    }

    /**
     * Get power plants in the system.
     * @return Array of power plants
     */
    public Object[] getPowerPlants() {
        return powerPlants.toArray();
    }

    /**
     * Simulate blackout according to given start time.
     * @param blackoutStart Start time of blackout
     */
    public void runBlackoutSimulation(LocalDateTime blackoutStart) {
        try {
            if (powerPlants == null || minuteDemands == null || minuteDemands.length != 1440) {
                throw new IllegalStateException("Datos incompletos: plantas o demandas no cargadas correctamente.");
            }

            BlackoutSimulation simulation = new BlackoutSimulation(powerPlants, minuteDemands);
            simulationResults = simulation.runSimulation(blackoutStart);

        } catch (PowerPlantException | IllegalStateException e) {
            System.err.println("Error en simulación de blackout:");
            simulationResults = List.of();
        }
    }

    /**
     * Return simulation results in JSON format.
     */
    public JSONArray getSimulationResults() {
        JSONArray jsonArray = new JSONArray();

        for (SimulationResult result : simulationResults) {
            JSONObject json = new JSONObject();
            json.put("time", result.getTime().toString());
            json.put("generatedMW", result.getGeneratedMW());
            json.put("expectedDemandMW", result.getExpectedDemandMW());
            json.put("averageStability", result.getAverageStability());
            json.put("generatedByTypeMW", result.getGeneratedByTypeMW());

            jsonArray.put(json);
        }

        return jsonArray;
    }

}
