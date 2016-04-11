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

import com.example.sahil.myapplication.FeastObjects.FeastObjects.Meal;
import com.example.sahil.myapplication.FeastObjects.FeastObjects.Menu;
import com.example.sahil.myapplication.FeastObjects.FeastObjects.Restaurant;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Sahil on 4/8/16.
 */
public class RestaurantFragment extends Fragment {
//    RecyclerView recyclerView;
//    MealRecyclerViewAdapter mealRecyclerViewAdapter;

    String diningHallID;
    ArrayList<Meal> meals = new ArrayList<>();
    Restaurant restaurant;

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
        restaurant = new Restaurant(); // would change this
        Log.d("Sahil", "Made a new restaurant: " + restaurant.getName());

        // get the desired date's selected Menu
        Menu dateSelectedMenu = restaurant.getMenus().get(0); // would change this to select the right date

        // get the meals for the day (breakfast, lunch, dinner)
        ArrayList<Meal> mealsOfDay = dateSelectedMenu.getMeals();
        for(int i=0; i < mealsOfDay.size(); i++) {
            // make a MealFragment for each Meal and add it to the view for the RestaurantFragment
            MealFragment mealFragment = new MealFragment();
            // pass whatever data to the mealFragment that you need to through the arguments

            Bundle args = new Bundle();
            args.putInt("MealType", i);
            mealFragment.setArguments(args);

            FragmentManager fm = getChildFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.linearLayoutRestaurant, mealFragment).commit();
        }



        // using listView for Each meal ~ scrapped ~

        // 2nd parameter takes the listView

//        MealListAdapter mealListAdapter = new MealListAdapter(this.getContext(), R.id.meal_list, mealsOfDay);
//        ListView mealListView= (ListView) view.findViewById(R.id.meal_list);
//        mealListView.setAdapter(mealListAdapter);




        return view;

    }



    public class MealListAdapter extends ArrayAdapter<Meal> {
//        List<Meal> meals;

        public MealListAdapter(Context context, int resource, List<Meal> objects) {
            super(context, resource, objects);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return super.getView(position, convertView, parent);
        }
    }




//    public static RestaurantFragment newInstance(Restaurant restaurant) {
//        RestaurantFragment fragment = new RestaurantFragment();
////        Bundle args = new Bundle();
////        args.putString(ARG_PARAM1, param1);
////        args.putString(ARG_PARAM2, param2);
////        fragment.setArguments(args);
//
//        return fragment;
//    }











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

