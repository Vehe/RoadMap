# Jenkins en Kubernetes

Para instalar Jenkins en kubernetes solo tenemos que crear el siguiente archivo `jenkins-deployment.yaml`:
```
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: jenkins
spec:
  replicas: 1
  template:
    metadata:
      labels:
        app: jenkins
    spec:
      containers:
        - name: jenkins
          image: jenkins/jenkins:lts
          env:
            - name: JAVA_OPTS
              value: -Djenkins.install.runSetupWizard=false
          ports:
            - name: http-port
              containerPort: 8080
            - name: jnlp-port
              containerPort: 50000
          volumeMounts:
            - name: jenkins-home
              mountPath: /var/jenkins_home
      volumes:
        - name: jenkins-home
          emptyDir: {}
```

y ejecutar:
```sh
$ kubectl create -f jenkins-deployment.yaml
```

y ya tendríamos funcionando jenkins, ahora podemos hacer un servicio en caso de que lo queramos, yo en mi caso, solo hare un `port-forward` sobre el puerto del pod que funciona jenkins a mi máquina de openstack.

```sh
$ kubectl port-forward jenkins-f76bc6fc6-xmr8m --address 0.0.0.0 9000:8080
```

Donde `jenkins-f76bc6fc6-xmr8m` es el nombre del pod.

En este momento ya podríamos acceder a la interfaz gráfica de Jenkins a través del navegador en el puerto `9000`.