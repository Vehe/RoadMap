# Cluster con Kubernetes

## Kubernetes vs Docker Swarm
Para elegir uno de los dos, tenemos que plantearnos primero que tipo de proyecto queremos desarrollar, ya que cada uno tiene sus puntos débiles y los fuertes.

Docker Swarm está más orientado para entornos simples, que necesitan una puesta en funcionamiento rápida, este también es más sencillo de utilizar, mientras que Kubernetes se orienta hacia entornos con clusters de tamaño medio/grande y aplicaciónes más complejas.

Para iniciar un cluster de Kubernetes primero necesitamos los siguientes requisitos:
- kubeadm: Instrucción que nos permite crear el cluster.
- kubelet: Es el componente de kubernetes que se ejecuta en todos los nodos y es responsable de ejecutar los pods y los contenedores.
- kubectl: La utilidad de línea de comandos que nos permite controlar el cluster.

## Crear cluster con Kubernetes
Vamos a instalarlo en una máquina con SO `CentOS Linux release 7.4.1708 (Core)`.
Ejecutaremos los siguientes comandos, los cuales estan sacados directamente de la página de Kubernetes (https://kubernetes.io/docs/setup/independent/install-kubeadm/).
```sh
$ cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://packages.cloud.google.com/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://packages.cloud.google.com/yum/doc/yum-key.gpg https://packages.cloud.google.com/yum/doc/rpm-package-key.gpg
exclude=kube*
EOF

$ setenforce 0
$ sed -i 's/^SELINUX=enforcing$/SELINUX=permissive/' /etc/selinux/config
$ yum install -y kubelet kubeadm kubectl --disableexcludes=kubernetes
$ systemctl enable --now kubelet
```
Ahora tendremos que instalar Docker, si no lo tenemos instalado aún, este tiene que estar en una version igual o inferior a la 18.06.0, ya que sino dará problemas de incompatibilidad.

Instalamos Docker en una versión específica de la siguiente forma:
```sh
$ sudo yum install docker-ce-18.06.0.ce-3.el7 docker-ce-cli-18.06.0.ce-3.el7 containerd.io
$ sudo systemctl start docker
```
Tenemos que activar el IPv4 Forward, para ello ejecutamos el siguiente comando:
```sh
$ sudo echo 1 > /proc/sys/net/ipv4/ip_forward
```

Con todo listo, solo nos queda iniciar el cluster con el siguiente comando:
```sh
$ kubeadm init 
```

Se nos mostrarán unas instrucciones por pantalla para usar el cluster desde un usuario corriente, ejecutamos los comandos siguientes desde el usuario regular.
```
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

En las mismas instrucciones que se nos proporcionan al crear el cluster, se nos proporciona un token de acceso para unir los nodos, simplemente lo ejecutamos.
```sh
$ kubeadm join 192.168.1.14:6443 --token [token] --discovery-token-ca-cert-hash [hash]
```

En este momento, si desde el nodo Leader, miramos los nodos que componen el cluster con el comando:
```sh
$ kubectl get nodes
```

Podremos apreciar que el estado de todos los nodos es `Not Ready`, esto es porque primero debemos añadir un `add-on` para la red, en mi caso, usare `Weave Net`, pero hay una amplia gama de add-ons que podemos usar.
La función de este es comunicar todos los Pod del cluster.

Para instalarlo, ejecutamos los siguientes comandos:
```sh
$ sudo sysctl net.bridge.bridge-nf-call-iptables=1
$ kubectl apply -f "https://cloud.weave.works/k8s/net?k8s-version=$(kubectl version | base64 | tr -d '\n')"
```

Para comprobar que se ha instalado con exito y esta funcionando, miramos que los Pod que ha creado el add-on, esten funcionando correctamente:
```sh
$ kubectl get pods --all-namespaces
```

Ahora todos los nodos deberian tener el status en `Ready`.