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

class ReadPeople 
{
	public static void main( String[] args ) 
	{
		int puerto = 9001;

		try {

			ServerSocket socket = new ServerSocket(puerto);
			System.out.println( "INICIANDO EN PUERTO 9001, BIND EN 81 LOCALHOST! ..." );

			while(true) {

				Socket connected = socket.accept();
				PrintWriter salida = new PrintWriter(connected.getOutputStream(), true);
				System.out.println("NUEVO CLIENTE: " + connected.getInetAddress() + ":" + connected.getPort());

				Mongo mongo = new Mongo("db", 27017);

				DB db = mongo.getDB("roadmap");
				DBCollection collection = db.getCollection("usuarios");

				DBCursor cursor = collection.find();
				while (cursor.hasNext()) {
					DBObject obj = cursor.next();
					salida.println(obj);
				}

				salida.close();
				connected.close();

			}

		} catch(IOException e) {

			System.out.print("Se ha producido un error!");

		}	
		
	}

}