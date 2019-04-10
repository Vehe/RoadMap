# Roles

A medida que vamos añadiendo funcionalidad y complejidad a nuestros playbooks, cada vez se hace más difícil manejarlo con un solo fichero. Los roles, nos permiten crear un playbook con una mínima configuración y definir toda la complejidad y lógica de las acciones a más bajo nivel.

Para la correcta utilización de los roles, es necesario crear toda una estructura de carpetas y subcarpetas donde iremos depositando nuestra configuración. Tenemos dos opciones para crear estas carpetas, de forma manual o utilizando ansible-galaxy. Ansible-galaxy es un sitio para la búsqueda, la reutilización y el intercambio de roles desarrollados por la comunidad.

The concept of an Ansible role is simple; it is a group of variables, tasks, files, and handlers that are stored in a standardized file structure.

## Crear un nuevo role

Nos situaremos en el directorio `/etc/ansible/roles/`, y ejecutaremos el siguiente comando:
```sh
$ ansible-galaxy init webdb
```

Esto creará un nuevo role `webdb`, con la siguiente estructura de directorios:
```
.
└── webdb
    ├── defaults
    │   └── main.yml
    ├── files
    ├── handlers
    │   └── main.yml
    ├── meta
    │   └── main.yml
    ├── README.md
    ├── tasks
    │   └── main.yml
    ├── templates
    ├── tests
    │   ├── inventory
    │   └── test.yml
    └── vars
        └── main.yml
```

Editamos el dichero `/tasks/main.yaml`, y añadimos el siguiente contenido:
```
---
# tasks file for webdb

# Instalamos Apache2
- yum: name=httpd state=present

# Guardamos la repo de MySQL
- yum: name=http://dev.mysql.com/get/mysql57-community-release-el7-9.noarch.rpm state=present

# Instalamos MySQL 5.7
- yum: name=mysql-server state=present

# Iniciamos el servicio de MySQL
- service: name=mysqld state=started

# Copiamos el index personalizado al directorio web de apache
- template: src=index.html dest=/var/www/html/index.html

# Iniciamos apache
- service: name=httpd state=started
```

Básicamente es una ejecución de todos los ejercicios anteriores, pero agrupados en un unico role y ejecución.

Movemos nuestro archivo `index.html` personalizado a la carpeta de `templates`, ya que la task, buscara este fichero en ese directorio.

Ahora, para ejecutar este role, creamos sobre el directorio `/etc/ansible/roles/` el fichero `webdb.yaml` con el siguiente contenido:
```
---
- hosts: openstack
  roles:
    - /etc/ansible/roles/webdb
```

Donde le estamos indicando que queremos ejecutar dicho role, sobre las máquinas agrupadas con el nombre de openstack en el fichero `/etc/ansible/hosts`.

Si todo ha ido bién, no deberíamos ver ningún error.