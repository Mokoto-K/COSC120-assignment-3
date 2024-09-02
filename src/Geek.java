/**
 * @author -
 * Email -
 * created for COSC120 Assignment 3
 * A Programed solution adding more funtionality and a graphical interface for the good people at the
 * gobbledy geek eatery
 */
// TODO - turn this into a record
public class Geek {

    private final String name;
    private final long orderNumber;

    public Geek(String name, long orderNumber) {
        this.name = name;
        this.orderNumber = orderNumber;
    }

    public String getName() {
        return name;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

}
