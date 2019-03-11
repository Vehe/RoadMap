# Docker Documentation

Redirecciona el puerto 6379 del contenedor, a un puerto aleatorio disponible en el host.

```sh
$ docker run -d --name redisDynamic -p 6379 redis:latest
```

Ver el puerto en el que se estableció el puerto del ejemplo anterior en el host.

```sh
$ docker port redisDynamic 6379
```

Copia el archivo `config.conf` del directorio actual al directorio `/config/` del contenedor `dataContainer`.

```sh
$ docker cp config.conf dataContainer:/config/ 
```

Exporta el contenedor en un fichero `.tar`.

```sh
$ docker export dataContainer > dataContainer.tar
```

Importa un contenedor desde un `.tar`.

```sh
$ docker import dataContainer.tar
```

Linkea el docker `redis-server` con el nuevo, y ejecuta `cat /etc/hosts`, en el que podríamos ver ese contenedor.

```sh
$ docker run --link redis-server:redis alpine cat /etc/hosts
```

Crea una nueva red, si no se establece nada, por defecto el driver es `bridge`.

```sh
$ docker network create backend-network
```

Crea una nueva red encriptada.

```sh
$ docker network create -d overlay --opt encrypted app1-network
```

Crea un nuevo docker, y le asigna la red `backend-network`.

```sh
$ docker run -d --name=redis --net=backend-network redis
```

Conecta un docker a la red `frontend-network` y le asigna un alias, por lo que todos en esta red, pueden identificarlo mediante este.

```sh
$ docker network connect --alias db frontend-network2 redis
```

Desconecta la red `frontend-network` del contenedor `redis`.

```sh
$ docker network disconnect frontend-network redis
```

Crea un nuevo docker basado en la imagen `ubuntu` y le asigna los mismos volúmenes que tiene `r1`.

```sh
$ docker run --volumes-from r1 -it ubuntu
```

Creamos un nuevo contenedor, y redirigimos los log a `syslog`.

```sh
$ docker run -d --name redis-syslog --log-driver=syslog redis
```

Creamos un nuevo contenedor, y deshabilitamos los logs.

```sh
$ docker run -d --name redis-syslog --log-driver=none redis
```

Inspeccionamos la configuración que tiene un docker en cuanto a sus logs.

```sh
$ docker inspect --format '{{ .HostConfig.LogConfig }}' redis-server
```

Creamos una docker, y le indicamos que en caso de crash, pruebe a reiniciarse 3 veces.

```sh
$ docker run -d --name restart-3 --restart=on-failure:3 scrapbook/docker-restart-example
```

Le indicamos al docker que en caso de crash, se reinicie siempre, hasta que manualmente le indicamos stop.

```sh
$ docker run -d --name restart-always --restart=always scrapbook/docker-restart-example
```

Creamos un docker asignandole el label `user`.

```sh
$ docker run -l user=12345 -d redis
```

Le indicamos al docker el fichero en el que se encuentran los label (1 por cada línea).

```sh
$ docker run --label-file=labels.txt -d redis
```

Establecemos varios label dentro del `Dockerfile`.

```sh
LABEL vendor=Katacoda \
	com.katacoda.version=0.0.5 \
    com.katacoda.build-date=2016-07-01T10:47:29Z \
    com.katacoda.course=Docker
```

Muestra los label que tiene un determinado docker.

```sh
$ docker inspect -f "{{json .Config.Labels }}" rd
```

Muestra los label que tiene una determinada imagen.

```sh
$ docker inspect -f "{{json .ContainerConfig.Labels }}" katacoda-label-example
```

Filtra los contenedores que tienen el label `user` con valor `scrapbook`.

```sh
$ docker ps --filter "label=user=scrapbook"
```