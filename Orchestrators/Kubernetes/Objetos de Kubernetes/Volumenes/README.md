# Volúmenes

Como hemos comentado anteriormente los pods son efímero, la información guardada en ellos no es persistente, pero es evidentemente que necesitamos que nuestras aplicaciones tengan la posibilidad de que su información no se pierda. La solución es añadir volúmenes (almacenamiento persistente) a los pods para que lo puedan utilizar los contenedores. Los volúmenes son considerados otro recurso de Kubernetes.

## Definiendo volúmenes en un pod
En la definición de un pod, además de especificar los contenedores que lo van a formar, también podemos indicar los volúmenes que tendrá. Además la definición de cada contenedor tendremos que indicar los puntos de montajes de los diferentes volúmenes. Por ejemplo, el el fichero `pod-nginx.yaml` podemos ver la definición de tres tipos de volúmenes en un pod:

```
apiVersion: v1
kind: Pod
metadata:
  name: www
spec:
  containers:
  - name: nginx
    image: nginx
    volumeMounts:
    - mountPath: /home
      name: home
    - mountPath: /git
      name: git
      readOnly: true
    - mountPath: /temp
      name: temp
  volumes:
  - name: home
    hostPath:
      path: /home/debian
  - name: git
    gitRepo:
      repository: https://github.com/josedom24/kubernetes.git
  - name: temp
    emptyDir: {}
```

En la sección volumes definimos los volúmenes disponibles y en la definción del contenedor, con la etiqueta volumeMounts, indicamos los puntos de montajes.

Hemos definido tres volúmenes de diferente tipo:

