# MongoDB y Java dentro de Docker
## Network
Antes de comenzar, creamos una red en la que estarán amos docker.
```sh
$ docker network create app
```
## MongoDB
Usamos la imágen oficial de MongoDB, creamos un nuevo docker 
```sh
$ docker run -it -d -v /opt/mongodb:/data/db --net app mongo
```
Para que la base de datos no se borre, y dure en caso de borrar la imagen, o reiniciarla, lo que vamos a hacer es crear un volumen, el cual es la carpeta `/opt/mongodb` en el host, y la carpeta `/data/db` en el guest, este es el directorio por defecto en el que mongodb guarda los datos, por lo que no hace falta más configuración.
Al ejecutar el comando, ya tendremos un docker de mongodb a la escucha por el puerto por defecto `27017`, el cual almacenará los datos en la carpeta `/opt/mongodb` del host.

## Java
Sobre el directorio en el que se encuentra el fichero `Dockerfile`, ejecutamos el comando:
```sh
$ docker build -t roadmapdocker:latest .
```
Creará una imágen llamada `roadmapdocker`, con la cual crearemos un docker con el siguiente comando:
```sh
$ docker run -it --net app roadmapdocker:latest
```
Añadimos este docker mediante `--net [nombre red]` a la red que creamos anteriormente, y en la que se encuentra el docker con la BBDD.
La aplicación java se ha compilado en el momento de la creación del docker, y se nos muestra con el prompt para ingresar un nuevo usuario nada más iniciar.