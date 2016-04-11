package com.example.sahil.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sahil.myapplication.FeastObjects.FeastObjects.FoodItem;
import com.example.sahil.myapplication.FeastObjects.FeastObjects.Meal;
import com.example.sahil.myapplication.FeastObjects.FeastObjects.Section;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sahil on 4/9/16.
 */
public class MealFragment extends Fragment {
    // region Variables

    public static final String TAG = "DiningHallFragment";


//    @Bind(R.id.menu)
    RecyclerView recyclerView;

    SectionRecyclerViewAdapter recyclerViewAdapter;
    String diningHallType;
//    DiningHallUtils.MealTime selectedMealTime;
//    USCDatabaseManager databaseManager;
//    String selectedDay;
    ArrayList<Section> sections = new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meal_fragment, container, false); // false as third parameter?

        int mealType = getArguments().getInt("MealType");

        // TODO get the actual corresponding meal
        Meal meal = new Meal(mealType);

        // should set the name of the meal here
        TextView mealName = (TextView)view.findViewById(R.id.mealNameTextView);
        mealName.setText(meal.getName());

        recyclerView = (RecyclerView) view.findViewById(R.id.sections_recyclerView);

        setUpRecyclerView(meal);
        return view;
    }

    private void setUpRecyclerView(Meal meal) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new SectionRecyclerViewAdapter(this.getContext(), meal.getSections());
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private static class SectionRecyclerViewAdapter extends RecyclerView.Adapter<SectionRecyclerViewAdapter.ViewHolder> {
        private List<Section> sections;
        private Context context;

        public SectionRecyclerViewAdapter(Context context, List<Section> sections) {
            this.context = context;
            this.sections = sections;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.section_card, parent, false);
            return new ViewHolder(context, view);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Section section = sections.get(position);
            viewHolder.setData(section);
        }

        @Override
        public int getItemCount() {
            return sections.size();
        }



        public static class ViewHolder extends RecyclerView.ViewHolder {
            public Context context;
            public ArrayList<TextView> menuItems;
            public final View view;
            public final TextView diningHallStationHeaderView;
            public final LinearLayout cardLayout;

            public ViewHolder(Context context, View view) {
                super(view);
                this.context = context;
                this.view = view;
                diningHallStationHeaderView = (TextView) view.findViewById(R.id.dining_hall_station_header_view);
                cardLayout = (LinearLayout) view.findViewById(R.id.card_layout);
                menuItems = new ArrayList<>();
            }

            public void setData(Section section){
                diningHallStationHeaderView.setText(section.getName());
                FoodListAdapter foodListAdapter = new FoodListAdapter(context, R.id.food_list, section.getFoodItems());
                ListView foodListView= (ListView) view.findViewById(R.id.food_list);
                foodListView.setAdapter(foodListAdapter);
            }

            public class FoodListAdapter extends ArrayAdapter<FoodItem> {

                public FoodListAdapter(Context context, int resource, List<FoodItem> items) {
                    super(context, resource, items);
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {

                    View v = convertView;

                    if (v == null) {
                        LayoutInflater vi;
                        vi = LayoutInflater.from(getContext());
                        v = vi.inflate(R.layout.food_item_row, null);
                    }
                    TextView foodItemName=(TextView)v.findViewById(R.id.food_item_text);
                    foodItemName.setText(getItem(position).getFoodName());
                    ImageView imageView=(ImageView)v.findViewById(R.id.dietary_identifier);
                    if(getItem(position).isV()){
                        imageView.setImageResource(R.drawable.iconvegan);
                    } else if(getItem(position).isVT()){
                        imageView.setImageResource(R.drawable.iconvegetarian);
                    } else {
                        imageView.setVisibility(View.GONE);
                    }


                    return v;
                }




            }
        }

    }
}
