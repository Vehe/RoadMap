# MongoDB
Iniciar el servicio MongoDB
>sudo service mongod start

Parar el servicio MongoDB
>sudo service mongod stop

Mostrar bases de datos
>show dbs

Mostrar las collections
>show collections

Cambiar de base de datos
>use test

Mostrar usuarios
>show users

Mostrar el contenido de un determinado collection
>db.usuarios.find()

Busqueda mediante query
>db.usuarios.find({name:"Pablo"})

Número de entradas en collection
>db.usuarios.count()

Combinar varios filtros
>.find().count()

Insertar en un determinado collection
>db.usuarios.insert({titulo:"Prueba", contenido:"Insertar"})

## Referencias
- https://docs.mongodb.com/manual/reference/mongo-shell/