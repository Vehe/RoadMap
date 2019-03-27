# Upgrade Kubernetes Cluster v1.13.4 to v1.14.0

Upgrade del cluster de kubernetes de la versión 1.13.4 de kubeadm a la 1.14.0 (Released March 25, 2019).
Realizado sobre plataforma `OpenStack` con máquinas `CentOS 7`.

## Upgrade

Comprobamos la ultima versión estable de `Kubeadm`:
```sh
$ yum list --showduplicates kubeadm --disableexcludes=kubernetes
```

Hacemos un upgrade a `Kubeadm` a al versión deseada, en este caso `v1.14.0`:
```sh
$ yum install -y kubeadm-1.14.0-0 --disableexcludes=kubernetes
```

Comprobamos la versión actual de `kubeadm` para ver si se ha realizado correctamente:
```sh
$ kubeadm version
```

El siguiente comando comprueba que podemos hacer el upgrade a nuestro cluster:
```sh
$ kubeadm upgrade plan
```

Si vemos que nos lo permite, aplicamos la versión que queremos:
```sh
$ kubeadm upgrade apply v1.14.0
```

Actualizamos `kubelet` a la versión `1.14.0`:
```sh
$ yum install -y kubelet-1.14.0-0 --disableexcludes=kubernetes
```

Ejecutamos el siguiente comando en todos los nodos del cluster para actualizar `kubectl`:
```sh
$ yum install -y kubectl-1.14.0-0 --disableexcludes=kubernetes
```

Marcamos todos los nodos del cluster en mantenimiento:
```sh
$ kubectl drain $NODE --ignore-daemonsets
```

Actualizamos `kubelet config` en todos los nodos del cluster:
```sh
$ kubeadm upgrade node config --kubelet-version v1.14.0
```

Actualizamos `kubelet` y `kubeadm` en todos los nodos del cluster:
```sh
$ yum install -y kubelet-1.14.0-0 kubeadm-1.14.0-0 --disableexcludes=kubernetes
```

Reiniciamos en todos los nodos `kubelet` y comprobamos que se levanta en estado `activo`:
```sh
$ systemctl restart kubelet
$ systemctl status kubelet
```

Levantamos el estado de mantenimiento de todos los nodos del cluster:
```sh
$ kubectl uncordon $NODE
```

Y para finalizar comprobamos que todos los nodos tienen estatus de `Ready` y que sus versiones coinciden con `1.14.0`.
```sh
$ kubectl get nodes
```