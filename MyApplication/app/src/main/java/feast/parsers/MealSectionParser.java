package feast.parsers;

import org.json.JSONObject;

import feast.MealSection;

/**
 * Created by Riley on 4/12/16.
 */
public class MealSectionParser
{
    public MealSection parsedMealSectionFromJSON(JSONObject object)
    {
        MealSection mealSection = new MealSection();

        try
        {
            mealSection.setIdentifier(object.getInt("section_identifier"));
            mealSection.setName(object.getString("section_name"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return mealSection;
    }
}
