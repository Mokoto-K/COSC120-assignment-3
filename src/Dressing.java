/**
 * @author -
 * Email -
 * created for COSC120 Assignment 3
 * A Programed solution adding more funtionality and a graphical interface for the good people at the
 * gobbledy geek eatery
 */

// An Enum representing Dressings for a salad
public enum Dressing {

    RANCH,FRENCH,ITALIAN,GREEN_GODDESS,NA;

    /**
     *
     * @return a prettified version of the case that is selected by the user.
     */
    public String toString(){
        return switch (this) {
            case RANCH -> "Ranch";
            case FRENCH -> "French";
            case ITALIAN -> "Italian";
            case GREEN_GODDESS -> "Green goddess";
            case NA -> "I don't mind...";
        };
    }

}
