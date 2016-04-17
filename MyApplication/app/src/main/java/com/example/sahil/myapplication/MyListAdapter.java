package com.example.sahil.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import feast.FeastAPI;
import feast.FoodItem;

/**
 * Created by Sahil on 4/11/16.
 */
public class MyListAdapter extends ArrayAdapter<ListItemParent> {
    private ArrayList<ListItemParent> objects;
    private Context context;


    // be sure to check that you overloaded the right constructor
    public MyListAdapter(Context context, int resource, ArrayList<ListItemParent> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);

        // assign the view we are converting to a local variable
        View view = convertView;
        final ListItemParent currentItem = objects.get(position);
        int itemViewType = -1;

        // first check to see if the view is null. if so, we have to inflate it.
        // to inflate it basically means to render, or show, the view.
//        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(currentItem.getType() == ListItemParent.mealHeader) {
                view = inflater.inflate(R.layout.meal_header_row, null);
                itemViewType = ListItemParent.mealHeader;
            } else if(currentItem.getType() == ListItemParent.sectionHeader) {
                view = inflater.inflate(R.layout.section_item_row, null);
                itemViewType = ListItemParent.sectionHeader;
            } else { // if(currentItem.getType() == ListItemParent.sectionHeader) --> caused a problem
                view = inflater.inflate(R.layout.food_item_row, null);
                itemViewType = ListItemParent.foodHeader;
            }
//        }


        if(currentItem != null) {
//            itemViewType = getItemViewType(currentItem);
            if (itemViewType == ListItemParent.mealHeader) {
                TextView mealSeparator = (TextView) view.findViewById(R.id.mealSeparator);
                mealSeparator.setText(currentItem.getTitle());
            } else if (itemViewType == ListItemParent.sectionHeader) {
                TextView sectionSeparator = (TextView) view.findViewById(R.id.sectionSeparator);
                sectionSeparator.setText(currentItem.getTitle());
            } else if (itemViewType == ListItemParent.foodHeader) {
                TextView foodItemName = (TextView) view.findViewById(R.id.food_item_text);
                foodItemName.setText(currentItem.getTitle());
                ImageView imageView = (ImageView) view.findViewById(R.id.dietary_identifier);
//                if (currentItem.getFoodItem().isV()) {
//                    imageView.setImageResource(R.drawable.iconvegan);
//                } else if (currentItem.getFoodItem().isVT()) {
//                    imageView.setImageResource(R.drawable.iconvegetarian);
//                } else {
//                    imageView.setVisibility(View.GONE);
//                }

                CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkbox);

                if (currentItem.getFavorites() != null)
                {
                    checkBox.setVisibility(View.VISIBLE);
                    checkBox.setChecked(currentItem.getFavorites().contains(currentItem.getFoodItem()));
                }
                else
                {
                    checkBox.setVisibility(View.INVISIBLE);
                }

                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        if ( isChecked )
                        {
                            MyListAdapter.this.addFavorite(currentItem.getFoodItem(), currentItem.getFavorites());
                        }
                        else
                        {
                            MyListAdapter.this.removeFavorite(currentItem.getFoodItem(), currentItem.getFavorites());
                        }

                    }
                });

            }
        }

        return view;
    }

    private int getItemViewType(ListItemParent currentItem) {
        if(currentItem.getType() == ListItemParent.mealHeader) {
            return ListItemParent.mealHeader;
        } else if(currentItem.getType() == ListItemParent.sectionHeader) {
            return ListItemParent.sectionHeader;
        } else if (currentItem.getType() == ListItemParent.foodHeader){
            return ListItemParent.foodHeader;
        } else
            return -1;
    }

    private void addFavorite(final FoodItem foodItem, final Set<FoodItem> favorites)
    {
        favorites.add(foodItem);

        FeastAPI.sharedAPI.addFavoriteWithCompletion(foodItem, new FeastAPI.RequestCallback() {
            @Override
            public void requestFinishedWithSuccess(Boolean success, VolleyError error) {
                if (success)
                {
                    Log.w("Riley", "SUCCESS adding favorite");
                }
                else
                {
                    favorites.remove(foodItem);
                    Log.w("Riley", "FAILURE adding favorite");
                }
            }
        });
    }

    private void removeFavorite(final FoodItem foodItem, final Set<FoodItem> favorites)
    {
        favorites.remove(foodItem);

        FeastAPI.sharedAPI.removeFavoriteWithCompletion(foodItem, new FeastAPI.RequestCallback() {
            @Override
            public void requestFinishedWithSuccess(Boolean success, VolleyError error) {
                if (success)
                {
                    Log.w("Riley", "SUCCESS adding favorite");
                }
                else
                {
                    Log.w("Riley", "FAILURE adding favorite");

                    favorites.add(foodItem);
                }
            }
        });
    }


}
