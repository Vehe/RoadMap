FROM ubuntu:xenial

# Actualizamos e instalamos Java.
RUN apt-get update -y && apt-get install -y \
	default-jre \
	default-jdk

# Copiamos los archivos Java y el mongo driver a /app
COPY . /app

# Establecemos las variables de entorno
ENV CLASSPATH .:/app/mongo-java-driver-3.9.1.jar

# Establecemos el Workdir y compilamos y ejecutamos el programa.
WORKDIR /app
RUN javac People.java
CMD java People