package feast.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import feast.FoodItem;
import feast.MealSection;

/**
 * Created by Riley on 4/12/16.
 */
public class MealSectionParser
{
    private FoodItemParser foodItemParser = new FoodItemParser();

    public MealSection parsedMealSectionFromJSON(JSONObject object)
    {
        MealSection mealSection = new MealSection();

        try
        {
            mealSection.setIdentifier(object.getString("section_identifier"));
            mealSection.setName(object.getString("section_name"));


            JSONArray array = (JSONArray)object.get("section_items");

            ArrayList<FoodItem> foodItems = new ArrayList<FoodItem>();

            for (int i = 0; i < array.length(); i++)
            {
                try
                {
                    JSONObject foodItemObject = array.getJSONObject(i);

                    FoodItem foodItem = foodItemParser.parsedFoodItemFromJSON(foodItemObject);
                    foodItems.add(foodItem);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            Collections.sort(foodItems);

            mealSection.setFoodItems(foodItems);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return mealSection;
    }
}
