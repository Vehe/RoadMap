# Java y MongoDB en Kubernetes

Antes de comenzar tenemos que asegurarnos de que tenemos un cluster funcionando y con varios nodos activos, si todos tienen como estado `Ready`, podemos comenzar.

Las imagenes que vamos a usar para este apartado son:
- mongo: La imagen original del repositorio oficial de mongoDB
- v3he/javaymongo:k8s : Esta imagen es mi aplicación de java que corre en el puerto 9000, es la version para Kubernetes, ya que le hice una pequeña modificación para pasarle el host contenedor de la base de datos a través de una variable de entorno.

Creamos el fichero `mongo-deployment.yaml` que desplegará el `service` y el `deployment` de la base de datos mongo donde almacenaremos la información.

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
```

Ahora crearemos el fichero `javaymongo-deployment.yaml`, el cual también desplegará el `service` y el `deployment`. Como dije anteriormente, este necesita que se le pase la variable de entorno `MONGO_DB_HOST` con el nombre del servicio de mongo que creamos anteriormente, además, este tiene el `type: LoadBalancer`, lo cual nos permite obtener una IP externa al cluster para acceder, para más información a cerca de esto, leer https://kubernetes.io/docs/tasks/access-application-cluster/create-external-load-balancer/

```
apiVersion: v1
kind: Service
metadata:
  name: javaymongo
  labels:
    app: javaymongo
spec:
  ports:
    - port: 9000
  selector:
    app: javaymongo
    tier: frontend
  type: LoadBalancer
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: javaymongo
  labels:
    app: javaymongo
spec:
  selector:
    matchLabels:
      app: javaymongo
      tier: frontend
  replicas: 4
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: javaymongo
        tier: frontend
    spec:
      containers:
      - image: v3he/javaymongo:k8s
        name: javaymongo
        env:
        - name: MONGO_DB_HOST
          value: javaymongo-mongodb
        ports:
        - containerPort: 9000
          name: javaymongo
```

Para ver que todo funciona correctamente, ejecutaremos el siguiente comando:
```sh
$ kubectl get pods
```

Y deberíamos ver 4 replicas de `javaymongo` y una de `javaymongo-mongodb`.

Para obtener la IP externa al cluster de la que hablé antes, ejecutaremos el siguiente comando:
```sh
$ kubectl get services javaymongo -o yaml |grep clusterIP
```

Ahora a través de esa IP y accediendo al puerto `9000`, que es donde corre nuestra aplicación de java, podremos acceder a ella sin problemas, y como vermos, cada vez nos da una IP distinta, ya que la petición se procesa de manera balanceada sobre los Pod.
