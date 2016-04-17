package com.example.sahil.myapplication;

import java.util.Set;

import feast.FoodItem;

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

    private Set<FoodItem> favorites;

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

    public Set<FoodItem> getFavorites() {
        return favorites;
    }

    public void setFavorites(Set<FoodItem> favorites) {
        this.favorites = favorites;
    }
}
