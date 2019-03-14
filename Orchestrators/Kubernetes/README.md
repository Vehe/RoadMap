# Kubernetes

Kubernetes is a portable, extensible open-source platform for managing containerized workloads and services, that facilitates both declarative configuration and automation.

Kubernetes provides a container-centric management environment. It orchestrates computing, networking, and storage infrastructure on behalf of user workloads. This provides much of the simplicity of Platform as a Service (PaaS) with the flexibility of Infrastructure as a Service (IaaS), and enables portability across infrastructure providers.

## Namespaces
You can think of namespace as a virtual cluster inside of you Kubernetes cluster. You can have multiples namespaces inside of a single Kubernetes cluster and they are isolated from each other. They can help your team with organisation, security and performance. Pods run inside of namespaces.

Namespaces isolates pods to separate workloads from each other and it gives you the capability of setting up resource constrainsts on it. It could fit with environment segregation.
![Namespaces](https://www.katacoda.com/contino/courses/kubernetes/pods/assets/namespaces.png)

### Namespaces por defecto de Kubernetes
- kube-system: Los pods de este namespace se usan para el funcionamiento de kubernetes, contienen controllers, add-ons …
- kube-public: Contiene un ConfigMap que contiene bootstraping y certificados sobre el cluster de Kubernetes.
- Default: Este contiene todos los objetos que se crean sin especificar un namespace, este no puede ser borrado.

Creamos un nuevo namespace:
```sh
$ kubectl create namespace dev-service1
```

Crear un namespace a partir de un YAML file:
```
apiVersion: v1
kind: Namespace
metadata:
  name: test
```
```sh
$ kubectl apply -f test-namespace.yaml
```

Para borrar un namespace tenemos dos formas de hacerlo:
```sh
$ kubectl delete -f test-namespace.yaml
$ kubectl delete namespace test
```

## Labels
Label is a metadata to identify information. It can be used for queries. It's good practice to use labels as much as possible, as you'll be able to have more granular control over what your pods are doing.

Le añade al `node01` el label `disk=ssh`:
```sh
$ kubectl label nodes node01 disk=ssd
```

Borra el label `disk` del node01.
```sh 
$ kubectl label node node01 disk-
```


## Pods
A Pod is a group of one or more application containers (such as Docker or rkt) and includes shared storage (volumes), IP address and information about how to run them.
Containers should only be scheduled together in a single Pod if they are tightly coupled and need to share resources such as disk.

![Pods](https://d33wubrfki0l68.cloudfront.net/fe03f68d8ede9815184852ca2a4fd30325e5d15a/98064/docs/tutorials/kubernetes-basics/public/images/module_03_pods.svg)

### Creando un pod mediante YAML file
Para crear un Pod desde un fichero, lo que tenemos que hacer es escirbir lo siguiente en un fichero:
```
apiVersion: v1
kind: Pod
metadata:
  name: happypanda
spec:
  containers:
  - name: nginx
    image: nginx
```
Cuendo este este escrito, guardamos y ejecutamos el siguiente comando (donde `pod.yaml` es el nombre del fichero):
```sh
$ kubectl apply -f pod.yaml
```
Borrar un Pod es igual de sencillo, tenemos dos formas de hacerlo:
```sh
$ kubectl delete -f pod.yaml
$ kubectl delete pod happypanda
```
Podemos crear un Pod en un determinado namespace, solo debemos añadir la siguiente línea en el apartado `spec:` del YAML file:
```
namespace: dev-service1
```
Mostramos los Pod que estan en un determinado nameserver:
```sh
$ kubectl get pods -n dev-service1
```
Podemos realizar modificaciones sobre las especificaciones del Pod modificando el YAML file, por ejemplo, cambiemos el anterior contendio por el siguiente:
```
apiVersion: v1
kind: Pod
metadata:
  name: happypanda
  namespace: dev-service1
  labels: 
    app: redis
    segment: backend
    company: mycompany    
spec:
  containers:
  - name: redis
    image: redis:4.0.10
    ports:
    - name: redisport
      containerPort: 6379
      protocol: TCP
```

Si volvemos a ejecutar el comando `kubectl apply -f pod.yaml` actualizaremos el Pod con los nuevos datos.
En este caso en concreto, dará un error, ya que anteriormente el Pod creaba un contenedor con una imagen de `nginx`, y actualmente lo hace con una de `redis`, y esto no es posible, ha ciertos apartados que no pueden ser modificados.
(Esto lo solucionaremos posteriormente mediante deployments)

Para que esto entrara en funcionamiento, deberiamos borrar el Pod y crearlo de nuevo con este nuevo fichero.

Muestra información sobre el Pod `happypanda` que se enecuentra en el namespace `dev-service1`.
```sh
$ kubectl describe pod happypanda --namespace dev-service1
```

Tenemos la opción de especificar un requisito sobre el nodo donde sera lanzado nuestro Pod, por ejemplo, queremos que este Pod solo funcione en nodos que tengan SSD, por lo que a estos, les añadimos el label `disk=ssd`, y en el archivo de creación del Pod (`pod.yaml`), insertamos las siguientes líneas dentro de `spec:`:
```
nodeSelector:
    disk: ssd
```
De tal manera que al lanzar el Pod, este buscara los nodos con el label `disk=ssh` para funcionar.

## Nodes
A Pod always runs on a Node. A Node is a worker machine in Kubernetes and may be either a virtual or a physical machine, depending on the cluster. Each Node is managed by the Master. A Node can have multiple pods, and the Kubernetes master automatically handles scheduling the pods across the Nodes in the cluster. The Master's automatic scheduling takes into account the available resources on each Node.

Every Kubernetes Node runs at least:
- Kubelet, a process responsible for communication between the Kubernetes Master and the Node; it manages the Pods and the containers running on a machine.
 - A container runtime (like Docker, rkt) responsible for pulling the container image from a registry, unpacking the container, and running the application.
![Nodes](https://d33wubrfki0l68.cloudfront.net/5cb72d407cbe2755e581b6de757e0d81760d5b86/a9df9/docs/tutorials/kubernetes-basics/public/images/module_03_nodes.svg)

Muestra los nodos que tiene el cluster:
```sh
$ kubectl get nodes
```
Si le añadimos la flag `--show-labels`, a parte de la información nos muestra los label que tiene ese nodo.

## Jobs
A Job creates one or more Pods and ensures that a specified number of them successfully terminate. As pods successfully complete, the Job tracks the successful completions. When a specified number of successful completions is reached, the task (ie, Job) is complete. Deleting a Job will clean up the Pods it created.

Hay dos tipos de Jobs:

- Non-parallel Job: A Job which creates only one Pod (which is re-created if the Pod terminates unsuccessfully), and which is completed when the Pod terminates successfully.

- Parallel jobs with a completion count: A Job that is completed when a certain number of Pods terminate successfully. You specify the desired number of completions using the completions field.


