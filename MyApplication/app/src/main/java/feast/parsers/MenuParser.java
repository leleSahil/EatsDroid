package feast.parsers;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import feast.Menu;

/**
 * Created by Riley on 4/11/16.
 */
public class MenuParser
{
    private SimpleDateFormat dateFormat;

    MenuParser()
    {
        this.dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
    }

    public Menu parsedMenuFromJSON(JSONObject object)
    {
        Menu menu = new Menu();

        try
        {
            String dateString = object.getString("date");
            menu.setDate(this.dateFormat.parse(dateString));

            menu.setIdentifier(object.getInt("restaurant_id"));
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return menu;
    }

}
