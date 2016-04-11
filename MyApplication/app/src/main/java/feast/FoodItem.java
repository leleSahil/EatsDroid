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

    private int identifier;
    private String name;

    private Type dietType;

    // Relationships
    private Favorite favorite;
    private Set<MealSection> mealSections;

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

    public Type getDietType() {
        return dietType;
    }

    public void setDietType(Type dietType) {
        this.dietType = dietType;
    }

    public Favorite getFavorite() {
        return favorite;
    }

    public void setFavorite(Favorite favorite) {
        this.favorite = favorite;
    }

    public Set<MealSection> getMealSections() {
        return mealSections;
    }

    public void setMealSections(Set<MealSection> mealSections) {
        this.mealSections = mealSections;
    }
}
