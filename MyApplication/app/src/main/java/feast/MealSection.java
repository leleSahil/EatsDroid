package feast;

import java.util.Set;

/**
 * Created by Riley on 4/11/16.
 */
public class MealSection
{
    private int identifier;
    private String name;

    // Relationships
    private Set<FoodItem> foodItems;
    private Meal meal;

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(Set<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }
}
