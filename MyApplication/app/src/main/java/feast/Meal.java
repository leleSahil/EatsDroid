package feast;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

/**
 * Created by Riley on 4/11/16.
 */
public class Meal implements Comparable<Meal>
{
    private String identifier;
    private String name;
    private Date startTime;
    private Date endTime;

    // Relationships
    private ArrayList<MealSection> mealSections;
    private Menu menu;

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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public ArrayList<MealSection> getMealSections() {
        return mealSections;
    }

    public void setMealSections(ArrayList<MealSection> mealSections) {
        this.mealSections = mealSections;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public int compareTo(Meal meal) {
        return this.startTime.compareTo(meal.startTime);
    }
}
