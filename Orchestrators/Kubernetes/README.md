# Kubernetes

![Kubernetes logo](https://www.josedomingo.org/pledin/assets/wp-content/uploads/2018/05/name_blue.png)

Kubernetes es un sistema de código abierto que nos permite despliegues automáticos, escabilidad y gestión de contenedores de aplicaiones. kubeadm es una herramienta que nos permite el despliegue de un cluster de kubernetes de manera sencilla. El cluster lo podemos crear en máquinas físicas o virtuales, en nuestro caso, vamos a usar CentOS 7 en 3 máquinas virtuales para realizar la instalación.

## Descripción oficial en la documentación
Kubernetes is a portable, extensible open-source platform for managing containerized workloads and services, that facilitates both declarative configuration and automation.

Kubernetes provides a container-centric management environment. It orchestrates computing, networking, and storage infrastructure on behalf of user workloads. This provides much of the simplicity of Platform as a Service (PaaS) with the flexibility of Infrastructure as a Service (IaaS), and enables portability across infrastructure providers.


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