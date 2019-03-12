# Docker Swarm
Creación de un Swarm, con 3 nodos, 1 como manager y 2 como workers.
Inicializamos el Swarm, y con la flag `--advertise-addr` indicamos sobre que red queremos hacerlo, en caso de que tengamos más de 1.
``` sh
$ docker swarm init --advertise-addr 192.168.50.2
```

Para ver el token para añadir nuevos workers al Swarm, debemos ejecutar el siguiente comando (si queremos añadir managers, solo cambiamos `worker` por `manager`):
```sh
$ docker swarm join-token worker
```

Podemos visualizar los nodos que tiene el Swarm (sobre un nodo manager).
```sh
$ docker node ls
```

Creamos un nuevo servicio a partir de un archivo `YAML`, para ello, usamos el siguiente comando:
```sh
$ docker stack deploy --compose-file docker-compose.yml TEST
```

Contenido del fichero `docker-compose.yml`:
```
version: "3"
services:
  nginx:
    image: nginx
    ports:
      - 80:80
    deploy:
      mode: replicated
      replicas: 5
      restart_policy:
        condition: on-failure
        delay: 30s
        max_attempts: 3
```

Vemos información sobre el servicio `TEST`.
```sh
$ docker stack services TEST
```

Mostramos más información sobre el servicio `TEST`, como en que nodos esta funcionando, el numero de réplicas, estados ...
```sh
$ docker stack ps TEST 
```

En cualquier momento, podemos dejar el Swarm, y los servicios que corrian sobre estos, se reparten entre los nodos restantes.
```sh
$ docker swarm leave
```

## Resources
- http://www.littlebigextra.com/how-to-create-a-docker-swarm-and-deploy-stack-of-services/
- https://docs.docker.com/engine/swarm/
- https://hub.docker.com/_/nginx
- https://docs.docker.com/get-started/part4/