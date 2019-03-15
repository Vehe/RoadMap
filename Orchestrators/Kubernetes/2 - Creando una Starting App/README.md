# Starting App en Kubernetes

Para crear una starting app, debemos tener ya iniciado el cluster, y con los nodos ya añadidos y con estatus `Ready`.

Creamos el fichero `nginx-deployment.yaml` con el siguiente contenido:
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
spec:
  selector:
    matchLabels:
      app: nginx
  replicas: 2
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx-test-pablo
        image: nginx
        ports:
        - containerPort: 80
```

Ahora ejecutamos el siguiente comando para crear el deployment a partir del fichero anterior:
```sh
$ kubectl apply -f nginx-deployment.yaml
```

Con este comando podremos ver los deployments que hay en el cluster.
```sh
$ kubectl get deployments
```

Si queremos ver información más concreta sobre alguno de estos, solo tenemos que ejecutar lo siguiente.
```sh
$ kubectl describe deployment nginx-deployment
```

Vamos a ver los pods que hemos creado, en este caso, debería haber 2, ya que en el fichero especificamos que queríamos 2 replicas.
```sh
$ kubectl get pods -l app=nginx
```

Para informacíon más concreta sobre alguno de los Pod, ejecutamos lo siguiente:
```sh
$ kubectl describe pod [Pod name]
```

## Updating the Deployment

Moficamos el fichero que creamos anteriormente `nginx-deployment.yaml` y editamos la línea:
```
replicas: 4 # Estaba puesto en 2 anteriormente
```

Volvemos a ejecutar el comando:
```sh
$ kubectl apply -f nginx-deployment.yaml
```

Y comprobamos que efectivamente se ha ejecutado satisfactoriamente, por lo que deberíamos ver 4 Pods, en lugar de 2.
```sh
$ kubectl get pods -l app=nginx
```

## Borrar un Deployment

Para borrar el Deployment, solo tenemos que ejecutar el siguiente comando:
```sh
$ kubectl delete deployment nginx-deployment
```

Donde `nginx-deployment` es el nombre que corresponda a nuestro deployment.

## Resources
- https://kubernetes.io/docs/tasks/run-application/run-stateless-application-deployment/