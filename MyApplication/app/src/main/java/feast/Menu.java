package feast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

/**
 * Created by Riley on 4/10/16.
 */
public class Menu
{
    private String identifier;
    private Date date;

    private String restaurantName;

    // Relationships
    private ArrayList<Meal> meals;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<Meal> meals) {
        this.meals = meals;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    @Override
    public int hashCode()
    {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof Menu == false)
        {
            return false;
        }

        Menu menu = (Menu)o;

        return this.identifier.equals(menu.identifier);
    }
}
