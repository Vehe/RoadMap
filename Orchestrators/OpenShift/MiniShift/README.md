# MiniShift

Minishift es una herramienta open-source que permite correr OpenShift localmente corriendo un single-node cluster dentro de una máquina virtual.

## Instalación en Windows

Descargaremos `MiniShift` desde la siguiente URL (Necesitamos tener instalado VirtualBox):

- https://www.okd.io/minishift/
- https://www.virtualbox.org/wiki/Downloads

Desde la terminal, ejecutaremos los siguientes comandos:
```
> minishift config set vm-driver virtualbox
> minishift start
```

Dejamos que el instalador inicie la máquina virtual, la cual tardará un par de minutos según nuestra velocidad de internet.
Una vez acabe, veremos algo como el siguiente output:
```
Login to server ...
Creating initial project "myproject" ...
Server Information ...
OpenShift server started.

The server is accessible via web console at:
    https://192.168.99.100:8443/console

You are logged in as:
    User:     developer
    Password: <any value>

To login as administrator:
    oc login -u system:admin
```

Mediante la URL que se nos muestra, podemos acceder a la interfáz gráfica de `OpenShift Web Console`, el usuario por defecto es `developer` y la contraseña es cualquiera que nosotros pongamos por priemra vez.