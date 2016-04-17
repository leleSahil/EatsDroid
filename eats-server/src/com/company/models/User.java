package com.company.models;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brian on 4/15/16.
 */
@Entity("users")
public class User {

    public class Favorite{
        String food_identifier;
        String food_name;
        public Favorite(){}
    }

    public User(String email){
        this.email = email;
        this.favorites = new ArrayList<>();
    }

    @Id
    public ObjectId _id;

    public String email;
    public List<Favorite> favorites;


    public User(){}

}