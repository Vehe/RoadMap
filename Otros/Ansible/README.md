# Ansible

![Ansible Logo](https://upload.wikimedia.org/wikipedia/commons/0/05/Ansible_Logo.png)

## ¿Qué es Ansible?
Ansible es una herramienta que nos permite gestionar configuraciones, aprovisionamiento de recursos, despliegue automático de aplicaciones y muchas otras tareas de TI de una forma limpia y sencilla.

## ¿En qué se diferencia de otras herramientas similares?
- No necesita agentes
- No requiere de configuraciones engorrosas y complicadas
- Flexibilidad (API, Módulos, Plugins)
- Facilidad de uso

## Instalar Ansible
La instalación es bastante simple, una vez instalado no encontraras base de datos o algún demonio que monitorizar o inicializar.

Sólo es necesario instalarlo en una máquina, que actuará como punto central de la aplicación. Cabe destacar que Ansible no necesita software o herramientas en los nodos donde se ejecute, por lo que el mantenimiento y actualización de la versión es muy sencillo.

Para instalarlo sobre un CentOS 7 es tan sencillo como ejecutar (necesitamos tener python 2.6 o 2.7 instalado): 
```sh
$ yum install ansible
```

## Laboratorio

Lo instalaremos en un entorno que consta de varias máquinas:
- 192.168.1.17 (La máquina master, donde esta instalado ansible)
- 192.168.1.6 (Nodo)
- 192.168.1.7 (Nodo)

Para no tener que usar IP's, añadiremos esto al siguiente archivo `/etc/hosts` de la máquina master:
```
192.168.1.7  openstack03.pablo
192.168.1.6  openstack02.pablo
```

De tal manera que ahora nos referiremos a estas IP, mediante su correspondiente nombre.

## Generar la clave ssh

Para que la máquina master tenga acceso a todas las máquinas del laboratorio, lo que vamos ha hacer es generar una clave ssh con el comando:
```sh
$ ssh-keygen
```

Copiaremos el fichero `~/.ssh/id_rsa.pub` en cada nodo en el directorio `~/.ssh/authorized_keys`.









