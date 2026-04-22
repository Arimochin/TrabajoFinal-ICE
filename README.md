# Trabajo Final de la materia Introducción a la Computación Evolutiva
## Prerequisitos para la ejecución:
- Se utilizo el jdk Eclipse Temurin 22.0.2
- Se utilizo python 3.12.3 (si se quiere realizar la visualizacion de los resultados.

## Ejecución
En la clase Main en la linea 22 se puede colocar el path a un archivo de formato .atsp


## Visualización
Recomendamos crear una venv con el comando: 
```
python3 -m venv <nombre_cualquiera> 
```
Para activarla:

Windows: 
```
<nombre_venv>/Scripts/activate
```
Linux: 
```
source <nombre_venv>/bin/activate
```
Para desactivar en ambos SO:
```
deactivate
``` 

Luego instalar matplotlib y pandas: 
```
pip install matplotlib
``` 
```
pip install pandas
```
Si se quiere hacer uso, dejamos un requirements.txt
``` 
pip install -r requirements.txt
``` 
En Windows simplemente ejecutando deberia funcionar la visualizacion. Si se está en linux puede que muestre un error si no se tienen la libreria tkinter, se puede instalar con el siguiente comando: 
```
sudo apt-get update
sudo apt-get install python3-tk 
```
