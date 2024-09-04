/**
 * @author -
 * Email -
 * created for COSC120 Assignment 3
 * A Programed solution adding more funtionality and a graphical interface for the good people at the
 * gobbledy geek eatery
 */

import java.text.DecimalFormat;

// A Record class for defining and keeping track of Each Item in the Menu
public record MenuItem(long menuItemIdentifier, String menuItemName, double price, String description, DreamMenuItem dreamMenuItem) {

    /**
     * A to String method for the Menu Item class, this returns a concatenated string of all the information of
     * a menu item if it matches with a users dream menu item.
     * @return String - a concatenation of all details owned by a given menu item.
     */
    public String toString(){
        // Define formatting for price
        DecimalFormat df = new DecimalFormat("0.00");

        // Initialize a string to hold all the items description
        String output = "\n*******************************************";
        if(this.menuItemIdentifier()!=0) {
            output+="\n"+this.menuItemName()+" ("+this.menuItemIdentifier()+")"+ "\n"+this.description();
            output+=this.dreamMenuItem().getInfo();
        }

        // If the price hasnt been set yet, return the string without price attached
        if(price==-1) {
            return output;
        }

        // Otherwise format the price and add it to the string
        else {
            return output+"\nPrice: $"+df.format(this.price());
        }
    }

}
