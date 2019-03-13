# Jenkins

## ¿Qué es?

Es un software de integración continua escrito en Java.

Se entiende por integración la compilación y ejecución de pruebas de todo un proyecto . El proceso se lleva a cabo normalmente cada cierto tiempo y su función es la descarga de las fuentes desde el control de versiones, su posterior compilación, la ejecución de pruebas y generar informes.

Se pueden ejecutar de forma inmediata las pruebas unitarias y hay una monitorización continua de las métricas de calidad del proyecto.

Por último, destacar que Jenkins puede extenderse mediante plugins . Actualmente hay una gran cantidad de plugins que permiten cambiar su comportamiento o añadir nuevas funcionalidades.

## Despliegue sober Docker Swarm

Para desplegar Jenkins sobre Swarm, primero necesitamos tener este inicializado.
Vamos a crear un nuevo servicio de Jenkins sobre el Swarm, para ello, ejecutamos:
```sh
$ docker service create -p 9090:8080 -p 3000:50000 jenkins/jenkins:lts
```

Si accedemos al puerto `9090` del localhost, podremos ver la UI de Jenkins, pero nos pide un codigo para la instalación, para ver ese codigo solo tendremos que ejecutar lo siguiente:
```sh
$ docker exec -it d9885d288406 cat /var/jenkins_home/secrets/initialAdminPassword
```
Donde en este caso `d9885d288406` es el ID del contenedor de Jenkins.
Para más información y documentación consultar la guia oficial.

## Resources
 - https://hub.docker.com/r/jenkins/jenkins
 - https://github.com/jenkinsci/docker/blob/master/README.md