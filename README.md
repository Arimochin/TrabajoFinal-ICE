# Trabajo Final de la materia Introducción a la Computación Evolutiva
## Prerequisitos para la ejecución:
- Se utilizo el jdk Eclipse Temurin 22.0.2
- Se utilizo python 3.12.3 (si se quiere realizar la visualizacion de los resultados)

## Ejecución
### Por medio del ejecutable (encontrado en la seccion releases):

Idealmente se necesitaria tener en una misma carpeta el archivo .jar y los archivos .atsp. El comando a ejecutar sería: 
```
java -jar TrabajoFinal.jar <path_atsp>
```
El primer parametro es el archivo que contiene la dimension del TSP y la matriz de costos. Hay 2 parametros más, `nro de iteraciones` con valor por defecto 1000 y `nro de muestras por grupo` con valor por defecto de 25.

### Por medio del codigo fuente
Si utiliza la IDE de IntelliJ, dirigirse a la carpeta src/main > click derecho en Main.java > More Run/Debug > Modify Run Configuration..., en el campo que dice "Program arguments" colocar: `resources/<nombre_archivo_atsp>`

-----
Se generará un archivo "output.txt" con los resultados de cada ejecución. Se utiliza en la visualización.
## Visualización
Recomendamos crear una venv (ambiente) con el comando: 
```
python3 -m venv <nombre_cualquiera> 
```
### Activar ambiente:

#### Windows: 
```
<nombre_venv>/Scripts/activate
```
#### Linux: 
```
source <nombre_venv>/bin/activate
```
### Desactivar ambiente (ambos SO):
```
deactivate
``` 

### Instalar librerias
``` 
pip install -r requirements.txt
``` 
En Windows simplemente ejecutando deberia funcionar la visualización. Si se está en linux puede que muestre un error si no se tienen la libreria tkinter, se puede instalar con el siguiente comando: 
```
sudo apt-get update
sudo apt-get install python3-tk 
```

Importante: tener el archivo "output.txt" en la misma carpeta que los visualizadores. 