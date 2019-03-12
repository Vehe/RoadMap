FROM ubuntu:xenial
RUN apt-get update -y && apt-get install -y \
	default-jre \
	default-jdk
COPY . /app
ENV CLASSPATH .:/app/mongo-java-driver-3.9.1.jar
WORKDIR /app
RUN javac People.java && javac ReadPeople.java
