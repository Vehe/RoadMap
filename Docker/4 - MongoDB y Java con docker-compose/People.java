import java.net.*;
import java.io.*;
import java.util.*;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

class People 
{
	public static void main( String[] args ) 
	{
		int puerto = 9000;

		try {

			ServerSocket socket = new ServerSocket(puerto);
			System.out.println( "INICIANDO EN PUERTO 9000, BIND EN 80 LOCALHOST! ..." );

			while(true) {

				Socket connected = socket.accept();

				BufferedReader entrada = new BufferedReader(new InputStreamReader(connected.getInputStream()));
				PrintWriter salida = new PrintWriter(connected.getOutputStream(), true);

				System.out.println("NUEVO CLIENTE: " + connected.getInetAddress() + ":" + connected.getPort());

				salida.println("");
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

				if(usrSexo.equals("!exit")) { break; }

				Mongo mongo = new Mongo("db", 27017);

				DB db = mongo.getDB("roadmap");
				DBCollection col = db.getCollection("usuarios");

				BasicDBObject documentDetail = new BasicDBObject();
				documentDetail.put("nombre", usrNom);
				documentDetail.put("apellido", usrApe);
				documentDetail.put("edad", usrEdad);
				documentDetail.put("sexo", usrSexo);
				col.insert(documentDetail);

				entrada.close();
				salida.close();
				connected.close();

			}

			socket.close();

		} catch(IOException e) {

			System.out.print("Se ha producido un error!");

		}

	}

}