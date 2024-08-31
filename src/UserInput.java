import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;

public class UserInput {

    private Menu menu;

    private JLabel bunOrSauceLabel;
    private JComboBox<String> bunOrDressingCombo;
    private JComboBox<Meat> meatCombo;
    private final DefaultListModel<Object> sauceOrGreensModel = new DefaultListModel<>();
    private JList<Object> sauceOrGreensList;
    private JPanel aCuccumberRadio;
    private ButtonGroup cucumberButtonGroup;
    private ButtonGroup pickleButtonGroup;
    private ButtonGroup tomatoButtonGroup;
    private JCheckBox cheeseCheck;
    private JTextField minPrice;
    private JTextField maxPrice;

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
        filterOption.add(bunOrDressingPanel());
        filterOption.add(sauceOrGreensPanel());
        filterOption.add(picklePanel());
        filterOption.add(tomatoPanel());
        filterOption.add(meatPanel());
        filterOption.add(cheesePanel());
        filterOption.add(pricePanel());
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
        sauceOrGreensModel.addElement("Select an Item");

        // Set all the parameters for the panel
        meal.setLayout(new BoxLayout(meal,BoxLayout.Y_AXIS));
        meal.setBorder(BorderFactory.createTitledBorder("Would you like a boigur or swalid?"));
//        meal.setPreferredSize(new Dimension(400, 100));

        // Create a combobox and populate it with all options from the type enum
        JComboBox<Type> typesOfMeals = new JComboBox<>(Type.values());
        typesOfMeals.setSelectedItem(Type.SELECT);
        typesOfMeals.setPreferredSize(new Dimension(300, 30));
        // add listener
        ActionListener listener = e -> {
            bunOrDressingCombo.setEnabled(!(typesOfMeals.getSelectedItem() == Type.SELECT));
            sauceOrGreensList.setEnabled(!(typesOfMeals.getSelectedItem() == Type.SELECT));
            burgerOrSalad((Type) Objects.requireNonNull(typesOfMeals.getSelectedItem()));

        };

        typesOfMeals.addActionListener(listener);
        // Set all parameters for the combobox

        JPanel typePane = new JPanel();
        typePane.setPreferredSize(new Dimension(300, 50));

        typePane.add(typesOfMeals);

        // Add the combo box to the main panel
        meal.add(Box.createRigidArea(new Dimension(0,20)));
        meal.add(typePane);
        meal.add(Box.createRigidArea(new Dimension(0,20)));

