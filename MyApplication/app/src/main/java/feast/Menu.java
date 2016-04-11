package feast;

import java.util.Date;
import java.util.Set;

/**
 * Created by Riley on 4/10/16.
 */
public class Menu
{
    private int identifier;
    private Date date;

    // Relationships
    private Set<Meal> meals;
    private Restaurant restaurant;

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Meal> getMeals() {
        return meals;
    }

    public void setMeals(Set<Meal> meals) {
        this.meals = meals;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
