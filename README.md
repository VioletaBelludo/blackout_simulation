# Simulador de recuperación de red eléctrica 

![Java](https://img.shields.io/badge/Java-17/21%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-4796FC?style=for-the-badge&logo=java&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![MVC](https://img.shields.io/badge/MVC-brightgreen?style=for-the-badge)
![JUnit](https://img.shields.io/badge/JUnit-blue?style=for-the-badge)
![JSON](https://img.shields.io/badge/JSON-orange?style=for-the-badge)


## Descripción del proyecto

Aplicación de simulación interactiva diseñada para modelar la recuperación de un sistema de red eléctrica tras un cero eléctrico o *blackout*. 

La aplicación carga los datos de múltiples plantas generadoras distribuidas geográficamente en la península ibérica y, a través de una interfaz gráfica intuitiva, permite al usuario visualizar las estaciones de generación, configurar la fecha y hora de un apagón y observar cómo la red recupera su estabilidad de forma paulatina a lo largo de las siguientes 36 horas.


## Características principales

* **Visualización geográfica:** Mapeo de todas las plantas de energía sobre el territorio a través de una interfaz gráfica dinámica.
* **Motor de simulación avanzado:** Calcula el balance entre la generación de electricidad y la demanda prevista cada minuto, aplicando tiempos de reinicio realistas y cuotas de estabilidad térmica y renovable.
* **Manejo de datos flexibles:** Carga automática del estado de las plantas y previsiones de demanda desde archivos de texto y estructuración de resultados de la simulación en formato estándar **JSON**.
* **Arquitectura Escalable:** Implementación estricta del patrón de diseño **Modelo-Vista-Controlador (MVC)**, garantizando un bajo acoplamiento entre la lógica de negocio, la gestión de datos y la interfaz de usuario.


## Tecnologías utilizadas

* **Lenguaje:** Java (JDK >= 21)
* **Interfaz gráfica:** JavaFX 
* **Construcción y dependencias:** Gradle
* **Pruebas unitarias:** JUnit 
* **Formato de exportación de datos:** JSON (`org.json`)

---


## Reglas del motor de simulación

Al desencadenar una simulación de cero eléctrico, el sistema aplica las siguientes reglas algorítmicas de balance energético minuto a minuto:

1. **Prioridad renovable:** Se prioriza la generación de energía renovable, ordenada descendentemente según la **estabilidad** que aportan a la red.
2. **Cobertura nuclear:** Si la demanda supera la capacidad renovable disponible en ese momento (considerando tiempos de reinicio), se acude a las centrales nucleares.
3. **Respaldo térmico:** Si todavía existe déficit frente a la demanda, se arranca la generación mediante plantas térmicas (ciclo combinado, carbón, gasolina, biomasa), ordenadas por su nivel de estabilidad.
4. **Control de estabilidad estricto (Restricción del 70%):** La red exige un servicio estable (sin sobrecargas ni caídas). Si tras el reparto, la estabilidad media ponderada cae por debajo de **0.7 (70%)**, el sistema desconecta paulatinamente las fuentes renovables menos estables hasta alcanzar este umbral de seguridad, compensando el déficit nuevamente con energía nuclear y térmica.
5. **Capping de demanda:** En ningún escenario la electricidad generada superará la demanda prevista para un minuto determinado. En situaciones críticas, habrá zonas de la red que quedarán desabastecidas temporalmente hasta que las plantas con arranques lentos completen su tiempo de reinicio.


## Especificaciones de las plantas generadoras

El comportamiento de la red depende del tipo de planta, su disponibilidad, tiempo de reinicio en frío y estabilidad aportada:

| Tipo de Planta | Disponibilidad | Tiempo de Reinicio | Estabilidad (0.0 - 1.0) | Renovable |
| :--- | :--- | :--- | :--- | :---: |
| **Biomasa** | 00:00 - 23:59 | 3 h | 0.5 | ❎ |
| **Carbón** | 00:00 - 23:59 | 8 h | 0.9 | ❎ |
| **Ciclo combinado** | 00:00 - 23:59 | 2 horas | 0.7 | ❎ |
| **Gasolina** | 00:00 - 23:59 | 4 h | 0.6 | ❎ |
| **Geotérmica** | 00:00 - 23:59 | 1 h | 0.7 | ✅ |
| **Hidroeléctrica** | 00:00 - 23:59 | 3 min | 0.8 | ✅ |
| **Nuclear** | 00:00 - 23:59 | 1 día (24h) | 1.0 | ❎ |
| **Solar** | 07:00 - 18:59 | 6 min | 0.1 | ✅ |
| **Eólica** | 00:00 - 23:59 | 6 min | 0.2 | ✅ |

*Todas las plantas renovables cuentan con un parámetro dinámico de eficiencia (por defecto 100%).*

---
