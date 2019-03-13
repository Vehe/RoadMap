# Portainer

## ¿Qúe es?

it’s a powerful, open-source management toolset that allows you to easily build, manage and maintain Docker environments.

## Despligue sobre Docker Swarm

Para comenzar a utilizar Portainer, necesitamos tener funcionando Docker Swarm, y para probar el funcionamiento, este debe tener servicios funcionando sobre él.
Si todo esta funcionando correctamente, lo que debemos hacer es descargar el archivo YAML de configuración de esta forma:
```sh
$ curl -L https://downloads.portainer.io/portainer-agent-stack.yml -o portainer-agent-stack.yml
```

Posterormente, solo debemos hacer un deploy sobre ese archivo YAML.
```sh
$ docker stack deploy --compose-file=portainer-agent-stack.yml portainer
```

Cuando este se ponga a funcionar, podremos apreciar que hay un docker en cada uno de los nodos con el nombre de `portainer_agent`, y uno con el nombre de `portainer_portainer`.
Por defeto Portainer funciona en el puerto `9000`, ya que así viene especificado en el fichero YAML, pero podemos cambiarlo a cualquier puerto del host que tengamos disponible.

Para más información consultar la guia en la imagen oficial de Portainer.

## Resources
 - https://hub.docker.com/r/portainer/portainer/
 - https://portainer.readthedocs.io/en/latest/deployment.html