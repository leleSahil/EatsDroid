package feast.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import feast.FoodItem;
import feast.Meal;
import feast.Menu;

/**
 * Created by Riley on 4/11/16.
 */
public class MenuParser
{
    private SimpleDateFormat dateFormat;

    private MealParser mealParser = new MealParser();

    public MenuParser()
    {
        this.dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    }

    public Menu parsedMenuFromJSON(JSONObject object)
    {
        Menu menu = new Menu();

        try
        {
            menu.setDate(new Date(object.getLong("date")));

            menu.setIdentifier(object.getString("_id"));
            menu.setRestaurantName(object.getString("restaurant_id"));

            JSONArray array = (JSONArray)object.get("meals");

            ArrayList<Meal> meals = new ArrayList<Meal>();

            for (int i = 0; i < array.length(); i++)
            {
                try
                {
                    JSONObject mealObject = array.getJSONObject(i);

                    Meal meal = mealParser.parsedMealFromJSON(mealObject);
                    meal.setMenu(menu);

                    meals.add(meal);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            Collections.sort(meals);

            menu.setMeals(meals);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return menu;
    }

}