        return meal;
    }

    public JPanel bunOrDressingPanel() {

        JPanel comboAndListPane = new JPanel();
        comboAndListPane.setPreferredSize(new Dimension(300, 200));
        JPanel comboPane = new JPanel();

        aCuccumberRadio = new JPanel();
        aCuccumberRadio.setVisible(false);

        cucumberButtonGroup = new ButtonGroup();
        JLabel cucumberLabel = new JLabel("Cucumber?");
        JRadioButton yes = new JRadioButton("Yes");
        JRadioButton no = new JRadioButton("No");
        JRadioButton neither = new JRadioButton("I don't mind");
        cucumberButtonGroup.add(yes);
        cucumberButtonGroup.add(no);
        cucumberButtonGroup.add(neither);

        JLabel bunOrDressingLabel = new JLabel(" ");
        bunOrDressingCombo = new JComboBox<>();
        bunOrDressingCombo.setPreferredSize(new Dimension(300, 30));
        bunOrDressingCombo.addItem("Select Item");
        bunOrDressingCombo.setEnabled(false);

        aCuccumberRadio.add(cucumberLabel);
        aCuccumberRadio.add(yes);
        aCuccumberRadio.add(no);
        aCuccumberRadio.add(neither);

        comboPane.setLayout(new BoxLayout(comboPane, BoxLayout.Y_AXIS));
        comboPane.add(Box.createRigidArea(new Dimension(0,50)));
        comboPane.add(bunOrDressingLabel);
        comboPane.add(bunOrDressingCombo);
        comboPane.add(Box.createRigidArea(new Dimension(0,20)));
        comboPane.add(aCuccumberRadio);

        comboAndListPane.add(Box.createRigidArea(new Dimension(0,20)));
        comboAndListPane.add(comboPane);
        comboAndListPane.add(Box.createRigidArea(new Dimension(0,20)));

        return comboAndListPane;
    }

    public JPanel sauceOrGreensPanel() {

        JPanel comboAndListPane = new JPanel();

        JPanel listPane = new JPanel();
//        listPane.setPreferredSize(new Dimension(300, 200));

        JLabel sauceOrGreensLabel = new JLabel(" ");
        sauceOrGreensList = new JList<>(sauceOrGreensModel);

        JScrollPane scrollPane = new JScrollPane(sauceOrGreensList);
        scrollPane.setPreferredSize(new Dimension(300, 100));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        sauceOrGreensList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        sauceOrGreensList.setEnabled(false);

        listPane.add(sauceOrGreensLabel);
        listPane.add(scrollPane);

        comboAndListPane.add(Box.createRigidArea(new Dimension(0,20)));
        comboAndListPane.add(listPane);
        comboAndListPane.add(Box.createRigidArea(new Dimension(0,20)));

        return comboAndListPane;
    }

    public JPanel picklePanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(500, 100));

        JPanel radioPanel = new JPanel();

        pickleButtonGroup = new ButtonGroup();
        JLabel pickleLabel = new JLabel("Pickles?");
        JRadioButton yes = new JRadioButton("Yes");
        JRadioButton no = new JRadioButton("No");
        JRadioButton neither = new JRadioButton("I don't mind");
        yes.requestFocusInWindow();
        pickleButtonGroup.add(yes);
        pickleButtonGroup.add(no);
        pickleButtonGroup.add(neither);

        radioPanel.add(pickleLabel);
        radioPanel.add(yes);
        radioPanel.add(no);
        radioPanel.add(neither);

        mainPanel.add(radioPanel);

        return mainPanel;
    }

    public JPanel tomatoPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(500, 100));

        JPanel radioPanel = new JPanel();

        tomatoButtonGroup = new ButtonGroup();
        JLabel pickleLabel = new JLabel("Tomato?");
        JRadioButton yes = new JRadioButton("Yes");
        JRadioButton no = new JRadioButton("No");
        JRadioButton neither = new JRadioButton("I don't mind");
        yes.requestFocusInWindow();
        tomatoButtonGroup.add(yes);
        tomatoButtonGroup.add(no);
        tomatoButtonGroup.add(neither);

        radioPanel.add(pickleLabel);
        radioPanel.add(yes);
        radioPanel.add(no);
        radioPanel.add(neither);

        mainPanel.add(radioPanel);

        return mainPanel;
    }

    public JPanel meatPanel() {
        JPanel mainPanel = new JPanel();

        meatCombo = new JComboBox<>(Meat.values());
        meatCombo.setPreferredSize(new Dimension(300, 40));
        mainPanel.add(meatCombo);

        return mainPanel;
    }

    public JPanel cheesePanel() {
        JPanel mainPanel = new JPanel();

        JLabel cheeseLabel = new JLabel("Cheese?");
        cheeseCheck = new JCheckBox();

        mainPanel.add(cheeseLabel);
        mainPanel.add(cheeseCheck);

        return mainPanel;
    }

    public JPanel pricePanel() {
        JPanel mainPanel = new JPanel();

        JLabel minPriceLabel = new JLabel("Min Price: ");
        minPrice = new JTextField(5);

        JLabel maxPriceLabel = new JLabel("Max Price: ");
        maxPrice = new JTextField(5);

        mainPanel.add(minPriceLabel);
        mainPanel.add(minPrice);
        mainPanel.add(maxPriceLabel);
        mainPanel.add(maxPrice);

        return mainPanel;
    }

    public void burgerOrSalad(Type typeOfFood) {

        if (typeOfFood.equals(Type.BURGER)){
            selectedOption = Type.BURGER;
            bunOrDressingCombo.removeAllItems();
            for (Object s : menu.getAllIngredientTypes(Filter.BUN)) {
                bunOrDressingCombo.addItem(s.toString());
            }

            sauceOrGreensModel.removeAllElements();
            for (Sauce s : Sauce.values()) {
                sauceOrGreensModel.addElement(s);
            }

            aCuccumberRadio.setVisible(false);

        }
        else if (typeOfFood.equals(Type.SALAD)) {
            selectedOption = Type.SALAD;
            bunOrDressingCombo.removeAllItems();
            for (Object s : menu.getAllIngredientTypes(Filter.DRESSING)) {
                bunOrDressingCombo.addItem(s.toString());
            }

            sauceOrGreensModel.removeAllElements();
            for (Object s : menu.getAllIngredientTypes(Filter.LEAFY_GREENS)) {
                sauceOrGreensModel.addElement(s.toString());
            }

            aCuccumberRadio.setVisible(true);
        }
        else {
            bunOrDressingCombo.removeAllItems();
            bunOrDressingCombo.addItem("Select an Item");
            sauceOrGreensModel.removeAllElements();
            sauceOrGreensModel.addElement("Select an Item");
            aCuccumberRadio.setVisible(false);
        }
    }


}
