package co.edu.escuelaing.arem;

import static spark.Spark.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.function.Consumer;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.*;

public class SparkWebServer {

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    
    public static void main(String[] args){
          port(getPort());
          post("/",(req, res)->{
            res.type("application/json");
            return create(req.queryParams("value"));
        });
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

    private static String create(String a){
        MongoClient mongoClient = new MongoClient("db");
        MongoDatabase db = mongoClient.getDatabase("Lista");
        MongoCollection<Document> collection = db.getCollection("datos");
        if(collection.countDocuments()==10){
            collection.deleteOne(Filters.eq("id",0));
            Document updated = new Document().append("$inc", new Document().append("id", -1));
            collection.updateMany(Filters.gt("id",0),updated);
        }
        collection.insertOne(new Document().append("fecha",formatter.format(new Date())).append("value", a).append("id",(int)collection.countDocuments()));
        ArrayList<String> res = new ArrayList<>();
        collection.find().forEach((Consumer<Document>) (Document d) -> { d.remove("_id");d.remove("id");res.add(d.toJson());});
        mongoClient.close();
        return Arrays.toString(res.toArray(new String[res.size()]));
    }

}