package com.example.sahil.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

//import com.example.sahil.myapplication.FeastObjects.FeastObjects.FoodItem;
//import com.example.sahil.myapplication.FeastObjects.FeastObjects.Meal;
//import com.example.sahil.myapplication.FeastObjects.FeastObjects.Menu;
//import com.example.sahil.myapplication.FeastObjects.FeastObjects.Restaurant;
//import com.example.sahil.myapplication.FeastObjects.FeastObjects.Section;

import com.android.volley.VolleyError;
import com.example.sahil.myapplication.Utils.CalendarUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    int diningHallID;
    Map<Integer, Menu> menus;

    ArrayList<ListItemParent> listItems;
    MyListAdapter myListAdapter;

    private Boolean fetchedMenus = false;
    private Boolean fetchedFavorites = false;

    private Set<FoodItem> favorites = null;

    private static final int CONTENT_VIEW_ID = 10101010; // have to set this for programmatically adding FragmentViews?

    private static int year = -1, month = -1, day = -1;

    public RestaurantFragment() {
//        this.restaurant = restaurant;
        initializeListItems();
//        if(year== -1 || month== -1 || day == -1) {
//            year = CalendarUtils.getYear();
//            month = CalendarUtils.getMonth();
//            day = CalendarUtils.getDay(); // + 1
//        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        diningHallID = getArguments().getInt("DiningHallID");
        //Log.w("Sahil", "dining hall id is: " + diningHallID);
    }

    //    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        // TODO get the id of the restaurant desired from the bundle
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(android.view.Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.restaurant_fragment_menu, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getI == R.id.action_calendar) {
//            DialogFragment newFragment = new DatePickerFragment();
//            newFragment.show(getFragmentManager(), "MyDialog");
//        }
//    }

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
//            date = dateFormat.parse("2016/04/18");
            date = dateFormat.parse(year + "/" + month + "/" + day); //TODO commment this back in, some problem with today's menu
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        this.fetchDataForDate(date);

        return populateListView(view, menus);
    }

    private void fetchDataForDate(Date date)
    {
        Log.w("Riley", "WTF");

        FeastAPI.sharedAPI.fetchMenusForDateWithCompletion(date, new FeastAPI.MenusCallback() {
            @Override
            public void fetchedMenus(Map<Integer, Menu> menus, VolleyError error) {
                if(error == null) {

                    fetchedMenus = true;
                    RestaurantFragment.this.menus = menus;

                    Log.w("Riley", menus.toString());

                    if (fetchedFavorites || !FeastAPI.sharedAPI.isUserAuthorized()) {

                        // recreate ArrayList<ListItemParent> listItems
                        // notifyDataSetChanged()
                        if(!menus.isEmpty()) {
                            listItems.clear();
                            // TODO check that a menu for that diningHallID actually exists
                            updateListItems(menus.get(new Integer(diningHallID)));
                            myListAdapter.notifyDataSetChanged();
                        }
                    }


                } else
                {
                    Toast toast = Toast.makeText(EatsApplication.applicationContext, "Failed to fetch menu data.", Toast.LENGTH_LONG);
                    toast.show();

                    // TODO alert the user that there was an error refreshing
                    Log.w("Sahil", "Error getting data: " + error.toString());
                }
            }
        });

        if (FeastAPI.sharedAPI.isUserAuthorized())
        {
            FeastAPI.sharedAPI.fetchFavoritesWithCompletion(new FeastAPI.FavoritesCallback() {
                @Override
                public void fetchedFavorites(Set<FoodItem> favorites, VolleyError error) {
                    if (error == null)
                    {
                        fetchedFavorites = true;

                        RestaurantFragment.this.favorites = favorites;

                        if (fetchedMenus)
                        {
                            // recreate ArrayList<ListItemParent> listItems
                            // notifyDataSetChanged()
                            listItems.clear();
                            updateListItems(menus.get(new Integer(diningHallID)));
                            myListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    }


    private View populateListView(View view, Map<Integer, Menu> menus) {
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
                if(mealSection.getFoodItems() != null) {
                    for (FoodItem foodItem : mealSection.getFoodItems()) {
                        ListItemParent food = new ListItemParent(ListItemParent.foodHeader);
                        food.setTitle(foodItem.getName()); // probably not necessary but oh well
                        food.setFoodItem(foodItem);

                        food.setFavorites(this.favorites);

                        listItems.add(food);
                    }
                }
            }
        }
    }

    private void setUpListView(View view) {
        myListAdapter = new MyListAdapter(getContext(), R.id.mealListView, listItems);
        ListView mealListView= (ListView) view.findViewById(R.id.mealListView);
        mealListView.setAdapter(myListAdapter);
    }

    public static void setDates(int year_x, int month_x, int day_x) {
        year = year_x;
        month = month_x;
        day = day_x;

        Log.w("Sahil", "Date set to " + year + "-" +  month + "-" + day);
    }

    public void refreshMenus() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = null;
        try
        {
//            date = dateFormat.parse("2016/04/17");
            date = dateFormat.parse(year + "/" + month + "/" + day);
//            date = new Date(year, month, day);
            Log.w("Sahil", "date from refresh menus is " + date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.fetchDataForDate(date);
    }
}

