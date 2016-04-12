package feast.parsers;

import org.json.JSONObject;

import feast.FoodItem;

/**
 * Created by Riley on 4/12/16.
 */
public class FoodItemParser
{
    public FoodItem parsedFoodItemFromJSON(JSONObject object)
    {
        FoodItem foodItem = new FoodItem();

        try
        {
            String foodName = object.getString("food_name");

            if (foodName.contains("(V)"))
            {
                foodName.replace("(V)", "");
                foodItem.setDietType(FoodItem.Type.VEGAN);
            }
            else if (foodName.contains("(VT)"))
            {
                foodName.replace("(VT)", "");
                foodItem.setDietType(FoodItem.Type.VEGETARIAN);
            }
            else
            {
                foodItem.setDietType(FoodItem.Type.ANY);
            }

            foodName.trim();
            foodItem.setName(foodName);


            foodItem.setIdentifier(object.getInt("food_identifier"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return foodItem;
    }
}
