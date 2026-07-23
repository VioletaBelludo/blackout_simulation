# ⚡ Simulador de Recuperación de Red Eléctrica (Grid Recovery Simulator)

![Java](https://img.shields.io/badge/Java-17/21%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-4796FC?style=for-the-badge&logo=java&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![MVC](https://img.shields.io/badge/MVC-brightgreen?style=for-the-badge)
![MVC](https://img.shields.io/badge/JUnit-blue?style=for-the-badge)
![MVC](https://img.shields.io/badge/CSS-pink?style=for-the-badge)
![MVC](https://img.shields.io/badge/JSON-orange?style=for-the-badge)


## 📖 Descripción del Proyecto

Este proyecto es una aplicación de simulación interactiva diseñada para modelar la recuperación de todo un sistema de red eléctrica tras un apagón masivo (conocido como *cero eléctrico* o *blackout*). 

A través de una interfaz gráfica intuitiva, la aplicación carga datos de múltiples plantas generadoras distribuidas geográficamente (enfocado en la península ibérica), permitiendo a los usuarios visualizar las estaciones de generación, configurar la fecha y hora de un apagón y observar cómo la red recupera su estabilidad de forma paulatina a lo largo de las siguientes 36 horas.

## ✨ Características Principales

* **Visualización Geográfica:** Mapeo de todas las plantas de energía sobre el territorio a través de una interfaz gráfica dinámica.
* **Motor de Simulación Avanzado:** Calcula minuto a minuto el balance entre la generación de electricidad y la demanda prevista, aplicando tiempos de reinicio realistas y cuotas de estabilidad térmica y renovable.
* **Manejo de Datos Flexibles:** Carga automática del estado de las plantas y previsiones de demanda desde archivos de texto y estructuración de resultados de la simulación en formato estándar **JSON**.
* **Arquitectura Escalable:** Implementación estricta del patrón de diseño **Modelo-Vista-Controlador (MVC)**, garantizando un bajo acoplamiento entre la lógica de negocio, la gestión de datos y la interfaz de usuario.

---

## 🏗 Arquitectura del Sistema (Patrón MVC)

El proyecto está diseñado bajo el patrón **Modelo-Vista-Controlador**, asegurando un código limpio, modularizado y mantenible:

1. **Modelo (`model`):** Gestiona el estado de la aplicación. Almacena las instancias de las diferentes plantas (Biomasa, Eólica, Nuclear, etc.), maneja las previsiones de demanda energética por minuto e indexa los resultados históricos de las simulaciones. Controla validaciones físicas, como asegurar que las coordenadas (latitud/longitud) estén en un rango válido.
2. **Controlador (`controller`):** Actúa como el motor lógico y el intermediario. Carga los datos iniciales, ejecuta el algoritmo de simulación del apagón y toma decisiones en tiempo real sobre qué fuentes de energía arrancar priorizando la estabilidad y el tipo de energía.
3. **Vista (`view`):** Compuesta por interfaces FXML diseñadas de forma interactiva. Se encarga de capturar la entrada del usuario (ej. fecha y hora de la simulación) y renderizar dinámicamente los gráficos y mapas de resultados.

---

## ⚙️ Reglas del Motor de Simulación

Al desencadenar una simulación de *Cero Eléctrico* (Blackout), el sistema aplica las siguientes reglas algorítmicas de balance energético minuto a minuto:

1. **Prioridad Renovable:** Se prioriza la generación de energía renovable, ordenada descendentemente según la **estabilidad** que aportan a la red.
2. **Cobertura Nuclear:** Si la demanda supera la capacidad renovable disponible en ese momento (considerando tiempos de reinicio), se acude a las centrales nucleares.
3. **Respaldo Térmico:** Si todavía existe déficit frente a la demanda, se arranca la generación mediante plantas térmicas (ciclo combinado, carbón, gasolina, biomasa), ordenadas por su nivel de estabilidad.
4. **Control de Estabilidad Estricto (Restricción del 70%):** La red exige un servicio estable (sin sobrecargas ni caídas). Si tras el reparto, la estabilidad media ponderada cae por debajo de **0.7 (70%)**, el sistema desconecta paulatinamente las fuentes renovables menos estables hasta alcanzar este umbral de seguridad, compensando el déficit nuevamente con energía nuclear y térmica.
5. **Capping de Demanda:** En ningún escenario la electricidad generada superará la demanda prevista para un minuto determinado. En situaciones críticas, habrá zonas de la red que quedarán desabastecidas temporalmente hasta que las plantas con arranques lentos completen su tiempo de reinicio.

---

## 🏭 Especificaciones de las Plantas Generadoras

El comportamiento de la red depende del tipo de planta, su disponibilidad, tiempo de reinicio en frío y estabilidad aportada:

| Tipo de Planta | Disponibilidad | Tiempo de Reinicio | Estabilidad (0.0 - 1.0) | Renovable |
| :--- | :--- | :--- | :--- | :---: |
| **Biomasa** | 00:00 - 23:59 | 3 horas | 0.5 | ❌ |
| **Carbón** | 00:00 - 23:59 | 8 horas | 0.9 | ❌ |
| **Ciclo Combinado** | 00:00 - 23:59 | 2 horas | 0.7 | ❌ |
| **Gasolina** | 00:00 - 23:59 | 4 horas | 0.6 | ❌ |
| **Geotérmica** | 00:00 - 23:59 | 1 hora | 0.7 | ✅ |
| **Hidroeléctrica** | 00:00 - 23:59 | 3 minutos | 0.8 | ✅ |
| **Nuclear** | 00:00 - 23:59 | 1 día (24h) | 1.0 | ❌ |
| **Solar** | 07:00 - 18:59 | 6 minutos | 0.1 | ✅ |
| **Eólica** | 00:00 - 23:59 | 6 minutos | 0.2 | ✅ |

*Nota: Todas las plantas renovables cuentan con un parámetro dinámico de eficiencia (por defecto 100%).*

---

## 🛠 Tecnologías Utilizadas

* **Lenguaje:** Java (JDK >= 21)
* **Interfaz Gráfica:** JavaFX (con archivos `.fxml` integrables con Scene Builder)
* **Construcción y Dependencias:** Gradle
* **Pruebas Unitarias:** JUnit (Testing avanzado funcional y básico)
* **Formato de Exportación de Datos:** JSON (mediante la librería `org.json`)

---

## 🚀 Compilación y Ejecución

El proyecto está preparado para construirse utilizando **Gradle**. Asegúrese de tener el JDK 21+ instalado en su entorno.

### 1. Ejecutar la aplicación en entorno de desarrollo
Puede levantar la aplicación ejecutando la clase principal o utilizando las tareas de Gradle.
```bash
./gradlew run
```
*(Nota: Si observa la advertencia `Unsupported JavaFX configuration: classes were loaded from 'unnamed module...'`, puede ignorarla de forma segura).*

### 2. Pruebas Unitarias (Testing)
Para comprobar la integridad del modelo y de los algoritmos de balance:
```bash
# Ejecutar todas las pruebas
./gradlew testAll

# Pruebas de funcionalidades básicas
./gradlew testBasic

# Pruebas de simulación avanzada
./gradlew testAdvanced
```

### 3. Empaquetado y Distribución

#### Generar un Fat JAR
Para crear un archivo ejecutable que contenga todas las dependencias necesarias:
1. Asegúrese de que la tarea `jar` está descomentada en `build.gradle`.
2. Ejecute:
   ```bash
   ./gradlew jar
   ```
3. El archivo resultante estará en `build/libs/`. Podrá ejecutarse mediante:
   ```bash
   java -jar app-1.0-SNAPSHOT.jar
   ```

#### Construcción de imagen JRE personalizada (jlink)
Si se desea distribuir la aplicación sin necesidad de pre-instalar un JRE en el sistema destino, el proyecto está modularizado (`module-info.java`) y soporta `jlink`:
1. Comente la tarea `jar` y descomente la tarea `jlink` en `build.gradle`.
2. Ejecute `./gradlew jlink`.
3. El paquete autocontenido se generará en el directorio `build/image`.
