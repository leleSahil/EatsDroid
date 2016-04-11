package feast;

import java.util.Set;

/**
 * Created by Riley on 4/10/16.
 */
public class Restaurant
{
    private int identifier;
    private String name;

    // Relationships
    private Set<Menu> menus;

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

    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }
}
