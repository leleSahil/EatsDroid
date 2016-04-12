package feast.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import feast.Restaurant;

/**
 * Created by Riley on 4/11/16.
 */
public class RestaurantParser
{
    public Restaurant parsedRestaurantFromJSON(JSONObject object)
    {
        Restaurant restaurant = new Restaurant();

        try
        {
            restaurant.setName(object.getString("name"));
            restaurant.setIdentifier(object.getInt("_id"));
        }
        catch (JSONException exception)
        {
            exception.printStackTrace();
        }

        return restaurant;
    }

}
