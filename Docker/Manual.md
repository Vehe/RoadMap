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

Ejecuta un comando sobre un contenedor que se encuentra funcionando.

```sh
$ docker exec nginx cat /etc/nginx/conf.d/default.conf
```

Muestra las estadísticas de un contenedor en concreto. (CPU, RAM, ...)

```sh
$ docker stats nginx
```

Varias maneras de ver estadísticas de varios contenedores al mismo tiempo.

```sh
$ docker ps -q | xargs docker stats
$ docker stats `docker ps -q`
```

Muestra información de un contenedor de manera un poco más estética.

```sh
$ docker ps --format '{{.Names}} container is using {{.Image}} image'
```

Muestra información de un contenedor en formato de tabla.

```sh
$ docker ps --format 'table {{.Names}}\t{{.Image}}'
```

Ver más información mediante el inspect.

```sh
$ docker inspect --format '{{ .Id }} - {{ .Name }} - {{ .NetworkSettings.IPAddress }}' 5017
```

Ver más información mediante el inspect.

```sh
$ docker inspect --format '{{ .Id }} - {{ .Name }} - {{ .NetworkSettings.IPAddress }}' 5017
```

Iniciamos un Swarm.

```sh
$ docker swarm init
```

Nos unimos a un Swarm.

```sh
$ docker swarm join 172.17.0.27:2377 --token $token
```

Muestra los nodos que conforman el Swarm (necesitamos ser Manager).

```sh
$ docker node ls
```

Creamos una red con el driver `overlay`, la cual nos permite comunicar todos los nodos del Swarm.

```sh
$ docker network create -d overlay skynet
```

Crea un servicio http con 2 replicas, en la red `skynet`, estas replicas se reparten en el número de nodos del swarm, bindea el puerto 80 del guest al exterior.

```sh
$ docker service create --name http --network skynet --replicas 2 -p 80:80 katacoda/docker-http-server
```

Muesta los servicios que tenemos.

```sh
$ docker service ls
```

Comprobamos el estado de un servicio.

```sh
$ docker service ps http
```

Vemos información sobre el servicio.

```sh
$ docker service inspect --pretty http
```

Vemos las tareas que esta realizando cada nodo.

```sh
$ docker node ps self
```

Escala el número de replicas del servicio http, puede ser mayor o menor que la actual.

```sh
$ docker service scale http=5
```

Creamos una red en modo `swarm-scoped`, de tal manera que solo los contenedores que estén lanzados como servicio, pueden unirse a esta.

```sh
$ dodocker network create --attachable -d overlay eg1
```

Podemos ver la IP virtual asignada a ese contenedor.

```sh
$ docker service inspect http --format="{{.Endpoint.VirtualIPs}}"
```

Añade una nueva variable de entorno, a todos los contenedores que conforman el servicio.

```sh
$ docker service update --env-add KEY=VALUE http
```

Asigna la CPU y la Memoria que puede consumir el servicio.

```sh
$ docker service update --limit-cpu 2 --limit-memory 512mb http
```

Otra forma de crear más réplicas de un servicio.

```sh
$ docker service update --replicas=6 http
```

Ejecuta un update, pero con una limitación de 1 en 1 y con un delay entre ellas de 10s.

```sh
$ docker service update --update-delay=10s --update-parallelism=1 --image katacoda/docker-http-server:v3 http
```