import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UserInput {

    private Menu menu;

    /**
     * Constructor for the class, takes in a menu object that is used to access the database of
     * items available on the menu
     * @param menu - a menu object used for gaining access to the database.
     */
    public UserInput(Menu menu){
        this.menu = menu;
    }

    /**
     * The main panel aggregator, all panels of the class are added to this panel which handles the
     * layout and aligning of the user input fields for the GUI
     * @return - filterOptions - a panel used in the main window of the GUI
     */
    public JPanel generateWindow() {
        // initialize the main panel
        JPanel filterOption = new JPanel();

        // Set all the parameters for the main panel
        // TODO - set layout
        filterOption.setLayout(new BoxLayout(filterOption,BoxLayout.Y_AXIS));

        // add all other panels to the main panel
        filterOption.add(typePanel());

        // Return the main panel
        return filterOption;
    }

    /**
     * Uses a J combo box to give the user the option of selecting which type of food they would
     * like to search for
     * @return - a panel containing the Types of foods from the database
     */
    public JPanel typePanel() {
        // initialize the panel
        JPanel meal = new JPanel();

        // Set all the parameters for the panel
        // TODO - set layout


        meal.setBorder(BorderFactory.createTitledBorder("Would you like a boigur or swalid?"));

        String[] menuItems = new String[]{Arrays.toString(Type.values())};
        JComboBox<String> typesOfMeals = new JComboBox<>(menuItems);
//        JLabel label = new JLabel("This is my label");
//        label.setPreferredSize(new Dimension(100, 100));



        meal.add(typesOfMeals);



        return meal;
    }
}
