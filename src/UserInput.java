import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UserInput {

    private Menu menu;

    private JLabel bunOrSauceLabel;
    private JComboBox<String> bunOrSauceCombo;

    private Type selectedOption;

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
        selectedOption = Type.SELECT;
        // Set all the parameters for the panel
        // TODO - set layout
        meal.setBorder(BorderFactory.createTitledBorder("Would you like a boigur or swalid?"));

        // Create a combobox and populate it with all options from the type enum
        JComboBox<Type> typesOfMeals = new JComboBox<>(Type.values());
        typesOfMeals.setSelectedItem(Type.SELECT);
        // add listener
        ActionListener listener = e -> {
            bunOrSauceCombo.setEnabled(!(typesOfMeals.getSelectedItem() == Type.SELECT));
            bunsOrDressing((Type) Objects.requireNonNull(typesOfMeals.getSelectedItem()));
        };

        typesOfMeals.addActionListener(listener);
        // Set all parameters for the combobox
        typesOfMeals.setPreferredSize(new Dimension(200, 20));


        bunOrSauceCombo = new JComboBox<>();
        bunOrSauceCombo.addItem("Select Item");
        bunOrSauceCombo.setEnabled(false);

        // Add the combo box to the main panel
        meal.add(typesOfMeals, BorderLayout.NORTH);
        meal.add(bunOrSauceCombo, BorderLayout.WEST);
//        meal.add(bunsAndDressingPanel(), BorderLayout.WEST);

        return meal;
    }

    public void bunsOrDressing(Type typeOfFood) {

        if (typeOfFood.equals(Type.BURGER)){
            bunOrSauceCombo.removeAllItems();
            for (Object s : menu.getAllIngredientTypes(Filter.BUN)) {
                bunOrSauceCombo.addItem(s.toString());
            }
        }
        else if (typeOfFood.equals(Type.SALAD)) {
            bunOrSauceCombo.removeAllItems();
            for (Object s : menu.getAllIngredientTypes(Filter.DRESSING)) {
                bunOrSauceCombo.addItem(s.toString());
            }
        }
        else {
            bunOrSauceCombo.removeAllItems();
            bunOrSauceCombo.addItem("Select Item");
        }
    }
}
