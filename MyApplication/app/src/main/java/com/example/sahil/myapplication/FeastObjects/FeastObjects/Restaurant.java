package com.example.sahil.myapplication.FeastObjects.FeastObjects;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by adityaaggarwal on 3/15/16.
 */
public class Restaurant implements Serializable {
    private String name;
    private String id;

    private ArrayList<Menu> menus;

    public Restaurant(JSONObject object){
        try {
            this.name=object.getString("name");
            this.id=object.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Restaurant() {
        menus = new ArrayList<>();

        name = "EVK";
        id = "001";
        menus.add(new Menu());
    }

    public String getName(){
        return name;
    }
    public String getId(){
        return id;
    }

    public ArrayList<Menu> getMenus() {
        return menus;
    }

    public String getID() {
        return id;
    }
}
