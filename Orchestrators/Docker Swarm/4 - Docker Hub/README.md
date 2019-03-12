# Docker Hub
Nos conectamos desde un cliente de docker a nuestra cuenta en Docker Hub:
```sh
$ docker login --username test
```

Debemos crear un repositorio, mediante la web de Docker Hub (https://hub.docker.com/).

Una vez que tenemos la imágen que queremos subir al repositorio, tenemos que ponerle un tag, con el comando:
```sh
$ docker tag 881bd08c0b08 test/testdockerhub:latest
```
Donde:
- `881bd08c0b08` es el ID de la imágen que queremos.
- `test` es el nombre de usuario que tenemos.
- `testdockerhub` es el nombre del repositorio que acabamos de crear.
- `latest` es el nombre del tag que le asignamos (no tiene porque ser latest).

Una vez que hemos hecho esto, solo nos queda subir la imagen al repositorio, mediante el siguiente comando:
```sh
$ docker push test/testdockerhub:latest
```

Si queremos descargarnos la imagen ya subida al repositorio, solo tenemos que ejecutar el comando:
(Si no ponemos ningun `:tag`, descarga la imagen que tenga el tag `latest`)
```sh
$ docker pull v3he/testdockerhub
```


## Resources
- https://ropenscilabs.github.io/r-docker-tutorial/04-Dockerhub.html