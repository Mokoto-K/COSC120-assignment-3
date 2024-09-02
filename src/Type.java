/**
 * @author -
 * Email -
 * created for COSC120 Assignment 3
 * A Programed solution adding more funtionality and a graphical interface for the good people at the
 * gobbledy geek eatery
 */

// An Enum representing the types of options a customer can order
public enum Type {

    SELECT, BURGER,SALAD;

    /**
     *
     * @return a prettified version of the case that is selected by the user.
     */
    public String toString(){
        return switch (this) {
            case SELECT -> "Select a Meal";
            case BURGER -> "Burger";
            case SALAD -> "Salad";
        };
    }
}