- hostPath: Este volumen corresponde a un directorio o fichero del nodo donde se crea el pod. Como vemos se monta en el directorio /home del contenedor. En cluster multinodos este tipo de volúmenes no son efectivos, ya que no tenemos duplicada la información en los distintos nodos y su contenido puede depender del nodo donde se cree el pod.
- gitRepo: El contenido del volumen corresponde a un repositorio de github, lo vamos a montar en el el directorio /git y como vemos lo hemos configurado de sólo lectura.
- emptyDir: El contenido de este volumen, que hemos montado en el directorio /temp se borrará al eliminar el pod. Lo utilizamos para compartir información entre los contenedores de un mismo pod.
- 
En la documentación de Kubernetes puedes encontrar todos los tipos de [volúmenes que podemos utilizar](https://kubernetes.io/docs/concepts/storage/volumes/).

Veamos los volúmenes que hemos creado:

```
$ kubectl create -f pod-nginx.yaml 
pod "www" created

$ kubectl describe pod www
...
Volumes:
  home:
    Type:          HostPath (bare host directory volume)
    Path:          /home/debian
    HostPathType:  
  git:
    Type:        GitRepo (a volume that is pulled from git when the pod is created)
    Repository:  https://github.com/josedom24/kubernetes.git
    Revision:    
  temp:
    Type:    EmptyDir (a temporary directory that shares a pod's lifetime)
    Medium:  
```

Accedemos al pod y vemos los contenidos de cada directorio:

```
$ kubectl exec -it www -- bash

root@www:/# cd /home
root@www:/home# ls
fichero.txt
root@www:/home# cd /git
root@www:/git# ls
kubernetes
root@www:/git# cd /temp
root@www:/temp# ls
root@www:/temp# ^C
root@www:/temp# 
```

- El directorio /home tiene un fichero que esta en el directorio /home/debian del nodo donde se ha ejecutado el pod.
- El directorio /git tiene el contenido del repositorio github que hemos indicado.
- El directorio /temp corresponde a un directorio vacío que podemos utilizar para compartir información entre los contenedores del pod,la información de este directorio se perderá al eliminarlo.

## Compartiendo información en un pod
Veamos con un ejemplo la posibilidad de compartir información entre contenedores de un pod. En el fichero `pod2-nginx.yaml` creamos un pod con dos contenedores y un volumen:

```
apiVersion: v1
kind: Pod
metadata:
  name: two-containers
spec:

  volumes:
  - name: shared-data
    emptyDir: {}

  containers:
  - name: nginx-container
    image: nginx
    volumeMounts:
    - name: shared-data
      mountPath: /usr/share/nginx/html

  - name: busybox-container
    image: busybox
    command:
      - sleep
      - "3600"
    volumeMounts:
    - name: shared-data
      mountPath: /pod-data
```

Vamos a crear el pod, vamos acceder al contenedor busybox-cotainer y vamos a escribir un index.html, que al estar compartido por el contenedor nginx-container podremos ver ala acceder a él.

```
$ kubectl create -f pod2-nginx.yaml 
pod "two-containers" created
    
$ kubectl get pods 
NAME             READY     STATUS    RESTARTS   AGE
two-containers   2/2       Running   0          10s
www              1/1       Running   0          1h

$ kubectl exec -it two-containers -c busybox-container -- sh
/ # cd /pod-data/
/pod-data # echo "Prueba de compartir información entre contenedores">index.html
/pod-data # exit

$ kubectl port-forward two-containers 8080:80
Forwarding from 127.0.0.1:8080 -> 80
Forwarding from [::1]:8080 -> 80
Handling connection for 8080
```

![Example HTML](https://www.josedomingo.org/pledin/assets/wp-content/uploads/2019/03/compartir.png)

# Almacenamiento disponible en Kubernetes: PersistentVolumen
Ya hemos visto que podemos añadir almacenamiento a un pod, sin embargo habría que distinguir dos conceptos:

- El desarrollador de aplicaciones no debería conocer con profundidad las características de almacenamiento que le ofrece el cluster. Desde este punto de vista, al desarrollador le puede dar igual que tipo de volumen puede utilizar (aunque en algún caso puede ser interesante indicarlo), lo que le interesa es, por ejemplo, el tamaño y las operaciones (lectura, lectura y escritura) del almacenamiento que necesita, y obtener del cluster un almacenamiento que se ajuste a esas características. La solicitud de almacenamiento se realiza con un elemento del cluster llamado PersistentVolumenCliams.
- El administrador será el responsable de dar de alta en el cluster los distintos tipos de almacenamientos que hay disponibles, y que se representa con un recurso llamado PersistentVolumen.

## Definiendo un PersistentVolumen
Un PersistentVolumen es un objeto que representa los volúmenes disponibles en el cluster. En él se van a definir los detalles del backend de almacenamiento que vamos a utilizar, el tamaño disponible, los modos de acceso, las políticas de reciclaje, etc.

Tenemos tres modos de acceso, que depende del backend que vamos a utilizar:

- ReadWriteOnce: read-write solo para un nodo (RWO)
- ReadOnlyMany: read-only para muchos nodos (ROX)
- ReadWriteMany: read-write para muchos nodos (RWX)

Las políticas de reciclaje de volúmenes también depende del backend y son:

- Retain: Reclamación manual
- Recycle: Reutilizar contenido
- Delete: Borrar contenido

## Creando un PersistentVolumen con NFS
Vamos a instalar en el master del cluster (lo podríamos tener en cualquier otro servidor) un servidor NFS para compartir directorios en los nodos del cluster.

### En el servidor
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

### En el cliente
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

Donde `192.168.1.100` es la IP del servidor NFS.

Con el siguiente comando podemos ver que se ha montado con exito:
```sh
$ df -kh
```

## Creación del volumen en Kubernetes
Ya podemos crear el volumen utilizando el objeto PersistentVolumen. Lo definimos en el fichero `nfs-pv.yaml`:

```
apiVersion: v1
kind: PersistentVolume
metadata:
  name: nfs-pv
spec:
  capacity:
    storage: 5Gi
  accessModes:
    - ReadWriteMany
  persistentVolumeReclaimPolicy: Recycle
  nfs:
    path: /var/shared
    server: 10.0.0.4
```

Y lo creamos y vemos el recurso:

```
$ kubectl create -f nfs-pv.yaml 
persistentvolume "nfs-pv" created

$ kubectl get pv
NAME      CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS      CLAIM     STORAGECLASS   REASON    AGE
nfs-pv    5Gi        RWX            Recycle          Available                                      10s
```

El tipo de volumen disponible lo vamos a referenciar cin su nombre (nfs-pv), tiene 5Gb de capacidad, estamos utilizando NFS, el modo de acceso es RWX y su política de reciclaje es de reutilización del contenido.

## Solicitud de almacenamiento en Kubernetes: PersistentVolumenClaims

A continuación si nuestro pod necesita un volumen, necesitamos hacer una solicitud de almacenamiento usando un objeto del tipo PersistentVolumenCliams.

Cuando creamos un PersistentVolumenCliams, se asignará un PersistentVolumen que se ajuste a la petición. Está asignación se puede configurar de dos maneras distintas:

- Estática: Primero se crea todos los PersistentVolumenCliams por parte del administrador, que se irán asignando conforme se vayan creando los PersistentVolumen.
- Dinámica: En este caso necesitamos un “provisionador” de almacenamiento (para cada uno de los backend), de tal manera que cada vez que se cree un PersistentVolumen, se creará bajo demanda un PersistentVolumenCliams que se ajuste a las características seleccionadas.

## Creación de PersistentVolumenClaim

Siguiendo con el ejercicio anterior vamos a crear una solicitud de almacenamiento del volumen creado anteriormente con NFS. Definimos el objeto en el fichero `nfs-pvc.yaml`:

```
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: nfs-pvc
spec:
  accessModes:
    - ReadWriteMany
  resources:
    requests:
      storage: 1Gi
```

Creamos el recurso y obtenemos los recursos que tenemos a nuestra disposición:

```
$ kubectl create -f nfs-pvc.yaml
persistentvolumeclaim "nfs-pvc" created

$ kubectl get pv,pvc
NAME                      CAPACITY   ACCESS MODES   RECLAIM POLICY   STATUS    CLAIM             AGE
persistentvolume/nfs-pv   5Gi        RWX            Recycle          Bound     default/nfs-pvc   14m

NAME                            STATUS    VOLUME    CAPACITY   ACCESS MODES   STORAGECLASS   AGE
persistentvolumeclaim/nfs-pvc   Bound     nfs-pv    5Gi        RWX                           15s
```

Como podemos observar al crear el pvc se busca del conjunto de pv uno que cumpla sus requerimientos, y se asocian (status bound) por lo tanto el tamaño indicado en el pvc es el valor mínimo de tamaño que se necesita, pero el tamaño real será el mismo que el del pv asociado.

Si queremos añadir un volumen a un pod a partir de esta solicitud, podemos usar la definición del fichero `pod-nginx-pvc.yaml`:

```
apiVersion: v1
kind: Pod
metadata:
  name: www-vol
spec:
  containers:
  - name: nginx
    image: nginx
    volumeMounts:
      - mountPath: /usr/share/nginx/html
        name: nfs-vol
  volumes:
    - name: nfs-vol
      persistentVolumeClaim:
        claimName: nfs-pvc
```

Lo creamos y accedemos a él:

```
$ kubectl create -f pod-nginx-pvc.yaml
pod "www-vol" created

$ kubectl port-forward www-vol 8080:80
Forwarding from 127.0.0.1:8080 -> 80
Forwarding from [::1]:8080 -> 80
```

![Example HTML 2](https://www.josedomingo.org/pledin/assets/wp-content/uploads/2019/03/nginx-pvc.png)

Evidentemente al montar el directorio DocumentRoot del servidor (`/usr/share/nginx/html`) en el volumen NFS, no tiene index.html, podemos crear uno en el directorio compartido del master y estará disponible en todos los nodos:
(`172.22.201.15` es la IP del servidor NFS).

```
$ echo "It works..." | ssh debian@172.22.201.15 'cat >> /var/shared/index.html'
 
$ kubectl port-forward www-vol 8080:80
Forwarding from 127.0.0.1:8080 -> 80
Forwarding from [::1]:8080 -> 80
```

![Example HTML 3](https://www.josedomingo.org/pledin/assets/wp-content/uploads/2019/03/nginx-pvc2.png)

Si escalamos el pod no tendríamos ningún problema ya que todos los nodos del cluster comparten el mismo directorio referenciado por el volumen. Además el contenido del volumen es persistente, y aunque eliminemos el pod, la información no se pierde:

```
$ kubectl delete pod www-vol
pod "www-vol" deleted

$ kubectl create -f pod-nginx-pvc.yaml
pod "www-vol" created

$ kubectl port-forward www-vol 8080:80
Frwarding from 127.0.0.1:8080 -> 80
Forwarding from [::1]:8080 -> 80
```

Y desde otro terminal:

```
$ curl http://localhost:8080
It works...
```

Además podemos comprobar cómo se ha montado el volumen en el contenedor:

```
$ kubectl exec -it www-vol -- bash

root@www-vol:/# df -h
...
10.0.0.4:/var/shared   20G  4.8G   15G  26% /usr/share/nginx/html
...
```