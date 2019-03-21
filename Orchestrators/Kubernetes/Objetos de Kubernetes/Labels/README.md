# Labels

Las Labels nos permiten etiquetar los recursos de kubernetes (por ejemplo un pod) con información del tipo clave/valor.

Para obtener las labels de los pods que hemos creado:

```
$ kubectl get pods --show-labels
NAME      READY     STATUS    RESTARTS   AGE       LABELS
nginx     1/1       Running   0          10m       app=nginx
```
Los Labels lo hemos definido en la sección metada del fichero yaml, pero también podemos añadirlos a los pods ya creados:

```
$ kubectl label pods nginx service=web
pod "nginx" labeled

$ kubectl get pods --show-labels
NAME      READY     STATUS    RESTARTS   AGE       LABELS
nginx     1/1       Running   0          12m       app=nginx,service=web
```
Los Labels me van a permitir seleccionar un recurso determinado, por ejemplo para visualizar los pods que tienen un label con un determinado valor:

```
$ kubectl get pods -l service=web
NAME      READY     STATUS    RESTARTS   AGE
nginx     1/1       Running   0          13m
```
También podemos visualizar los valores de los labels como una nueva columna:

```
$ kubectl get pods -Lservice
NAME      READY     STATUS    RESTARTS   AGE       SERVICE
nginx     1/1       Running   0          15m       web
```

Para borrar un label:

```
$ kubectl label pods nginx service-
```