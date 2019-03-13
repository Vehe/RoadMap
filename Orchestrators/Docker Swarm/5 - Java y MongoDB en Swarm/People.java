import java.net.*;
import java.io.*;
import java.util.*;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

class People 
{
	public static void main( String[] args ) 
	{
		int port = 9000;

		try {

			ServerSocket socket = new ServerSocket(port);
			System.out.println( "INICIANDO EN PUERTO 9000, COMO ESCRITURA EN LA BBDD!" );
			System.out.println( "INICIANDO EN PUERTO 9001, COMO LECTURA EN LA BBDD!" );
			InetAddress inetAddress = InetAddress.getLocalHost();

			while(true) {

				Socket connected = socket.accept();

				BufferedReader entrada = new BufferedReader(new InputStreamReader(connected.getInputStream()));
				PrintWriter salida = new PrintWriter(connected.getOutputStream(), true);

				System.out.println("NUEVO CLIENTE: " + connected.getInetAddress() + ":" + connected.getPort());

				salida.println("IP del servidor: " + inetAddress.getHostAddress());
				salida.println("");
				salida.println("Selecciona -> 1. Guardar Usuario / 2. Leer BBDD ");
				String usrchoice = entrada.readLine();
				salida.println("");

				Mongo mongo = new Mongo("db", 27017);
				DB db = mongo.getDB("roadmap");
				DBCollection col = db.getCollection("usuarios");

				if(usrchoice.equals("1")) {

					salida.println("Introduce los siguientes datos sobre un nuevo usuario ->");
					salida.println("");

					salida.println("Nombre: ");
					String usrNom = entrada.readLine();

					salida.println("Apellido 1: ");
					String usrApe = entrada.readLine();

					salida.println("Edad: ");
					String usrEdad = entrada.readLine();

					salida.println("Sexo (H/M): ");
					String usrSexo = entrada.readLine();

					BasicDBObject documentDetail = new BasicDBObject();
					documentDetail.put("nombre", usrNom);
					documentDetail.put("apellido", usrApe);
					documentDetail.put("edad", usrEdad);
					documentDetail.put("sexo", usrSexo);
					col.insert(documentDetail);
				
				} else if(usrchoice.equals("2")) {

					salida.println("Datos actuales en la BBDD: ");
					salida.println("");
					DBCursor cursor = col.find();
					while (cursor.hasNext()) {
						DBObject obj = cursor.next();
						salida.println(obj);
					}

				} else {

					System.out.println("NO SE HA SELECCIONADO! CERRANDO ...");
					entrada.close();
					salida.close();
					connected.close();

				}

				entrada.close();
				salida.close();
				connected.close();

			}


		} catch(IOException e) {

			System.out.print("Se ha producido un error!");

		}

	}

}
