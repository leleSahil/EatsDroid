package feast.parsers;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

import feast.Meal;
import feast.Menu;

/**
 * Created by Riley on 4/12/16.
 */
public class MealParser {
    private SimpleDateFormat dateFormat;

    MealParser() {
        this.dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    }

    public Meal parsedMealFromJSON(JSONObject object) {
        Meal meal = new Meal();

        try {
            meal.setIdentifier(object.getInt("meal_identifier"));
            meal.setName(object.getString("meal_name"));

            String startTimeString = object.getString("meal_begin_time");
            String endTimeString = object.getString("meal_end_time");

            meal.setStartTime(this.dateFormat.parse(startTimeString));
            meal.setEndTime(this.dateFormat.parse(endTimeString));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return meal;
    }
}
