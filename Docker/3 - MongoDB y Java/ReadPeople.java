import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class ReadPeople 
{

    public static void main(String[] args) 
    {
        
        Mongo mongo = new Mongo("172.18.0.3", 27017);

        DB db = mongo.getDB("roadmap");
        DBCollection collection = db.getCollection("usuarios");

        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject obj = cursor.next();
            System.out.println(obj);
        }
        
    }

}