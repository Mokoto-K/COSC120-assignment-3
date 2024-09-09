/**
 * @author -
 * Email -
 * created for COSC120 Assignment 3
 * A Programed solution adding more funtionality and a graphical interface for the good people at the
 * gobbledy geek eatery
 */

import java.util.*;


// TODO - Remove all the commented out print statements from the matches method once all testing is finished
public class DreamMenuItem {

    // Variables for the class
    private final Map<Filter,Object> filterMap;
    private float minPrice;
    private float maxPrice;

    /**
     * A constructor serving primarily for user input
     * @param filterMap - a map of the users dream menu item
     * @param minPrice the min price a user is willing to pay
     * @param maxPrice the max price a user is willing to pay
     */
    public DreamMenuItem(Map<Filter, Object> filterMap, float minPrice, float maxPrice) {
        this.filterMap=new LinkedHashMap<>(filterMap);
        this.minPrice=minPrice;
        this.maxPrice=maxPrice;
    }

    /**
     * Constructor to specifically handle our hashmap, this will serve as our primary constructor for our database
     * @param filterMap a map containing all menu items from the database and their attributes
     */
    public DreamMenuItem(Map<Filter, Object> filterMap) {
        this.filterMap=new LinkedHashMap<>(filterMap);
    }

    /**
     * Getter for our Map of filters
     * @return a hashmap of all menuItems and there attributes
     */
    public Map<Filter, Object> getAllFilters() {
        return new LinkedHashMap<>(filterMap);
    }

    /**
     *
     * @param key representing a value from the filters enum
     * @return the value from the MenuItem map returned when passing the key in
     */
    public Object getFilter(Filter key){return getAllFilters().get(key);}

    /**
     *
     * @return gets the price of the users max price input
     */
    public float getMinPrice() {
        return minPrice;
    }

    /**
     *
     * @return gets the price of the users min price input
     */
    public float getMaxPrice() {
        return maxPrice;
    }

    /**
     * Iterates through all keys from the classes map and adds their values to a string builder that is returned as
     * the description for any given menuItem.
     * @return String - String builders "to string" output of concatenated description data.
     */
    public String getInfo(){
        // Create a string builder to hold all of an items description
        StringBuilder description = new StringBuilder();
        StringBuilder extras = new StringBuilder("\nExtras: ");

        // Iterate through all keys in the filter map
        for(Filter key: filterMap.keySet()) {
            // If the value behind the key is a collection
            if(getFilter(key) instanceof Collection<?>){

                // Append the key name and iterate through each value of the collection and append it's value
                description.append("\n").append(key).append(":");
                for(Object x:((Collection<?>) getFilter(key)).toArray()) description.append("\n").append(" --> ").append(x);
            }
            // If the value is a boolean, append the key name to the string builder
            else if(getFilter(key).equals(true)) extras.append(key).append(", ");
            // If the value of teh boolean isnt false, append the value to the string builder
            else if(!getFilter(key).equals(false)) description.append("\n").append(key).append(": ").append(getFilter(key));
        }
        // Return the string builder
        description.append(extras.substring(0,extras.length()-2));
        return description.toString();
    }


    /**
     * Adapted from my personal Assignment 2 - from a method of the same name line 110 - 166
     * The comparison method for all menu items. Compares the users dream menu item against every menu item
     * in the database.
     * @param dreamMenuItem the users dream menu item in the form of a map
     * @return true or false depending on if the meal match or not
     */
    public boolean matches(DreamMenuItem dreamMenuItem){
        // Iterate through all the keys in the users dream menu item map, this ensures we only iterate through
        // values (keys) stored in the users map and check for comparison of only those keys

        for (Filter key : dreamMenuItem.getAllFilters().keySet()) {

            // if a menu item from the database contains a key that is also in the users dream menu item map
            if (this.getAllFilters().containsKey(key)) {

                // If both the database menu items and the users dream menu item are collection, compare them here
                if (this.getFilter(key) instanceof Collection<?> && dreamMenuItem.getFilter(key) instanceof Collection<?>) {

                    // Creating a set of objects of the database and dream menu item collection
                    Set<Object> intersect = new HashSet<>((Collection<?>) this.getFilter(key));

                    // if the set is empty after running retain all, then there were no matching features, so we return false
                    intersect.retainAll((Collection<?>) dreamMenuItem.getFilter(key));

                    if (intersect.isEmpty()) {
                        return false;
                    }
                }
                // Comparing if the database menu item is a collection and the users dream menu item value is not
                else if (this.getFilter(key) instanceof Collection<?> && !(dreamMenuItem.getFilter(key) instanceof Collection<?>)) {


                    // return false if the users dream menu item attribute isn't in the database menu item collection
                    if (!((Collection<?>) this.getFilter(key)).contains(dreamMenuItem.getFilter(key))) {
                        return false;
                    }
                }
                // Comparing if the users dream menu item value is a collection but the database menu item is not
                else if (dreamMenuItem.getFilter(key) instanceof Collection<?> && !(this.getFilter(key) instanceof Collection<?>)) {

                    // return false if the database menu item attribute is not in the user's menu item collection
                    if (!((Collection<?>) dreamMenuItem.getFilter(key)).contains(this.getFilter(key))) {

                        return false;
                    }
                }
                // Comparing if the value from the database menu item and the users dream menu item are single values, not collections
                else if (!this.getFilter(key).equals(dreamMenuItem.getFilter(key))) {
                    // returning false if they don't match

                    return false;
                }
                // This else statement is a protective measure against there being an error in the database, since we are
                // comparing if the key from a users menu item is a key shared by a menu item in the database, we should never
                // end up here, but if there is a mistake in the database, and it doesn't contain a key (menu item value) that
                // we expect it to have, the top "if" statement will be false and instantly return true without any
                // comparison.
            } else {
                return false;
            }
        }
        // if the meal survives that gauntlet... we add it
        return true;
    }
}
