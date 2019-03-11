# Hello-World Java
Para comenzar con la práctica necesitamos tener instalado
- VirtualBox
- Vagrant

Levantamos Vagrant mediante el comando 
> vagrant up

Sobre el fichero Vagrantfile, el cual ya viene configurado, y trabaja sobre ubuntu 16.04 (Xenial), al crear la máquina se realiza un update y se instala java sobre ella.

Posteriormente compilamos el archivo `People.java` mediante el comando:
> javac People.java

Y posteriormente lo ejecutamos con el comando:
> java People

Pedira datos sobre el usuario que se quiere guardar, y estos se guardaran en un documento de texto sobre el mismo directorio llamado `userdata.txt`

Para leer estos datos almacenados, realizaremos el mismo proceso de compilación y ejecutado del programa anterior pero esta vez sobre `ReadPeople.java`