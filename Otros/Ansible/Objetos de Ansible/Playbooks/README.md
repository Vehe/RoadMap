# Playbooks

[Official DOC](https://docs.ansible.com/ansible/latest/user_guide/playbooks.html)

Los Playbooks básicamente nos permiten gestionar la configuración del despliegue que vamos a realizar en los nodos. En ellos describimos la configuración, pero, también nos permiten orquestar una serie de pasos o tareas a seguir. El Playbook se escribe utilizando YAML.

Ejemplo de un `playbook.yaml`:

```
---
- hosts: webservers
  vars:
    http_port: 80
    max_clients: 200
  remote_user: root
  tasks:
  - name: ensure apache is at the latest version
    yum:
      name: httpd
      state: latest
  - name: write the apache config file
    template:
      src: /srv/httpd.j2
      dest: /etc/httpd.conf
    notify:
    - restart apache
  - name: ensure apache is running
    service:
      name: httpd
      state: started
  handlers:
    - name: restart apache
      service:
        name: httpd
        state: restarted
```

Para ejecutar un playbook, ejecutaremos el siguiente comando:
```sh
$ ansible-playbook test.yaml
```