/**
 * @author -
 * Email -
 * created for COSC120 Assignment 3
 * A Programed solution adding more funtionality and a graphical interface for the good people at the
 * gobbledy geek eatery
 */

// An Enum class representing choices for Sauce on a burger
public enum Sauce {

    TOMATO, GARLIC, AIOLI, BBQ, CHILLI, RANCH, SPECIAL, NA;

    /**
     *
     * @return a prettified version of the case that is selected by the user.
     */
    public String toString(){
        return switch (this) {
            case TOMATO -> "Tomato";
            case GARLIC -> "Garlic";
            case AIOLI -> "Aioli (vegan friendly)";
            case BBQ -> "BBQ";
            case CHILLI -> "Chilli";
            case RANCH -> "Ranch";
            case SPECIAL -> "Special sauce";
            case NA -> "Any sauce will do...";
        };
    }

}
