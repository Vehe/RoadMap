# MongoDB y Java con Docker-compose
## Dockerfile
Establecemos en el Dockerfile, las bases para el contenedor.
```sh
FROM ubuntu:xenial
RUN apt-get update -y && apt-get install -y \
        default-jre \
        default-jdk
COPY . /app
ENV CLASSPATH .:/app/mongo-java-driver-3.9.1.jar
WORKDIR /app
RUN javac People.java && javac ReadPeople.java
```
## docker-compose.yml
Archivo de configuración del docker compose.
```
version: '3'
services:
    write_app:
        build: .
        command: java People
        depends_on:
            - db
        ports:
            - "80:9000"
    read_app:
        build: .
        command: java ReadPeople
        depends_on:
            - db
        ports:
            - "81:9001"
    db:
        image: mongo
        volumes:
            - "/opt/mongodb:/data/db"
```
Creamos 3 servicios:
- `write_app` el cual contiene la aplicación `ReadPeople.java`, la cual abre mediante shockets el puerto `9001`, este esta bindeado en el puerto `81` de host, al acceder a este, se mostraran todos los usuarios de la base de datos.

- `read_app` el cual contiene la aplicación `People.java`, la cual abre mediante shockets el puerto `9000`, este esta bindeado en el puerto `80` de host, al acceder a él, nos preguntara por datos para crear un nuevo user en la BBDD.

- `db` es la base de datos creada a partir de la imagen oficial de `mongo`, contiene toda la base de datos, los servicios anteriores, necesitan que este se encienda primero para poder funcionar, por ello, tienen el apartado `depends_on`.

## Puesta en funcionamiento
En nuesto directorio de trabajo, debemos tener lo siguiente:
- Dockerfile
- docker-compose.yml
- People.java
- ReadPeople.java
- mongo-java-driver-3.9.1.jar

Ejecutamos el comando:
```sh
$ docker-compose up
```

Si realizamos alguna modificación y queremos construir y lanzar otra vez, podemos usar:
```sh
$ docker-compose up --build
```

Si todo ha ido bien, podremos ver los mensajes de todos los servicios funcionando correctamente.
Por lo que ahora podemos conectarnos al servicio `write_app` para crear un nuevo usuario, mediante el comando:
```sh
$ nc localhost 80
```

Y para ver el contenido de la BBDD, nos conectamos al servicio `read_app`, mediante el comando:
```sh
$ nc localhost 81
```

## Resources
- https://stackoverflow.com/questions/6876266/java-net-connectexception-connection-refused
- http://nereida.deioc.ull.es/~cleon/doctorado/doc06/doc06/html/node9.html
- https://docs.docker.com/compose/networking/
- https://docs.docker.com/compose/compose-file/