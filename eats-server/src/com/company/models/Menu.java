package com.company.models;

import com.mongodb.MongoClient;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.query.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by brian on 4/15/16.
 */
@Entity("menus")
public class Menu {

    @Id
    ObjectId _id;
    List<Meal> meals;
    Date date;
    String restaurant_availability;
    @Reference
    ObjectId restaurant_id;

    public Menu(ObjectId _id, List<Meal> meals, Date date, String restaurant_availability, ObjectId restaurant_id) {
        this._id = _id;
        this.meals = meals;
        this.date = date;
        this.restaurant_availability = restaurant_availability;
        this.restaurant_id = restaurant_id;
    }

    public Menu(){}

    public static class Meal {
        public Meal() {}

        public Meal(Date meal_end_time, Date meal_begin_time, String meal_availablity, String meal_identifier, String meal_name, List<MealSections> meal_sections) {
            this.meal_end_time = meal_end_time;
            this.meal_begin_time = meal_begin_time;
            this.meal_availablity = meal_availablity;
            this.meal_identifier = meal_identifier;
            this.meal_name = meal_name;
            this.meal_sections = meal_sections;
        }

        Date meal_end_time;
        Date meal_begin_time;
        String meal_availablity; // "open" or "closed"
        String meal_identifier; // "breakfast", "lunch"
        String meal_name; // "Breakfast"
        List<MealSections> meal_sections;
    }

    public static class MealSections{
        public MealSections(){}

        public MealSections(String section_identifier, String section_name, List<FoodItem> section_items) {
            this.section_identifier = section_identifier;
            this.section_name = section_name;
            this.section_items = section_items;
        }

        String section_identifier;
        String section_name;
        List<FoodItem> section_items;
    }

    public static class FoodItem {
        public FoodItem(){}

        String food_identifier;
        String food_name;

        public FoodItem(String food_identifier, String food_name) {
            this.food_identifier = food_identifier;
            this.food_name = food_name;
        }
    }

    public static void main(String[] args) {
        Datastore ds = new Morphia().createDatastore(new MongoClient("localhost", 27017), "eatseve");
        ds.ensureIndexes();
//        List<FoodItem>  foodItems = new ArrayList<FoodItem>();
//        foodItems.add(new FoodItem("fried_chicken", "Fried Chicken"));
//        foodItems.add(new FoodItem("pasta_and_sauce", "Pasta and Sauce"));
//        List<MealSections> sections = new ArrayList<MealSections>();
//        sections.add(new MealSections("pasta_bar", "Pasta Bar", foodItems));
//        List<Meal> meals = new ArrayList<Meal>();
//        Meal meal = new Meal(new Date(2016, 1, 1, 9, 0, 0), new Date(2016, 1, 1, 11, 0, 0), "open", "breakfast", "Breakfast", sections);
//        meals.add(meal);
//        Menu myMenu = new Menu(null, meals, new Date(2016, 1, 1), "open", null);
//        ds.save(myMenu);



        Query<Menu> query = ds.createQuery(Menu.class);
        List<Menu> menus = query.asList();
        System.out.println("Menu size " + menus.size());
        if(menus.size() > 0){
            Menu menu = menus.get(0);
            System.out.println(menu.restaurant_availability);
        }
    }

}

