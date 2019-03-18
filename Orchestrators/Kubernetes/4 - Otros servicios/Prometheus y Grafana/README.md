# Prometheus y Grafana en Kubernetes Cluster
Todos los archivos que se nombran durante esta página se encuentran en el mismo repositorio que este.
Para iniciar el proceso de instalación, debemos tener creado con anterioridad un cluster con varios nodos y como ejemplo, tener un nginx corriendo con varias replicas.

## Instalar Prometheus
Creamos un `namespace` que usaremos para tener todo lo relacionado con la monitorización.
```sh
$ kubectl create namespace monitoring
```

Vamos a asignar permisos al cluster sobre el namespace que acabamos de crear, para que prometheus pueda obtener las metrics sobre la API de Kubernetes, para ello, usarmos el fichero `clusterRole.yaml`.
```sh
$ kubectl create -f clusterRole.yaml
```

Creamos un `ConfigMap` para prometheus, para ello, usarmos el fichero `config-map.yaml`, ejecutando:
```sh
$ kubectl create -f config-map.yaml -n monitoring
```

Ahora crearemos el `Deployment` de este, para ello, usaremos el siguiente archivo, asi como el `ConfigMap` que creamos anteriormente.
```sh
$ kubectl create -f prometheus-deployment.yaml --namespace=monitoring
```

Comprobamos que el deployment se ha creado satisfactoriamente, con el siguiente comando:
```sh
$ kubectl get deployments --namespace=monitoring
```

Vemos los Pod de prometheus si ejecutamos:
```sh
$ kubectl get pods --namespace=monitoring
```

Prometheus corre en el puerto `9090`, ya que en este caso el cluster funciona sobre las máquinas de OpenStack, para acceder a la interfaz de prometheus tenemos que hacer uso de `port-forward`, asi que accederemos sobre el puerto `8080`, ejecutamos:
```sh
$ kubectl port-forward prometheus-deployment-7c878596ff-vqgkl --address 0.0.0.0 8080:9090 -n monitoring
```
Donde `prometheus-deployment-7c878596ff-vqgkl` es el nombre del Pod que nos mostró el anterior comando.

Si todo ha ido correctamente, deberíamos ver la interfaz de Prometheus a través de nuesto navegador.

## Install Prometheus y Grafana
Hay una forma más sencilla de instalar ambos al mismo tiempo, para ello, ejecutamos:
```sh
$ kubectl apply --filename https://raw.githubusercontent.com/giantswarm/kubernetes-prometheus/master/manifests-all.yaml
```
Este archivo ya viene todo configurado para ser lanzado en Kubernetes, acceder al github de su propietario para más información:
- https://github.com/giantswarm/prometheus

Al igual que en la instalación por separado de Prometheus, hicimos un `port-forward`, asi que de la misma manera, ejecutamos los siguientes comandos:
```sh
$ export POD_NAME=$(kubectl get pods --namespace monitoring -l "app=grafana,component=core" -o jsonpath="{.items[0].metadata.name}")
$ kubectl port-forward $POD_NAME --address 0.0.0.0 9010:3000 -n monitoring
```

Y a través del puerto `9010`, podremos acceder a la interfáz de Grafana.