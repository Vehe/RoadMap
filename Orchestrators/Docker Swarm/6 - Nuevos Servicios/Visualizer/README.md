# Visualizer
Para comenzar a utilizar Visualizer, necesitamos tener funcionando Docker Swarm, y para probar el funcionamiento, este debe tener servicios funcionando sobre él.
Si todo esta funcionando, es tan sencillo como sobre uno de los nodos manager, ejecutar el siguiente comando:
```sh
$ docker service create \
  --name=viz \
  --publish=8080:8080/tcp \
  --constraint=node.role==manager \
  --mount=type=bind,src=/var/run/docker.sock,dst=/var/run/docker.sock \
  dockersamples/visualizer
```
De tal manera que a través del puerto 8080, podremos ver una web con una interfaz gráfica sencilla todos los nodos que estan en el Swarn y que dockers corren sobre ellos.
Para más información consultar la guia en la imagen oficial de Visualizer.
 - https://hub.docker.com/r/dockersamples/visualizer