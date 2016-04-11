package feast;

import java.util.Date;
import java.util.Set;

/**
 * Created by Riley on 4/11/16.
 */
public class Meal
{
    private int identifier;
    private String name;
    private Date startTime;
    private Date endTime;

    // Relationships
    private Set<MealSection> mealSections;
    private Menu menu;

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

    public Set<MealSection> getMealSections() {
        return mealSections;
    }

    public void setMealSections(Set<MealSection> mealSections) {
        this.mealSections = mealSections;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
