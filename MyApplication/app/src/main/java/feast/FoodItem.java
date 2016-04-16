package feast;

import java.util.Set;

/**
 * Created by Riley on 4/10/16.
 */
public class FoodItem
{
    public enum Type
    {
        ANY, VEGETARIAN, VEGAN
    }

    private String identifier;
    private String name;

    private Type dietType;

    // Relationships
    private Set<MealSection> mealSections;

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

    public Type getDietType() {
        return dietType;
    }

    public void setDietType(Type dietType) {
        this.dietType = dietType;
    }

    public Set<MealSection> getMealSections() {
        return mealSections;
    }

    public void setMealSections(Set<MealSection> mealSections) {
        this.mealSections = mealSections;
    }

    @Override
    public int hashCode()
    {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object o)
    {
        if (o instanceof FoodItem == false)
        {
            return false;
        }

        FoodItem foodItem = (FoodItem)o;

        return this.identifier.equals(foodItem.identifier);
    }
}
