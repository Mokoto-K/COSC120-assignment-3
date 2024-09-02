/**
 * @author -
 * Email -
 * created for COSC120 Assignment 3
 * A Programed solution adding more funtionality and a graphical interface for the good people at the
 * gobbledy geek eatery
 */

// An Enum class repreenting all possible options for items to put on a burger or on a salad, used as keys in a map
// to keep track of items in the database
public enum Filter {

    TYPE,BUN,MEAT,CHEESE,PICKLES,CUCUMBER,TOMATO,DRESSING,LEAFY_GREENS,SAUCE_S;

    /**
     *
     * @return a prettified version of the case that is selected by the user.
     */
    public String toString(){
        return switch (this) {
            case TYPE -> "Menu item type";
            case BUN -> "Bun/bread";
            case MEAT -> "Meat";
            case CHEESE -> "Cheese";
            case PICKLES -> "Pickles (gherkins)";
            case CUCUMBER -> "Cucumber (continental)";
            case TOMATO -> "Tomato";
            case DRESSING -> "Salad dressing";
            case LEAFY_GREENS -> "Leafy greens";
            case SAUCE_S -> "Sauces";
        };
    }

}
