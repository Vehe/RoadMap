# Ingress

[Documentación de Kubernetes](https://kubernetes.io/docs/concepts/services-networking/ingress/)

Hasta ahora tenemos dos opciones principales para acceder a nuestras aplicaciones desde el exterior:

- Utilizando servicios del tipo NodePort: Esta opción no es muy viable para entornos de producción ya que tenemos que utilizar puertos aleatorios desde 30000-40000.
- Utilizando servicios del tipo LoadBalancer: Esta opción sólo es válida si trabajamos en un proveedor Cloud que nos cree un balanceador de carga para cada una de las aplicaciones, en cloud público puede ser una opción muy cara.

La solución puede ser utilizar un Ingress controller que nos permite utilizar un proxy inverso (HAproxy, nginx, traefik,…) que por medio de reglas de encaminamiento que obtiene de la API de Kubernetes nos permite el acceso a nuestras aplicaciones por medio de nombres.

![Ingress](https://www.josedomingo.org/pledin/assets/wp-content/uploads/2018/12/ingress.png)

- Vamos a desplegar una pod que implementa un proxy inverso (podemos utilizar varias opciones: nginx, HAproxy, traefik,…) que esperará peticiones HTTP y HTTPS.
- Por lo tanto el Ingress Controller será el nodo del cluster donde se ha instalado el pod. En nuestro caso, para realizar el despliegue vamos utilizar un recurso de Kubernetes llamado DaemontSet que asegura que el despliegue se hace en todos los nodos del cluster y que los puertos que expone los pods (80 y 443) están mapeado y son accesible en los nodos. Por lo que cada uno de los nodos del cluster va a poseer una copia del Ingress Controller.
- No es necesario que al servicio al que accede el Ingress Controller este configurado del tipo NodePort.

## Kubernetes Doc
In Kubernetes, an Ingress is an object that allows access to your Kubernetes services from outside the Kubernetes cluster. You configure access by creating a collection of rules that define which inbound connections reach which services.

This lets you consolidate your routing rules into a single resource. For example, you might want to send requests to example.com/api/v1/ to an api-v1 service, and requests to example.com/api/v2/ to the api-v2 service. With an Ingress, you can easily set this up without creating a bunch of LoadBalancers or exposing each service on the Node.

NodePort and LoadBalancer let you expose a service by specifying that value in the service’s type. Ingress, on the other hand, is a completely independent resource to your service. You declare, create and destroy it separately to your services.

This makes it decoupled and isolated from the services you want to expose. It also helps you to consolidate routing rules into one place.

![Ingress Diagram](https://matthewpalmer.net/kubernetes-app-developer/articles/ingress.png)