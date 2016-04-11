package com.example.sahil.myapplication.FeastObjects.FeastObjects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by adityaaggarwal on 3/15/16.
 */
public class Section implements Serializable {
        private String name;
        private ArrayList<FoodItem> foodItems;
        public Section(JSONObject jsonObject){
            try {
                name = jsonObject.getString("section_name");
                JSONArray section_itemsJSON= jsonObject.getJSONArray("section_items");

                for(int i=0; i<section_itemsJSON.length(); i++){
                    foodItems.add(new FoodItem(section_itemsJSON.getJSONObject(i)));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

    public Section() {
        foodItems = new ArrayList<FoodItem>();

        name = "Grainery";
        foodItems.add(new FoodItem());
        foodItems.add(new FoodItem());

    }

    public ArrayList<FoodItem> getFoodItems(){
            return foodItems;
        }
        public String getName(){
            return name;
        }


    }


