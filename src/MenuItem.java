/**
 * @author -
 * Email -
 * created for COSC120 Assignment 3
 * A Programed solution adding more funtionality and a graphical interface for the good people at the
 * gobbledy geek eatery
 */

import java.text.DecimalFormat;

public class MenuItem {

    //fields
    private final long menuItemIdentifier;
    private final String menuItemName;
    private final String description;
    private final double price;
    private final DreamMenuItem dreamMenuItem;

    //constructor/s
    public MenuItem(long menuItemIdentifier, String menuItemName, double price, String description, DreamMenuItem dreamMenuItem) {
        this.menuItemIdentifier = menuItemIdentifier;
        this.menuItemName = menuItemName;
        this.price = price;
        this.description = description;
        this.dreamMenuItem=dreamMenuItem;
    }

    public MenuItem(DreamMenuItem dreamMenuItem) {
        this.menuItemIdentifier = 0;
        this.menuItemName = "CUSTOM ORDER";
        this.price = -1;
        this.description = "custom - see preferences";
        this.dreamMenuItem=dreamMenuItem;
    }

    //getters
    public long getMenuItemIdentifier() {
        return menuItemIdentifier;
    }
    public String getMenuItemName() {
        return menuItemName;
    }
    public String getDescription() {
        return description;
    }
    public double getPrice() {
        return price;
    }
    public DreamMenuItem getDreamMenuItem(){ return dreamMenuItem;}

    //menu info
    public String getMenuItemInformation(){
        DecimalFormat df = new DecimalFormat("0.00");
        String output = "\n*******************************************";
        if(getMenuItemIdentifier()!=0) output+="\n"+this.getMenuItemName()+" ("+getMenuItemIdentifier()+")"+ "\n"+this.getDescription();
        output+=getDreamMenuItem().getInfo();
        if(price==-1) return output;
        else return output+"\nPrice: $"+df.format(this.getPrice());
    }


}
