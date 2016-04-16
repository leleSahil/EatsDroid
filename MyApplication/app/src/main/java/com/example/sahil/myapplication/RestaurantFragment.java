package com.example.sahil.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//import com.example.sahil.myapplication.FeastObjects.FeastObjects.FoodItem;
//import com.example.sahil.myapplication.FeastObjects.FeastObjects.Meal;
//import com.example.sahil.myapplication.FeastObjects.FeastObjects.Menu;
//import com.example.sahil.myapplication.FeastObjects.FeastObjects.Restaurant;
//import com.example.sahil.myapplication.FeastObjects.FeastObjects.Section;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import feast.FeastAPI;
import feast.FoodItem;
import feast.Meal;
import feast.MealSection;
import feast.Menu;


/**
 * Created by Sahil on 4/8/16.
 */
public class RestaurantFragment extends Fragment {
//    RecyclerView recyclerView;
//    MealRecyclerViewAdapter mealRecyclerViewAdapter;

    String diningHallID;
    Set<feast.Menu> menuSet;

    ArrayList<ListItemParent> listItems;
    MyListAdapter myListAdapter;


    private static final int CONTENT_VIEW_ID = 10101010; // have to set this for programmatically adding FragmentViews?

    public RestaurantFragment() {
//        this.restaurant = restaurant;
        initializeListItems();
    }

    private void initializeListItems() {
        // create one meal
        Meal dummyMeal = new Meal();
        dummyMeal.setName("Waiting for Data");
//        dummyMeal.setMealSections(new ArrayList<MealSection>());
//        MealSection dummyMealSection = new MealSection();
//        dummyMealSection.setMeal(dummyMeal);


        listItems = new ArrayList<ListItemParent>();
        ListItemParent mealItem = new ListItemParent(ListItemParent.mealHeader);
        mealItem.setTitle(dummyMeal.getName());
        listItems.add(mealItem);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_fragment, container, false); // false as third parameter?

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try
        {
            date = dateFormat.parse("2016/04/15");
            Log.w("Sahil", "date is " + date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        FeastAPI.sharedAPI.fetchMenusForDateWithCompletion(date, new FeastAPI.MenusCallback() {
            @Override
            public void fetchedMenus(Set<feast.Menu> menus) {
                Log.w("Riley", menus.toString());
                menuSet = menus;
                // recreate ArrayList<ListItemParent> listItems
                // notifyDataSetChanged()
                listItems.clear();
                updateListItems(menuSet.iterator().next());
                myListAdapter.notifyDataSetChanged();
            }
        });
        return populateListView(view, menuSet);
    }

    private View populateListView(View view, Set<Menu> menuSet) {
        // get the desired date's selected Menu
//        Menu dateSelectedMenu = menuSet.iterator().next(); // would change this to select the right date

        // get the meals for the day (breakfast, lunch, dinner)
//        ArrayList<Meal> mealsOfDay = dateSelectedMenu.getMeals();

//        listItems = new ArrayList<ListItemParent>();
//        updateListItems(dateSelectedMenu);



        setUpListView(view);
        return view;
    }

    private void updateListItems(Menu selectedMenu) {
        for(Meal currentMeal: selectedMenu.getMeals()) { // int i=0; i < mealsOfDay.size(); i++
            // for each Meal - make one header add it to a vector of custom parent type
            // for each section - make a add it to a vector of custom parent type

//            Meal currentMeal = mealsOfDay.get(i);
            ListItemParent mealItem = new ListItemParent(ListItemParent.mealHeader);
            mealItem.setTitle(currentMeal.getName());
            listItems.add(mealItem);

            for(MealSection mealSection: currentMeal.getMealSections()) {
                ListItemParent sectionItem = new ListItemParent(ListItemParent.sectionHeader);
                sectionItem.setTitle(mealSection.getName());
                listItems.add(sectionItem);
                for(FoodItem foodItem: mealSection.getFoodItems()) {
                    ListItemParent food = new ListItemParent(ListItemParent.foodHeader);
                    food.setTitle(foodItem.getName()); // probably not necessary but oh well
                    food.setFoodItem(foodItem);
                    listItems.add(food);
                }
            }
        }
    }

    private void setUpListView(View view) {
        myListAdapter = new MyListAdapter(getContext(), R.id.mealListView, listItems);
        ListView mealListView= (ListView) view.findViewById(R.id.mealListView);
        mealListView.setAdapter(myListAdapter);
    }
}

