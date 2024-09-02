/**
 * @author -
 * Email -
 * created for COSC120 Assignment 3
 * A Programed solution adding more funtionality and a graphical interface for the good people at the
 * gobbledy geek eatery
 */

public enum Meat {

    BEEF, CHICKEN, VEGAN, NA;

    public String toString(){
        return switch (this) {
            case BEEF -> "Beef";
            case CHICKEN -> "Chicken";
            case VEGAN -> "Vegan";
            case NA -> "Any meat will do...";
        };
    }


}
