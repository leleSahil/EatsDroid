package feast;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import feast.parsers.FoodItemParser;

/**
 * Created by Riley on 4/10/16.
 */
public class FeastAPI
{
    public interface RequestCallback
    {
        public void requestFinishedWithSuccess(Boolean success);
    }

    public interface FavoritesCallback
    {
        public void fetchedFavorites(Set<FoodItem> favorites);
    }

    public void setContext(Context context)
    {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public static FeastAPI sharedAPI = new FeastAPI();

    private String userIdentifier = "5711c2931a95609d06ed8125";
    private String baseURL = "http://159.203.216.246:8000";
    private RequestQueue requestQueue;

    private FeastAPI()
    {

    }

    public void fetchFavoritesWithCompletion(final FavoritesCallback callback)
    {
        String href = this.baseURL + "/users/" + this.userIdentifier + "/favorites";

        JsonArrayRequest request = new JsonArrayRequest(href, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                FoodItemParser parser = new FoodItemParser();

                Set<FoodItem> foodItems = new HashSet<FoodItem>();

                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        JSONObject object = response.getJSONObject(i);

                        FoodItem foodItem = parser.parsedFoodItemFromJSON(object);
                        foodItems.add(foodItem);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                callback.fetchedFavorites(foodItems);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.w("Riley", error.toString());
            }
        });

        this.requestQueue.add(request);
    }

    public void addFavoriteWithCompletion(FoodItem favorite, final RequestCallback callback)
    {
        String href = this.baseURL + "/users/" + this.userIdentifier + "/favorites";

        JSONObject object = null;

        try
        {
            object = new JSONObject();
            object.put("food_identifier", favorite.getIdentifier());
            object.put("food_name", favorite.getName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, href, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.requestFinishedWithSuccess(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.requestFinishedWithSuccess(false);
            }
        });

        this.requestQueue.add(request);
    }

    public void removeFavoriteWithCompletion(FoodItem favorite, final  RequestCallback callback)
    {
        String href = this.baseURL + "/users/" + this.userIdentifier + "/favorites/" + favorite.getIdentifier();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, href, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.requestFinishedWithSuccess(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.requestFinishedWithSuccess(false);
            }
        });

        this.requestQueue.add(request);
    }



}
