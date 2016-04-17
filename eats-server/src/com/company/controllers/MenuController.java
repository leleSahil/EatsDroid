package com.company.controllers;

import com.company.models.DatabaseSingleton;
import com.company.server.JsonRequestHandlerInterface;
import com.company.server.NotFoundRequestHandler;
import com.company.server.ResponseTuple;
import com.company.server.Serializer;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.sun.net.httpserver.HttpExchange;
import org.bson.Document;

import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * Created by brian on 4/15/16.
 */
public class MenuController implements JsonRequestHandlerInterface {

    @Override
    public ResponseTuple handleLogic(HttpExchange t) throws IOException {
        URI uri = t.getRequestURI();
        String[] pathElements = uri.getPath().split("/");
        // "", "restaurants"
        if(uri.getQuery().startsWith("date=")){
            String dateString = uri.getQuery().replaceFirst("date=", "");
            System.out.println(dateString);
            String[] dateElements = dateString.split("-");
            if(dateElements.length != 3){
                return NotFoundRequestHandler.throw404(t);
            }

            Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"), Locale.US);
            c.set(Calendar.YEAR, Integer.parseInt(dateElements[0])); // regular year
            c.set(Calendar.MONTH, Integer.parseInt(dateElements[1])-1); // from 0 - 11
            c.set(Calendar.DATE, Integer.parseInt(dateElements[2]));  // from 1 - 31..
            c.set(Calendar.HOUR, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);

            System.out.println(c.getTime());
            return new ResponseTuple(200, getMenus(c.getTime()));
        }
        return NotFoundRequestHandler.throw404(t);

    }
    public String getMenus(Date date){
        MongoCollection<Document> collection = DatabaseSingleton.getInstance().database.getCollection("menus");

        BasicDBObject query = new BasicDBObject();
        query.put("date", new BasicDBObject("$eq", date)); // Just get menus that equal this date

        FindIterable<Document> documents = collection.find(query);
        ArrayList<Document> castedFavorites = new ArrayList<Document>();
        for(Document doc : documents){
            System.out.println("Adding document: "+doc.toString());
            castedFavorites.add(doc);
        }
        ArrayList<String> renderedDocuments = new ArrayList<String>();
        for (Document favorite : castedFavorites) {
            renderedDocuments.add(new Serializer(favorite).serialize());
        }
        String returnValue = "[" + String.join(", ", renderedDocuments) + "]";
        System.out.println(returnValue);
        return returnValue;
    }

}

