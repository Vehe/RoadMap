# Gogs

## ¿Qué es?
Gogs (Go Git Service) es una forja multiplataforma basada en git escrita en Go. Se trata de un software libre, bajo licencia MIT. Su peculiaridad es de ser ligero y que puede funcionar sobre mapa ARM, lo que hace que está adaptado al auto-alojamiento. Gogs tiene una interfaz web similar a la de GitHub.

## Funcionalidades
- Gestionar repositorios Git así como sus usuarios y derechos de accesos a los mismos.
- Trabajar de manera colaborativa en repositorios privados.
- Autenticación de dos factores y conexión a un protocolo ligero de acceso a directorios LDAP.
- Gestionar el acceso por rama a repositorios.
- Efectuar exámenes de código y reforzar la colaboración con solicitudes de fusion.
- Herramienta de informe de bug.
- Crear repositorios espejos.
- Creación de wiki.

## Despliegue de Gogs en Swarm
Para desplegar Gogs sobre Swarm, primero necesitamos tener este inicializado.
Para que Gogs funcione necesitamos una base de datos MySQL o similar, por lo que crearemos un archivo `docker-compose.yml` en el que pondremos todas las instrucciones para crear estos servicios.
```sh
$ docker stack deploy --compose-file=docker-compose.yml GOGS
```

El contenido del archivo `docker-compose.yml` es el siguiente:
```
version: '3'
services:
    gogs:
        image: gogs/gogs
        depends_on:
            - db
        ports:
            - "3000:3000"
    db:
        image: mysql
        environment:
            MYSQL_ROOT_PASSWORD: root
            MYSQL_DATABASE: gogs
```

Como podemos ver, la imagen de Gogs depende primero de que si inicie una base de datos MySQL, para ello creamos esta estableciendole ciertas variables de entorno, como son la clave para el usuario root, y el nombre de la base de datos que crear al iniciar.

A su vez, tenemos que establecer el puerto `3000` del container de Gogs con el puerto `3000` del host.
Por este podremos acceder a la interfaz grafica que Gogs trae.

Para más información y documentación consultar la guia oficial.
## Resources
 - https://hub.docker.com/r/gogs/gogs/
 - https://gogs.io/docs/installation/configuration_and_run
 - https://hub.docker.com/_/mysql