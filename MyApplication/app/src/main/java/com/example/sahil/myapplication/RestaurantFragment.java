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


/**
 * Created by Sahil on 4/8/16.
 */
public class RestaurantFragment extends Fragment {
//    RecyclerView recyclerView;
//    MealRecyclerViewAdapter mealRecyclerViewAdapter;

    String diningHallID;
    Set<feast.Menu> menuSet;

    ArrayList<ListItemParent> listItems;


    private static final int CONTENT_VIEW_ID = 10101010; // have to set this for programmatically adding FragmentViews?

    public RestaurantFragment() {
//        this.restaurant = restaurant;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_fragment, container, false); // false as third parameter?

        // return super.onCreateView(inflater, container, savedInstanceState);

//        diningHallID = getArguments().getString("Dining Hall ID");

        //get the currentlySelectedRestaurant
//        restaurant = new Restaurant(); // would change this



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
            }
        });



        while (menuSet == null) {

        }

        // get the desired date's selected Menu
        feast.Menu dateSelectedMenu = menuSet.iterator().next(); // would change this to select the right date

        // get the meals for the day (breakfast, lunch, dinner)
//        ArrayList<Meal> mealsOfDay = dateSelectedMenu.getMeals();

        ArrayList<ListItemParent> listItems = new ArrayList<ListItemParent>();
        for(Meal currentMeal: dateSelectedMenu.getMeals()) { // int i=0; i < mealsOfDay.size(); i++
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
        setUpListView(view, listItems);
        return view;

    }

    private void setUpListView(View view, ArrayList<ListItemParent> listItems) {
        MyListAdapter myListAdapter = new MyListAdapter(getContext(), R.id.mealListView, listItems);
        ListView mealListView= (ListView) view.findViewById(R.id.mealListView);
        mealListView.setAdapter(myListAdapter);
    }

    /*

    private void createRecyclerView() {
        Restaurant restaurant = DBWrapper.getRestaurants(new Date()).get(Integer.getInteger(diningHallID));

        if(restaurant != null) {
            meals = restaurant.getMenus().get(0).getMeals();
        } else {
            meals = new ArrayList<>();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        mealRecyclerViewAdapter = new MealRecyclerViewAdapter(meals);
        recyclerView.setAdapter(mealRecyclerViewAdapter);

    }

    private static class MealRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        ArrayList<Meal> meals;
        private Context context;



        public MealRecyclerViewAdapter(ArrayList<Meal> meals) {
            this.meals = meals;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.)
            return new ViewHolder()
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

        }

        private static class ViewHolder extends RecyclerView.ViewHolder{
            public ViewHolder(View itemView) {
                super(itemView);
            }


            public class
        }
    }

    */






}

