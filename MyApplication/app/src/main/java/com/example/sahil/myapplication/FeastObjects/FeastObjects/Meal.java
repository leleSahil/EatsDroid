package com.example.sahil.myapplication.FeastObjects.FeastObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by adityaaggarwal on 3/15/16.
 */
public class Meal implements Serializable {
    public String name;
    public ArrayList<Section> sections=new ArrayList<>();
    public Meal(JSONObject jsonObject){
        try {
            name = jsonObject.getString("meal_name");
            JSONArray meal_sectionsJSON= jsonObject.getJSONArray("meal_sections");

            for(int i=0; i<meal_sectionsJSON.length(); i++){
                sections.add(new Section(meal_sectionsJSON.getJSONObject(i)));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public Meal(int choice) {
        sections = new ArrayList<Section>();

        if(choice == 0) {
            name = "Breakfast";
        } else if(choice == 1) {
            name = "Lunch";
        } else if(choice == 2) {
            name = "Dinner";
        } else {
            name = "Hocus";
        }
        sections.add(new Section());
        sections.add(new Section());
    }

    public ArrayList<Section> getSections(){
        return sections;
    }
    public String getName(){
        return name;
    }
}
