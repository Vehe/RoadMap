# Grafana Dashboard

Realizaremos una pequeña practica para visualizar la cantidad de paquetes que envía `nginx` y visualizaremos la información gráficamente con `Grafana`, obteniendo los datos desde `Prometheus`.

Para ello, tendremos que tener en funcionamiento tanto `Prometheus` como `Grafana`, y un `nginx` con dos replicas.

## Inicio

Tenemos dos replicas de `nginx`, las IP de los pods y los identificadores son los siguientes:
- 10.38.0.2 -> nginx-deployment-7c9f99b6f9-k58hr
- 10.32.0.3 -> nginx-deployment-7c9f99b6f9-5rkk9

Para generar un poco de tráfico en el pod con IP `10.38.0.2`, ejecutaremos el siguiente comando:
```sh
$ for i in {1..20}; do curl 10.38.0.2; done
```

Lo mismo con el pod con IP `10.32.0.3`, pero distinta cantidad, para que sean métricas diferentes:
```sh
$ for i in {1..30}; do curl 10.32.0.3; done
```

Si ahora vamos a la interfaz de `Prometheus`, a la sección de `Graph`, y ejecutamos la siguiente Query:
```
container_network_transmit_packets_total{}
```

Probablemente nos salga una lista enorme, y no podamos ver los pod que queremos, para ello, usaremos el shortcut `Ctrl + F` en el navegador para buscar por palabras, y escribimos `nginx`.
Deberíamos ver marcados ahora si los Pod que queremos.

Copiamos el tag `name` de cada uno de los pod que estamos visualizando en mi caso corresponde a lo siguiente:
- 10.38.0.2 -> name="k8s_POD_nginx-deployment-7c9f99b6f9-k58hr_default_0ed0babf-512f-11e9-91d9-fa163e63860f_0"
- 10.32.0.3 -> name="k8s_POD_nginx-deployment-7c9f99b6f9-5rkk9_default_0ecf976b-512f-11e9-91d9-fa163e63860f_0"

Ahora que tenemos el tag que queremos que identifica unicamente los Pod, podemos ir a `Grafana` a ejecutar esta query y ver gráficamente la información.

Vamos a `Grafana -> Dashboards -> Kubernetes Pod Resources -> Network` y damos clic y pulsamos `Edit`.

Ocultamos las Querys anteriores y creamos unas nuevas, con los siguientes datos:

- A -> container_network_transmit_packets_total{name="k8s_POD_nginx-deployment-7c9f99b6f9-5rkk9_default_0ecf976b-512f-11e9-91d9-fa163e63860f_0"}
- B -> container_network_transmit_packets_total{name="k8s_POD_nginx-deployment-7c9f99b6f9-k58hr_default_0ed0babf-512f-11e9-91d9-fa163e63860f_0"}

Y ya podremos ver gráficamente los paquetes transmitidos por los Pods de `nginx`.