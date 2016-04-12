package com.example.sahil.myapplication;


import com.example.sahil.myapplication.FeastObjects.FeastObjects.FoodItem;

/**
 * Created by Sahil on 4/11/16.
 */
public class ListItemParent {
    public static final int mealHeader = 1;
    public static final int sectionHeader = 2;
    public static final int foodHeader = 3;

    private String title;
    private FoodItem foodItem;
    private int type;

    public ListItemParent(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFoodItem(FoodItem item) {
        foodItem = item;
    }

    public int getType() {
        return type;
    }
}
