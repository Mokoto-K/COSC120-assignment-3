/**
 * @author -
 * Email -
 * created for COSC120 Assignment 3
 * A Programed solution adding more funtionality and a graphical interface for the good people at the
 * gobbledy geek eatery
 */

import java.util.*;

public class Menu {

    private final Set<MenuItem> menu = new HashSet<>();

    public void addItem(MenuItem menuItem){
        this.menu.add(menuItem);
    }

    public Set<Object> getAllIngredientTypes(Filter filter){
        Set<Object> allSubtypes = new LinkedHashSet<>();
        for(MenuItem menuItem: menu){
            if(menuItem.getDreamMenuItem().getAllFilters().containsKey(filter)){
                var ingredientTypes = menuItem.getDreamMenuItem().getFilter(filter);
//                System.out.println(ingredientTypes);
                if(ingredientTypes instanceof Collection<?>) allSubtypes.addAll((Collection<?>) ingredientTypes);
                else allSubtypes.add(menuItem.getDreamMenuItem().getFilter(filter));
            }
        }
        allSubtypes.add("I don't mind");
        return allSubtypes;
    }

    public List<MenuItem> findMatch(DreamMenuItem dreamMenuItem){
        List<MenuItem> matching = new ArrayList<>();
        for(MenuItem menuItem: menu){
//            System.out.println(menuItem.getMenuItemName());
            if(!menuItem.getDreamMenuItem().matches(dreamMenuItem)) continue;
            if(menuItem.getPrice()<dreamMenuItem.getMinPrice()|| menuItem.getPrice()>dreamMenuItem.getMaxPrice()) continue;
            matching.add(menuItem);
        }
        return matching;
    }

}
