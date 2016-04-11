package com.example.sahil.myapplication;

import com.example.sahil.myapplication.FeastObjects.FeastObjects.Restaurant;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Sahil on 4/8/16.
 */
public class DBWrapper {
    public static ArrayList<Restaurant> getRestaurants(Date date) {
        ArrayList<Restaurant> dummyRestaurants = new ArrayList<>();
        for(int i=0; i<3; i++) {
            Restaurant restaurant = new Restaurant();
            dummyRestaurants.add(restaurant);
        }
        return dummyRestaurants;
    }
}
