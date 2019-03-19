# Instalar servidor NFS en un Nodo (CentOS 7).

## En el servidor
Instalamos los paquetes necesarios mediante el siguiente comando:
```sh
$ yum install nfs-utils
```

Creamos el directorio que usaremos como NFS, y le asignaremos los permisos correspondientes:
```sh
$ mkdir /var/nfsshare
$ chmod -R 755 /var/nfsshare
$ chown nfsnobody:nfsnobody /var/nfsshare
```

Editamos el fichero `/etc/exports` e introducimos el siguiente contenido:
```
/var/nfsshare    *(rw,sync,no_root_squash,no_all_squash)
```
Tener en cuenta que el `*` indica que permitimos entrar al servidor a cualquiera, si solo quisieramos por ejemplo, que la IP `192.168.1.99` entrase, remplazaríamos el `*` con esta.

Reiniciamos el servicio NFS:
```sh
$ systemctl restart nfs-server
```

## En el cliente
Instalamos los paquetes necesarios mediante el siguiente comando:
```sh
$ yum install nfs-utils
```

Creamos el directorio donde queremos montar el NFS:
```sh
$ mkdir -p /mnt/nfs/var/nfsshare
```

Montamos el NFS en el directorio que acabamos de crear, tener en cuenta, que `192.168.1.100` es la IP del servidor.
```sh
$ mount -t nfs 192.168.1.100:/var/nfsshare /mnt/nfs/var/nfsshare/
```

Con el siguiente comando podemos ver que se ha montado con exito:
```sh
$ df -kh
```