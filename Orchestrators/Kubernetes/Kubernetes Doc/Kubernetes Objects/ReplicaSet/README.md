# ReplicaSet

ReplicaSet es un recurso de Kubernetes que asegura que siempre se ejecute un número de replicas de un pod determinado. Por lo tanto, nos asegura que un conjunto de pods siempre están funcionando y disponibles. Nos proporciona las siguientes características:

- Que no haya caída del servicio
- Tolerancia a errores
- Escabilidad dinámica

![ReplicaSet](https://www.josedomingo.org/pledin/assets/wp-content/uploads/2018/07/rs.png)

Ejemplo archivo de creación de un ReplicaSet:
```
apiVersion: extensions/v1beta1
kind: ReplicaSet
metadata:
  name: nginx
  namespace: default
spec:
  replicas: 2
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
        - image:  nginx
          name:  nginx
```

- replicas: Indicamos el número de pos que siempre se van a estar ejecutando.
- selector: Indicamos el pods que vamos a replicar y vamos a controlar con el ReplicaSet. En este caso va a controlar pods que tenga un label app cuyo valor sea nginx. Si no se indica el campo selector se seleccionar or defecto los pods cuyos labels sean iguales a los que hemos declarado en la sección siguiente.
- template: El recurso ReplicaSet contiene la definición de un pod.

## Crear un ReplicaSet
Al crear el ReplicaSet se crearán los pods que hemos indicado como número de replicas:

```
$ kubectl create -f nginx-rs.yaml
replicaset.extensions "nginx" created
```
Veamos el ReplicaSet creado y los pods que ha levantado.

```
$ kubectl get rs
NAME      DESIRED   CURRENT   READY     AGE
nginx     2         2         2         44s

$ kubectl get pods
NAME          READY     STATUS    RESTARTS   AGE
nginx-5b2rn   1/1       Running   0          1m
nginx-6kfzg   1/1       Running   0          1m
```
¿Qué pasaría si borro uno de los pods que se han creado? Inmediatamente se creará uno nuevo para que siempre estén ejecutándose los pods deseados, en este caso 2:

```
$ kubectl delete pod nginx-5b2rn
pod "nginx-5b2rn" deleted

$ kubectl get pods
NAME          READY     STATUS              RESTARTS   AGE
nginx-6kfzg   1/1       Running             0          2m
nginx-lkvzj   0/1       ContainerCreating   0          4s

$ kubectl get pods
NAME          READY     STATUS    RESTARTS   AGE
nginx-6kfzg   1/1       Running   0          2m
nginx-lkvzj   1/1       Running   0          8s
```

## Modificar un ReplicaSet
En cualquier momento puedo escalar el número de pods que queremos que se ejecuten:

```
$ kubectl scale rs nginx --replicas=5
replicaset.extensions "nginx" scaled

$ kubectl get pods --watch
NAME          READY     STATUS    RESTARTS   AGE
nginx-6kfzg   1/1       Running   0          5m
nginx-bz2gs   1/1       Running   0          46s
nginx-lkvzj   1/1       Running   0          3m
nginx-ssblp   1/1       Running   0          46s
nginx-xxg4j   1/1       Running   0          46s
```
Como anteriormente vimos podemos modificar las características de un ReplicaSet con la siguiente instrucción:

```
$ kubectl edit rs nginx
```

## Borrar un ReplicaSet
Por último si borramos un ReplicaSet se borraran todos los pods asociados:
```
$ kubectl delete rs nginx
replicaset.extensions "nginx" deleted

$ kubectl get rs
No resources found.

$ kubectl get pods 
No resources found.
```

El uso del recurso ReplicaSet sustituye al uso del recurso `ReplicaController`, más concretamente el uso de Deployment que define un ReplicaSet.