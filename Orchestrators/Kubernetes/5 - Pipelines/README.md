# Pipelines

Jenkins Pipeline (or simply "Pipeline" with a capital "P") is a suite of plugins which supports implementing and integrating continuous delivery pipelines into Jenkins.

A continuous delivery (CD) pipeline is an automated expression of your process for getting software from version control right through to your users and customers. Every change to your software (committed in source control) goes through a complex process on its way to being released. This process involves building the software in a reliable and repeatable manner, as well as progressing the built software (called a "build") through multiple stages of testing and deployment.

## Ejemplo pipeline

```
pipeline {
    agent { docker { image 'node:6.3' } }
    stages {
        stage('build') {
            steps {
                sh 'npm --version'
            }
        }
    }
}
```

## Input del usuario
Como el nombre sugiere, esto permite al usuario parar la ejecución de la pipeline esperando la respuesta del usaurio:
```
input 'Continuar con la siguiente parte?'
```
Mostrará al usuario dos opciones, `Proceed` la cual nos permitirá continuar con la siguiente parte, o `Abort`, la cual acabará con la ejecución de la pipeline con el estado `aborted`.

Podemos asignar un identificador único a este input del usuario, en caso de que lo necesitasemos para otra cosa, para ello, podemos hacer algo como lo siguiente:
```
input id: 'ctns-prompt', message: 'Continuar con la siguiente parte?'
```

Si el lugar de que para continuar nos ponga un botón con `Proceed` queremos que salga algo personalizado, podemos hacerlo a través de:
```
input message: '<message text>', ok: 'Continuar'
```

## Resources
- https://jenkins.io/doc/pipeline/tour/hello-world/