package feast.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import feast.Meal;
import feast.MealSection;
import feast.Menu;

/**
 * Created by Riley on 4/12/16.
 */
public class MealParser {
    private SimpleDateFormat dateFormat;

    private MealSectionParser mealSectionParser = new MealSectionParser();

    MealParser() {
        this.dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    }

    public Meal parsedMealFromJSON(JSONObject object) {
        Meal meal = new Meal();

        try {
            meal.setIdentifier(object.getString("meal_identifier"));
            meal.setName(object.getString("meal_name"));

            meal.setStartTime(new Date(object.getLong("meal_begin_time")));
            meal.setEndTime(new Date(object.getLong("meal_end_time")));


            JSONArray array = (JSONArray)object.get("meal_sections");

            ArrayList<MealSection> mealSections = new ArrayList<MealSection>();

            for (int i = 0; i < array.length(); i++)
            {
                try
                {
                    JSONObject mealSectionObject = array.getJSONObject(i);

                    MealSection mealSection = mealSectionParser.parsedMealSectionFromJSON(mealSectionObject);
                    mealSection.setMeal(meal);

                    mealSections.add(mealSection);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            Collections.sort(mealSections);

            meal.setMealSections(mealSections);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return meal;
    }
}
