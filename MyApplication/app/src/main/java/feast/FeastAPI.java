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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import feast.parsers.FoodItemParser;
import feast.parsers.MenuParser;

/**
 * Created by Riley on 4/10/16.
 */
public class FeastAPI
{
    public interface RequestCallback
    {
        public void requestFinishedWithSuccess(Boolean success, VolleyError error);
    }

    public interface FavoritesCallback
    {
        public void fetchedFavorites(Set<FoodItem> favorites, VolleyError error);
    }

    public interface MenusCallback
    {
        public void fetchedMenus(Set<Menu> menus, VolleyError error);
    }

    public void setContext(Context context)
    {
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public static FeastAPI sharedAPI = new FeastAPI();

    private String userIdentifier = null;
    private String baseURL = "http://159.203.216.246:8000";
    private RequestQueue requestQueue;

    private FeastAPI()
    {

    }

    public Boolean isUserAuthorized()
    {
        return this.userIdentifier != null;
    }

    public void authorizeUserWithOAuthToken(String oauthToken, final RequestCallback callback)
    {
        String href = this.baseURL + "/authorize";

        JSONObject object = null;

        try
        {
            object = new JSONObject();
            object.put("oauth_token", oauthToken);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, href, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                VolleyError error = null;

                try
                {
                    FeastAPI.this.userIdentifier = response.getString("user_id");
                }
                catch (JSONException exception)
                {
                    exception.getMessage();
                    error = new VolleyError(new String("Failed to authorize user"));
                }

                callback.requestFinishedWithSuccess(FeastAPI.this.userIdentifier != null, error);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.requestFinishedWithSuccess(false, error);
            }
        });

        this.requestQueue.add(request);
    }

    public void fetchMenusForDateWithCompletion(Date date, final MenusCallback callback)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String href = this.baseURL + "/menus?date=" + dateFormat.format(date);

        JsonArrayRequest request = new JsonArrayRequest(href, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                MenuParser parser = new MenuParser();

                Set<Menu> menus = new HashSet<Menu>();

                for (int i = 0; i < response.length(); i++)
                {
                    try
                    {
                        JSONObject object = response.getJSONObject(i);

                        Menu menu = parser.parsedMenuFromJSON(object);
                        menus.add(menu);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

                callback.fetchedMenus(menus, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.fetchedMenus(null, error);
            }
        });

        this.requestQueue.add(request);
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

                callback.fetchedFavorites(foodItems, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.fetchedFavorites(null, error);
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
                callback.requestFinishedWithSuccess(true, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.requestFinishedWithSuccess(false, error);
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
                callback.requestFinishedWithSuccess(true, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.requestFinishedWithSuccess(false, error);
            }
        });

        this.requestQueue.add(request);
    }



}
