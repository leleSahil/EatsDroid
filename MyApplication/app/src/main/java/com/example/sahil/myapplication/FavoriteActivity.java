package com.example.sahil.myapplication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.Set;

import feast.FeastAPI;
import feast.FoodItem;

public class FavoriteActivity extends AppCompatActivity {

    private Set<FoodItem> favorites = null;

    ArrayList<ListItemParent> listItems;
    MyListAdapter myListAdapter;

    private Boolean fetchedMenus = false;
    private Boolean fetchedFavorites = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);


//        FavoriteFragment favoriteFragment = new FavoriteFragment();
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.add(R.id.FavoritesFragmentContainer, favoriteFragment);
//        ft.add(R.id.linearLayoutRestaurant, favoriteFragment).commit();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = super.onCreateView(parent, name, context, attrs);
        setUpListView(view);
        fetchFavorites();
        return view;
    }

    private View populateListView(View view) {
        setUpListView(view);
        return view;
    }

    private void setUpListView(View view) {
        myListAdapter = new MyListAdapter(getApplicationContext(), R.id.favorite_listview, listItems);
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

                        FavoriteActivity.this.favorites = favorites;

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
        for(FoodItem favorite: favorites) {
            ListItemParent food = new ListItemParent(ListItemParent.foodHeader);
            food.setTitle(favorite.getName()); // probably not necessary but oh well
            food.setFoodItem(favorite);
            food.setFavorites(favorites);
            listItems.add(food);
        }
    }

}
