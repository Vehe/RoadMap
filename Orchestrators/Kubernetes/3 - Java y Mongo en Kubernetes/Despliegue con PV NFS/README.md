# Despliegue con PV NFS

Desplegaremos la misma aplicación de Java y MongoDB sobre el cluster de kubernetes, pero esta vez, monogo tendrá un volumen persistente con NFS, el servidor de este, será uno de los nodos que conforman el cluster con la IP `192.168.1.7`.

Primero crearemos el `Persistent Volumes` para mongo con el siguiente archivo YAML.
```
apiVersion: v1
kind: PersistentVolume
metadata:
  name: mongo-pv
spec:
  capacity:
    storage: 10Gi
  accessModes:
    - ReadWriteMany
  nfs:
    server: 192.168.1.7
    path: "/var/nfsshare"
```

Ahora debemos crear el `PersistentVolumeClaim`, para ello, crearemos un archivo con el siguiente contenido:
```
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mongo-pvc
spec:
  accessModes:
    - ReadWriteMany
  storageClassName: ""
  resources:
    requests:
      storage: 10Gi
```

Para finalizar con mongo, crearemos el archivo que contendrá el `service` y el `deployment` para crear el pod de mongo:
```
apiVersion: v1
kind: Service
metadata:
  name: javaymongo-mongodb
  labels:
    app: javaymongo
spec:
  ports:
    - port: 27017
  selector:
    app: javaymongo
    tier: mongo
  clusterIP: None
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: javaymongo-mongodb
  labels:
    app: javaymongo
spec:
  selector:
    matchLabels:
      app: javaymongo
      tier: mongo
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: javaymongo
        tier: mongo
    spec:
      containers:
      - image: mongo
        name: mongo
        ports:
        - containerPort: 27017
          name: mongo
        volumeMounts:
        - name: mongo-db-v
          mountPath: /data/db
      volumes:
      - name: mongo-db-v
        persistentVolumeClaim:
          claimName: mongo-pvc
```

Creamos todo lo que escribimos anteriormente en los ficheros con los siguientes comandos:
```sh
$ kubectl create -f mongo-pv.yaml
$ kubectl create -f mongo-pvc.yaml
$ kubectl create -f mongo-deployment-service.yaml
```

Si todo ha ido correctamente, podremos ver que los por tienen todos un Status de `Running`.
```sh
$ kubectl get pods
```

Si por algun motivo algo no ha ido bien, o se ha quedado creando el Pod de mongo, puede ser por un fallo al conectar al NFS, podemos ver más detalles mediante:
```sh
$ kubectl describe pods [pod_name]
```

Solo quedaría desplegar Java de la misma forma que la vez anterior, por lo que no se explica de nuevo.

## Resources 
- https://matthewpalmer.net/kubernetes-app-developer/articles/kubernetes-volumes-example-nfs-persistent-volume.html
- https://www.jorgedelacruz.es/2017/12/26/kubernetes-volumenes-nfs/
- http://blog.itp-inc.com/deploy-mongodb-cluster-as-a-microservice-on-kubernetes-with-persistent-storage/
- https://kubernetes.io/docs/concepts/storage/persistent-volumes/