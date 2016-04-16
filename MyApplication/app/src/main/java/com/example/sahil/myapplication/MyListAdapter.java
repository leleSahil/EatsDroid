package com.example.sahil.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
        ListItemParent currentItem = objects.get(position);
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
}
