# Inventario en Ansible

[Official DOC](https://docs.ansible.com/ansible/latest/user_guide/intro_inventory.html)

En este fichero se definen las máquinas a ser gestionadas. Las máquinas pueden ser identificadas por su IP o por su hostname. El inventario principal de ansible es el fichero `/etc/ansible/hosts`, este es un ejemplo de contenido:
```
mail.example.com

[webservers]
foo.example.com
bar.example.com

[dbservers]
one.example.com
two.example.com
three.example.com
```

Los hosts independientes deben estar al comienzo del fichero, antes de cualquier grupo.

Si por ejemplo en alguno de los servidores, no se usa el puerto por defecto de ssh (`22`), podemos indicarlo de la siguiente forma:
```
badwolf.example.com:5309
```

En el caso de tener muchos host identificados de una forma similar, se pueden hacer cosas como esta:
```
[webservers]
www[01:50].example.com

[databases]
db-[a:f].example.com
```

Así como una configuración más avanzada como la siguiente, en la que especificamos como conectarnos y el usuario con el que hacerlo:
```
[targets]
localhost              ansible_connection=local
other1.example.com     ansible_connection=ssh        ansible_user=testuser
other2.example.com     ansible_connection=ssh        ansible_user=testuser
```

También podemos crear una zona para asignar variables a los hosts de un determinado grupo:
```
[atlanta]
host1
host2

[atlanta:vars]
ntp_server=ntp.atlanta.example.com
proxy=proxy.atlanta.example.com
```

Es posible crear grupos más complejos, de la siguiente forma:
```
[atlanta]
host1
host2

[raleigh]
host2
host3

[southeast:children]
atlanta
raleigh

[southeast:vars]
some_server=foo.southeast.example.com
halon_system_timeout=30
self_destruct_countdown=60
escape_pods=2

[usa:children]
southeast
northeast
southwest
northwest
```

Si queremos ejecutar el comando `ansible` sobre otro inventario, solo tenemos que añadir la flag `-i` junto al fichero que queremos, ejemplo:
```sh
$ ansible -i /home/centos/newinv atlanta -m ping
```