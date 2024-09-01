import java.util.*;

public class DreamMenuItem {
    /**
     * Created by Dr Andreas Shepley for COSC120 on 03/07/2023
     */
    private final Map<Filter,Object> filterMap;
    private final float minPrice;
    private final float maxPrice;

    public DreamMenuItem(Map<Filter, Object> filterMap, float minPrice, float maxPrice) {
        this.filterMap=new LinkedHashMap<>(filterMap);
        this.minPrice=minPrice;
        this.maxPrice=maxPrice;
    }
    public DreamMenuItem(Map<Filter, Object> filterMap) {
        this.filterMap=new LinkedHashMap<>(filterMap);
        this.minPrice=-1;
        this.maxPrice=-1;
    }

    public Map<Filter, Object> getAllFilters() {
        return new LinkedHashMap<>(filterMap);
    }
    public Object getFilter(Filter key){return getAllFilters().get(key);}
    public float getMinPrice() {
        return minPrice;
    }
    public float getMaxPrice() {
        return maxPrice;
    }

    public String getInfo(){
        StringBuilder description = new StringBuilder();
        StringBuilder extras = new StringBuilder("\nExtras: ");
        for(Filter key: filterMap.keySet()) {
            if(getFilter(key) instanceof Collection<?>){
                description.append("\n").append(key).append(":");
                for(Object x:((Collection<?>) getFilter(key)).toArray()) description.append("\n").append(" --> ").append(x);
            }
            else if(getFilter(key).equals(true)) extras.append(key).append(", ");
            else if(!getFilter(key).equals(false)) description.append("\n").append(key).append(": ").append(getFilter(key));
        }
        description.append(extras.substring(0,extras.length()-2));
        return description.toString();
    }

    public boolean matches(DreamMenuItem dreamMenuItem){
        // Iterate through all the keys in the users dream fruiting plant map, this ensures we only iterate through
        // values (keys) stored in the users map and check for comparison of only those keys

        for (Filter key : dreamMenuItem.getAllFilters().keySet()) {
            System.out.println(dreamMenuItem.getAllFilters());
            System.out.println(this.getAllFilters());
            // if a plant from the database contains a key that is also in the users dream plant map
            if (this.getAllFilters().containsKey(key)) {

                // If both the database plant and the users dream plant are collection, compare them here
                if (this.getFilter(key) instanceof Collection<?> && dreamMenuItem.getFilter(key) instanceof Collection<?>) {
                    System.out.println("1 " + key + " " + dreamMenuItem.getFilter(key));
                    // Creating a set of objects of the database and dream users plants collection
                    Set<Object> intersect = new HashSet<>((Collection<?>) dreamMenuItem.getFilter(key));
                    // if the set is empty after running retain all, then there were no matching features so we return false
                    intersect.retainAll((Collection<?>) dreamMenuItem.getFilter(key));
                    if (intersect.isEmpty()) return false;
                }
                // Comparing if the database plant is a collection and the users dream plants value is not
                else if (this.getFilter(key) instanceof Collection<?> && !(dreamMenuItem.getFilter(key) instanceof Collection<?>)) {
                    System.out.println("2 " + key + " " + dreamMenuItem.getFilter(key));

                    // return false if the users dream plant attribute isnt in the database plants collection
                    if (!((Collection<?>) this.getFilter(key)).contains(dreamMenuItem.getFilter(key))) {
                        return false;
                    }
                }
                // Comparing if the users dream plants value is a collection but the database plant is not
                else if (dreamMenuItem.getFilter(key) instanceof Collection<?> && !(this.getFilter(key) instanceof Collection<?>)) {
                    System.out.println("3 " + key + " " + dreamMenuItem.getFilter(key));
                    // return false if the database plants attribute is not in the user's plants collection
                    if (!((Collection<?>) dreamMenuItem.getFilter(key)).contains(this.getFilter(key))) {
                        System.out.println("Secrect");
                        return false;
                    }
                }
                // Comparing if the value from the database plant and the users dream plant are single values, not collections
                else if (!this.getFilter(key).equals(dreamMenuItem.getFilter(key))) {
                    // returning false if they don't match
                    System.out.println("4 " + key + " " + dreamMenuItem.getFilter(key));
                    return false;
                }
                // This else statement is a protective measure against there being an error in the database, since we are
                // comparing if the key from a users plant is a key shared by a plant in the database, we should never
                // end up here, but if there is a mistake in the database, and it doesn't contain a key (plant value) that
                // we expect it to have, the top "if" statement will be false and instantly return true without any
                // comparison.
            } else {
                return false;
            }
        }
        // if a plant survives that gauntlet... we add it
        return true;






//        for(Filter key : dreamMenuItem.getAllFilters().keySet()) {
//            if(this.getAllFilters().containsKey(key)){
//                if(getFilter(key) instanceof Collection<?> && dreamMenuItem.getFilter(key) instanceof Collection<?>){
//                    Set<Object> intersect = new HashSet<>((Collection<?>) dreamMenuItem.getFilter(key));
//                    intersect.retainAll((Collection<?>) getFilter(key));
//                    if(intersect.size()==0) return false;
//                }
//                else{
//                    if(!this.getFilter(key).equals(dreamMenuItem.getFilter(key))) return false;
//                }
//            }
//        }
//        return true;
    }



}
