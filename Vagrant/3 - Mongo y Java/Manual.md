# MongoDB y Java
Para comenzar con la práctica necesitamos tener instalado
- VirtualBox
- Vagrant
- mongo-java-driver-3.9.1.jar

Levantamos Vagrant mediante el comando 
> vagrant up

Sobre el fichero Vagrantfile, el cual tiene configuradas 2 máquinas, una que la llamaremos `JavaVM` la cual tendrá el programa llamado `People.java` que al igual que en la práctica Hello-World, lo que hace es pedir por unos datos sobre el usuario, pero esta vez en lugar de guardar la información en un fichero, la guardara en la siguiente máquina virtual en una base de datos de MongoDB, esta máquina tiene la IP Estática `192.168.50.4`.

La siguiente máquina tiene el nombre de `mongoVM`, tiene asignada la IP estática `192.168.50.5`, y al ser creada, instala automáticamente MongoDB.
(Para que podamos acceder a la base de datos desde cualquier IP, cambiaremos en el fichero `/etc/mongo.conf` el contenido de bindIP, por `0.0.0.0`)

Ambas máquinas tendran una variable de entorno que se crea al crearlas por primera vez, llamada CLASSPATH, y contiene el JAR que necesitamos para la comunicación de Java y MongoDB. (`mongo-java-driver-3.9.1.jar`)

En la máquina `JavaVM` compilaremos el programa `People.java` mediante los siguientes comandos:

> javac People.java
java People

Y posteriormente lo ejecutamos con el comando:
> java People

Pedira datos sobre el usuario que se quiere guardar, y si todo ha ido correctamente, deberemos ver que en la base de datos se han añadido los datos correctamente.

Para leer estos datos almacenados, realizaremos el mismo proceso de compilación y ejecutado del programa anterior pero esta vez sobre `ReadPeople.java`

## Referencias
- https://www.vagrantup.com/docs/provisioning/shell.html#inline-scripts
- https://docs.mongodb.com/manual/tutorial/install-mongodb-on-ubuntu/
- https://www.quackit.com/mongodb/tutorial/mongodb_create_a_database.cfm
- https://repo1.maven.org/maven2/org/mongodb/mongo-java-driver/3.9.1/
- https://www.mkyong.com/mongodb/java-mongodb-insert-a-document/