/**
 * @author -
 * Email -
 * created for COSC120 Assignment 3
 * A Programed solution adding more funtionality and a graphical interface for the good people at the
 * gobbledy geek eatery
 */

import java.util.*;

public class Menu {
    // A Set containing the entire database
    private final Set<MenuItem> menu = new HashSet<>();

    /**
     * Method to add an item to the set that holds the database
     * @param menuItem - a MenuItem object comprised of data from a single line of the database
     */
    public void addItem(MenuItem menuItem){
        this.menu.add(menuItem);
    }

    /**
     * Method that takes in a filter (Attribute from a meal) and returns an ordered set of all the attributes that
     * exist in the database that match that filter
     * @param filter - An Enum variable pointing to an attribute of a MenuItem
     * @return - An Ordered set of all values that exist in the database of the provided Filter
     */
    public Set<Object> getAllIngredientTypes(Filter filter){
        // Create a LinkedHashSet to preserve order and make sure there are no duplicates
        Set<Object> allSubtypes = new LinkedHashSet<>();

        // Iterate over every item in the menu
        for(MenuItem menuItem: menu){

            // For all the items, get only the attribute that is equal to the filter passed in, i.e sauce, bun, cheese
            if(menuItem.getDreamMenuItem().getAllFilters().containsKey(filter)){
                var ingredientTypes = menuItem.getDreamMenuItem().getFilter(filter);

                // Add all the items in the selected Filter if it's a collection, else just add the individual value
                if(ingredientTypes instanceof Collection<?>) allSubtypes.addAll((Collection<?>) ingredientTypes);
                else allSubtypes.add(menuItem.getDreamMenuItem().getFilter(filter));
            }
        }
        // Add the skip option
        allSubtypes.add("I don't mind");
        return allSubtypes;
    }

    /**
     * Method that compares a passed in user menu item against all items in the database to look for matches
     * @param dreamMenuItem - The users created item
     * @return Matching - a list of matching items from the database
     */
    public List<MenuItem> findMatch(DreamMenuItem dreamMenuItem){
        // Create a list to hold all matching items from the database
        List<MenuItem> matching = new ArrayList<>();

        // For every item in the database
        for(MenuItem menuItem: menu){

            // If the items feature's don't match the users dream item, then continue
            if(!menuItem.getDreamMenuItem().matches(dreamMenuItem)) continue;

            // If the price of the item is not within the user defined range, then continue
            if(menuItem.getPrice()<dreamMenuItem.getMinPrice()|| menuItem.getPrice()>dreamMenuItem.getMaxPrice()) continue;

            // Add all items that match the users criteria
            matching.add(menuItem);
        }
        return matching;
    }

}
