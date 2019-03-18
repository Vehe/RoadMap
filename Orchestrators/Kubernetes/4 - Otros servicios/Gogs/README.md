# Gogs en Kubernetes Cluster

## Instalar MySQL
Debemos tener una base de datos antes de comenzar con la instalación de `Gogs`, por ello, instalaremos MySQL.
Crearemos el fichero `mysql-dep-serv.yaml`, el cual contendrá el siguiente contenido:
```
apiVersion: v1
kind: Service
metadata:
  name: mysql
spec:
  ports:
  - port: 3306
  selector:
    app: mysql
  clusterIP: None
---
apiVersion: apps/v1 # for versions before 1.9.0 use apps/v1beta2
kind: Deployment
metadata:
  name: mysql
spec:
  selector:
    matchLabels:
      app: mysql
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
      - image: mysql:5.6
        name: mysql
        env:
          # Use secret in real usage
        - name: MYSQL_ROOT_PASSWORD
          value: password
        - name: MYSQL_DATABASE
          value: gogs
        ports:
        - containerPort: 3306
          name: mysql
```

Crea un deployment y un servicio, al iniciar crea la base de datos `gogs` que es la que usaremos posteriormente, y la contraseña del usuario root de MySQL es `password`.

Ejecutamos lo siguiente para hacerlo efectivo:
```sh
$ kubectl create -f mysql-dep-serv.yaml
```

Comprobamos que el Pod se ha creado correctamente y esta funcionando:
```sh
$ kubectl get pods
```

## Instalar Gogs

Ahora es el turno de Gogs, para ello, crearemos un nuevo deployment, escribimos lo siguiente en un archivo llamado `gogs-deployment.yaml`.
```
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: gogs
spec:
  replicas: 1
  template:
    metadata:
      labels:
        name: gogs
    spec:
      containers:
      - resources:
        name: gogs
        image: gogs/gogs
        ports:
        - name: gogs-port
          containerPort: 3000
        volumeMounts:
        - mountPath: /data
          name: data
      volumes:
      - name: data
        hostPath:
          path: /home/centos/gogs/data
```

Ejecutamos lo siguiente para hacerlo efectivo:
```sh
$ kubectl create -f gogs-deployment.yaml
```

Comprobamos que el Pod esta activo y funcionando con el mismo comando que en MySQL.

Si queremos hacer el servicio a partir de este, creamos el fichero `gogs-service.yaml` y le insertamos el siguiente contenido:
```
apiVersion: v1
kind: Service
metadata:
  name: gogs
  labels:
    name: gogs
spec:
  ports:
  - port: 3001
    targetPort: 3000
    nodePort: 30130
  selector:
    name: gogs
  type: NodePort
```

Si no deseamos esto, podemos hacer un `port-forward` con el siguiente comando:
```sh
$ kubectl port-forward --address 0.0.0.0 gogs-67bc8b67bd-xbt2d 10000:3000
```

Donde `gogs-67bc8b67bd-xbt2d` es el nombre del Pod, y `10000` es el puerto donde queremos que funcione en el localhost.