# Java y MongoDB en Docker Swarm
Creamos la imagen que vamos a usar a través del fichero `Dockerfile`.
```sh
$ docker build -t javaymongo .
```

El contenido del `Dockerfile` es el siguiente:
```
FROM ubuntu:xenial
RUN apt-get update -y && apt-get install -y \
        default-jre \
        default-jdk
COPY . /app
ENV CLASSPATH .:/app/mongo-java-driver-3.9.1.jar
WORKDIR /app
RUN javac People.java
```

Creamos un repositorio en Docker Hub (https://hub.docker.com/), cambiamos el tag a la imagen y la subimos:
```sh
$ docker tag 940d9d2fd15a v3he/javaymongo:latest
$ docker push v3he/javaymongo:latest
```

Desde uno de los nodos que vamos a usar, creamos el Swarm, indicando la red por la que va a funcionar:
```sh
$ docker swarm init --advertise-addr 192.168.50.2
```

Creamos un nuevo servicio mediante el archivo `docker-compose.yml`, y usaremos la imagen del repositorio que acabamos de crear:
```sh
$ docker stack deploy --compose-file docker-compose.yml javaYmongo
```

Contenido del fichero `docker-compose.yml`:
```
version: '3'
services:
    app:
        image: v3he/javaymongo
        command: java People
        depends_on:
            - db
        ports:
            - "80:9000"
        deploy:
            mode: replicated
            replicas: 5
            restart_policy:
                condition: on-failure
                delay: 30s
                max_attempts: 3
    db:
        image: mongo
        deploy:
            restart_policy:
                condition: on-failure
                delay: 30s
                max_attempts: 3
```

Y si todo ha ido correctamente, podremos ver a través de la aplicación que corre en el puerto 80, que cada vez, la peticion la gestiona un docker distinto.