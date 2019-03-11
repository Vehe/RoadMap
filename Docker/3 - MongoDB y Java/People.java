import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.util.Scanner;

public class People
{

    public static void main(String[] args)
    {
        Scanner kb = new Scanner(System.in);

        System.out.println();
        System.out.println("Introduce los siguientes datos sobre un nuevo usuario ->");
        System.out.println();


        System.out.print("Nombre: ");
        String usrNom = kb.nextLine();

        System.out.print("Apellido 1: ");
        String usrApe = kb.nextLine();

        System.out.print("Edad: ");
        String usrEdad = kb.nextLine();

        System.out.print("Sexo (H/M): ");
        String usrSexo = kb.nextLine();

        Mongo mongo = new Mongo("172.18.0.3", 27017);

        DB db = mongo.getDB("roadmap");
        DBCollection col = db.getCollection("usuarios");

        BasicDBObject documentDetail = new BasicDBObject();
        documentDetail.put("nombre", usrNom);
        documentDetail.put("apellido", usrApe);
        documentDetail.put("edad", usrEdad);
        documentDetail.put("sexo", usrSexo);
        col.insert(documentDetail);

        System.out.println();

    }

}