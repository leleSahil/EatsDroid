package com.example.sahil.myapplication;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Set;

import feast.FeastAPI;
import feast.FoodItem;
import feast.Meal;

public class FavoriteFragment extends Fragment {

    private Set<FoodItem> favorites = null;

    ArrayList<ListItemParent> listItems;
    MyListAdapter myListAdapter;

    private Boolean fetchedMenus = false;
    private Boolean fetchedFavorites = false;

    public FavoriteFragment() {
        // Required empty public constructor
//        initializeListItems();

    }

    private void initializeListItems() {
        Log.w("Favorite", "Inside initializeListItems");
        listItems = new ArrayList<ListItemParent>();
        ListItemParent mealItem = new ListItemParent(ListItemParent.mealHeader);
        mealItem.setTitle("Waiting for Favorites");
        listItems.add(mealItem);
    }

    /*
    // TODO: Rename and change types and number of parameters
    public static FavoriteFragment newInstance(String param1, String param2) {
        FavoriteFragment fragment = new FavoriteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        initializeListItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);


        fetchFavorites();
        return populateListView(view);
    }

    private View populateListView(View view) {
        setUpListView(view);
        return view;
    }

    private void setUpListView(View view) {
        myListAdapter = new MyListAdapter(getActivity(), R.id.favorite_listview, listItems);
        ListView favoriteListView= (ListView) view.findViewById(R.id.favorite_listview);
        favoriteListView.setAdapter(myListAdapter);
    }

    private void fetchFavorites() {
        if (FeastAPI.sharedAPI.isUserAuthorized())
        {
            FeastAPI.sharedAPI.fetchFavoritesWithCompletion(new FeastAPI.FavoritesCallback() {
                @Override
                public void fetchedFavorites(Set<FoodItem> favorites, VolleyError error) {
                    if (error == null)
                    {
                        fetchedFavorites = true;
                        Log.w("Favorite", "favorites are: " + favorites);
                        FavoriteFragment.this.favorites = favorites;

                        // recreate ArrayList<ListItemParent> listItems
                        // notifyDataSetChanged()
                        listItems.clear();
                        updateListItems(favorites);
                        myListAdapter.notifyDataSetChanged();

                    }
                }
            });
        }
    }

    private void updateListItems(Set<FoodItem> favorites) {
        ListItemParent mealItem = new ListItemParent(ListItemParent.mealHeader);
        mealItem.setTitle("Your Favorites");
        listItems.add(mealItem);

        for(FoodItem favorite: favorites) {
            ListItemParent food = new ListItemParent(ListItemParent.foodHeader);
            food.setTitle(favorite.getName()); // probably not necessary but oh well
            food.setFoodItem(favorite);
            food.setFavorites(favorites);
            listItems.add(food);
        }
    }
}
