/**
 * @author -
 * Email -
 * created for COSC120 Assignment 3
 * A Programed solution adding more funtionality and a graphical interface for the good people at the
 * gobbledy geek eatery
 */

// An enum representing the meat for a menu item
public enum Meat {

    BEEF, CHICKEN, VEGAN, NA;

    /**
     *
     * @return a prettified version of the case that is selected by the user.
     */
    public String toString(){
        return switch (this) {
            case BEEF -> "Beef";
            case CHICKEN -> "Chicken";
            case VEGAN -> "Vegan";
            case NA -> "Any meat will do...";
        };
    }


}
