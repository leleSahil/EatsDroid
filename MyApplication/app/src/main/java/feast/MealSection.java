package feast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by Riley on 4/11/16.
 */
public class MealSection implements Comparable<MealSection>
{
    private String identifier;
    private String name;

    // Relationships
    private ArrayList<FoodItem> foodItems;
    private Meal meal;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(ArrayList<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    @Override
    public int compareTo(MealSection meal) {
        return this.name.compareTo(meal.name);
    }
}
