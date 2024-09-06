/**
 * @author -
 * Email -
 * created for COSC120 Assignment 3
 * A Programed solution adding more funtionality and a graphical interface for the good people at the
 * gobbledy geek eatery
 */

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserInput {

    // Menu item to get a hold of the database items
    private final Menu menu;

    // All variables for each item in the database
    private static String bun;
    private static Set<Object> sauce;
    private static Dressing dressing;
    private static Set<Object> leafy;
    private static String cucumber;
    private static String pickles;
    private static String tomato;
    private static Meat meat;
    private static boolean cheese;
    private static float minPrice = 0;
    private static float maxPrice = 0;

    // All fields for customer information input
    private static JTextField name;
    private static JTextField email;
    private static JTextField phoneNumber;
    private static JTextArea customMessage;

    // Initialise all main JComponent objects that are used throughout the class
    private static JLabel bunOrDressingLabel;
    private static JComboBox<Object> bunOrDressingCombo;
    private static JComboBox<Meat> meatCombo;
    private final static DefaultListModel<Object> sauceOrGreensModel = new DefaultListModel<>();
    private static JLabel sauceOrGreensLabel;
    private static JList<Object> sauceOrGreensList;
    private static JPanel cucumberRadio;
    private static ButtonGroup cucumberButtonGroup;
    private static JRadioButton cucumberYes;
    private static JRadioButton cucumberNo;
    private static JRadioButton cucumberNeither;

    private static ButtonGroup pickleButtonGroup;
    private static ButtonGroup tomatoButtonGroup;
    private static JCheckBox cheeseCheck;
    private final JLabel feedbackMin = new JLabel(" "); //set to blank to start with
    private final JLabel feedbackMax = new JLabel(" ");
    private final JLabel feedbackName = new JLabel(" ");
    private final JLabel feedbackNumber = new JLabel(" ");

    // A variable to keep track of the selected type of meal, burger, salad
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
        filterOption.setLayout(new BorderLayout());

        // add all other panels to the main panel
        filterOption.add(northPanel(), BorderLayout.NORTH);
        filterOption.add(centerPanel(), BorderLayout.CENTER);
        filterOption.add(southPanel(), BorderLayout.SOUTH);

        // Return the main panel
        return filterOption;
    }

    /**
     * Method that generates the Top 3rd of the main window for the program
     * @return northPanel - Panel containing type of meal, bun or dressing option
     * sauce or greens option, and cucumber option
     */
    public JPanel northPanel() {
        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());

        panel.add(typePanel(), BorderLayout.NORTH);
        panel.add(typeDependantOptionsPanel(), BorderLayout.SOUTH);

        return panel;
    }

    public JPanel centerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.setBorder(BorderFactory.createTitledBorder("Please choose the extras you would like"));

        panel.add(leftPanel(), BorderLayout.WEST);
        panel.add(rightPanel(), BorderLayout.EAST);

        return panel;
    }

    public JPanel southPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(pricePanel(), BorderLayout.NORTH);
        return panel;
    }

    // NORTHERN PANEL CONTENT

    /**
     * Uses a J combo box to give the user the option of selecting which type of food they would
     * like to search for
     * @return - a panel containing the Types of foods from the database
     */
    public JPanel typePanel() {
        // initialize the panel
        JPanel meal = new JPanel();
        meal.setAlignmentX(0);
        // Set the selected type to neither
        selectedOption = Type.SELECT;

        // Set all the parameters for the panel
        meal.setLayout(new BoxLayout(meal,BoxLayout.Y_AXIS));
        meal.setBorder(BorderFactory.createTitledBorder("Please choose between a burger or a salad?"));

        // Create a combobox and populate it with all options from the type enum
        JComboBox<Type> typesOfMeals = new JComboBox<>(Type.values());
        typesOfMeals.setSelectedItem(Type.SELECT);
        typesOfMeals.setPreferredSize(new Dimension(300, 30));

        // Add action listener to listen for user select of type of meal
        ActionListener listener = e -> {
//            System.out.println(selectedOption);
            bunOrDressingCombo.setEnabled(!(typesOfMeals.getSelectedItem() == Type.SELECT));
            sauceOrGreensList.setEnabled(!(typesOfMeals.getSelectedItem() == Type.SELECT));

            // Call the burger Or salad method sending the users choice to initialize the rest of the selection option
            burgerOrSalad((Type) Objects.requireNonNull(typesOfMeals.getSelectedItem()));
        };

        // Add the listener to the combo box
        typesOfMeals.addActionListener(listener);

        // Create a panel to hold the combo box
        JPanel typePane = new JPanel();
//        typePane.setPreferredSize(new Dimension(300, 50));
        typePane.setAlignmentX(0);
        // Add the combo box to the panel
        typePane.add(typesOfMeals);

        typesOfMeals.setAlignmentX(0);

        // Add the combo box panel to the main panel
        meal.add(Box.createRigidArea(new Dimension(0,20)));
        meal.add(typePane);
        meal.add(Box.createRigidArea(new Dimension(0,20)));

        return meal;
    }

    public JPanel typeDependantOptionsPanel(){
        JPanel panel = new JPanel();
        panel.setAlignmentX(0);

        panel.add(bunOrDressingPanel());
        panel.add(Box.createRigidArea(new Dimension(50,0)));
        panel.add(sauceOrGreensPanel());

        return panel;
    }

    /**
     * Method for returning a panel that contains either the bun options or the dressing and cucumber options
     * depending on what meal the user has chosen, also deals with returning the chosen options for comparison
     * @return - A panel containing the dressing/bun and cucumber options
     */
    public JPanel bunOrDressingPanel() {

        // Create the main panel to hold all the components
        JPanel mainPanel = new JPanel();

        // Set it's preferred size
//        mainPanel.setPreferredSize(new Dimension(400, 200));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setAlignmentX(0);
        // Create a panel to hold the
        JPanel comboPane = new JPanel();
        comboPane.setLayout(new BorderLayout());
        comboPane.setAlignmentX(0);
        // Create an information label to let the customer know what they are selecting
        bunOrDressingLabel = new JLabel("Select a type to reveal options");
        bunOrDressingLabel.setAlignmentX(0);

        // Create a combo box to hold all the buns and dressings depending on the customers choice of type of meal
        bunOrDressingCombo = new JComboBox<>();
//        bunOrDressingCombo.setPreferredSize(new Dimension(300, 30));
        bunOrDressingCombo.setAlignmentX(0);
        // Add the default item and set it to be disables
        bunOrDressingCombo.addItem("Select Item");
        bunOrDressingCombo.setEnabled(false);


        // Add an item listener to help pass which option was selected for sauce or dressing
        bunOrDressingCombo.addItemListener(e -> {

            // If something was selected, check what it was
            if (e.getStateChange() == ItemEvent.SELECTED) {
//                System.out.println(bun);
                // If it was a burger, set all the defaults to burger related options
                if (selectedOption == Type.BURGER) {

                    // Controls the customers input to be sent to the MenuSearcher class
                    bun = (String) bunOrDressingCombo.getSelectedItem();
                    assert bun != null;
                    if (bun.equals("I don't mind")) {
                        bun = "NA";
                    }
                }
                // If it was a salad, send all salad information to the MenuSearcher class
                else if (selectedOption == Type.SALAD){

                    if (bunOrDressingCombo.getSelectedItem().toString().equals("I don't mind...")) {
                        dressing = Dressing.NA;

                    } else {
                        // Parsing the values from the dressings enum
                        dressing = Dressing.valueOf(bunOrDressingCombo.getSelectedItem().toString().toUpperCase().replace(" ", "_"));
                    }
                }
            }
        });

        comboPane.add(bunOrDressingLabel, BorderLayout.NORTH);
        comboPane.add(bunOrDressingCombo, BorderLayout.SOUTH);
        // Finally add the combo pane to the main panel to be returned
        mainPanel.add(Box.createRigidArea(new Dimension(0,100)));
        mainPanel.add(comboPane, BorderLayout.NORTH);
        mainPanel.add(Box.createRigidArea(new Dimension(0,10)));
        mainPanel.add(cucumberPanel(), BorderLayout.SOUTH);
        mainPanel.add(Box.createRigidArea(new Dimension(0,10)));

        return mainPanel;
    }

    /**
     * Method for creating a Panel containing a list area and assigning its contents to the MenuSearcher
     * class for comparison. The list will change content depending on the users choice of selected meal
     * @return - A Panel containing a List area
     */
    public JPanel sauceOrGreensPanel() {

        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setAlignmentX(0);
        // Create the panel to hold the List area
//        JPanel listPane = new JPanel();

        // Create the label for the list
        sauceOrGreensLabel = new JLabel("Select a type to reveal options");
        sauceOrGreensLabel.setAlignmentX(0);
        // Remove any items in the list area and add a default item to view in the JListArea
        sauceOrGreensModel.removeAllElements();
        sauceOrGreensModel.addElement("Select an Item");

        // Initialize the JList with the predefined default list model
        sauceOrGreensList = new JList<>(sauceOrGreensModel);
        sauceOrGreensList.setAlignmentX(0);
        // Create a scroll pane passing it the list and setting its basic settings
        JScrollPane scrollPane = new JScrollPane(sauceOrGreensList);
        scrollPane.setPreferredSize(new Dimension(300, 100));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Allow multiple selections
        sauceOrGreensList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // Set it as disabled by default, will only be enabled if the customer selections a meal option
        sauceOrGreensList.setEnabled(false);

        // Create a listener to focus on the selections in the list, if the burger type was selected by the user
        // set the sauce variable to a set of selected items, otherwise set it to the same for leafy greens
        ListSelectionListener listener = e -> {
            // TODO - Might have to specifically address the select case, instead of burger or else
            if (selectedOption == Type.BURGER) {
                sauce = new HashSet<>(sauceOrGreensList.getSelectedValuesList());
            }
            else {
                leafy = new HashSet<>(sauceOrGreensList.getSelectedValuesList());
            }
        };

        // Add the listener to the list
        sauceOrGreensList.addListSelectionListener(listener);

        // Add the Components to the list panel
//        listPane.add(sauceOrGreensLabel);
//        listPane.add(scrollPane);

        // Add the list panel to the main panel as well as some padding
        mainPanel.add(Box.createRigidArea(new Dimension(0,10)));
        mainPanel.add(sauceOrGreensLabel, BorderLayout.NORTH);
        mainPanel.add(Box.createRigidArea(new Dimension(0,10)));
        mainPanel.add(scrollPane, BorderLayout.SOUTH);
        mainPanel.add(Box.createRigidArea(new Dimension(0,10)));

        return mainPanel;
    }

    public JPanel cucumberPanel() {
        // Create a panel for the cucumber option
        cucumberRadio = new JPanel();

        // Create a button group to hold the cucumber options
        cucumberButtonGroup = new ButtonGroup();
        JLabel cucumberLabel = new JLabel("Cucumber?");

        // Create the radio buttons with all the options we want to give the customer
        cucumberYes = new JRadioButton("Yes");
        cucumberNo = new JRadioButton("No");
        cucumberNeither = new JRadioButton("I don't mind");

        cucumberYes.setEnabled(false);
        cucumberNo.setEnabled(false);
        cucumberNeither.setEnabled(false);

        // Add the options to the radiobutton group
        cucumberButtonGroup.add(cucumberYes);
        cucumberButtonGroup.add(cucumberNo);
        cucumberButtonGroup.add(cucumberNeither);
        // Set the default selection
        cucumberButtonGroup.setSelected(cucumberNeither.getModel(), true);

        // Set the output of the action commands
        cucumberYes.setActionCommand("yes");
        cucumberNo.setActionCommand("no");
        cucumberNeither.setActionCommand("I don't mind");

        // Handles passing the users selection to the MenuSearcher class
        cucumber = cucumberButtonGroup.getSelection().getActionCommand();
        ActionListener listener = e -> {
            cucumber = cucumberButtonGroup.getSelection().getActionCommand();
        };

        // Add the listener to the radio buttons
        cucumberYes.addActionListener(listener);
        cucumberNo.addActionListener(listener);
        cucumberNeither.addActionListener(listener);

        // Add the radio buttons to the radio panel
        cucumberRadio.add(cucumberLabel);
        cucumberRadio.add(cucumberYes);
        cucumberRadio.add(cucumberNo);
        cucumberRadio.add(cucumberNeither);

        return cucumberRadio;
    }

    /**
     * Method that sets which ingredients are available to choose from relating to menu specific items. Sets a combo
     * box and a list area with a user type selections options, it also takes care of resetting and switch the values
     * if the user changes which meal they want to order.
     * @param typeOfFood - Enum value of the users selection
     */
    public void burgerOrSalad(Type typeOfFood) {

        // If the user selects Burger for their meal
        if (typeOfFood.equals(Type.BURGER)){

            // Set the selection Type to burger
            selectedOption = Type.BURGER;

            // Set the bun and dressing label to the bun option
            bunOrDressingLabel.setText("Select which Bun you'd like");

            // Delete all items in the combo box related to the bun's and dressing's
            bunOrDressingCombo.removeAllItems();

            // For every bun in the database
            for (Object bun : menu.getAllIngredientTypes(Filter.BUN)) {
                // Add the bun to the combo box
                bunOrDressingCombo.addItem(bun.toString());
            }
            // Select the default item in the combo box to the first item
            bunOrDressingCombo.setSelectedItem(bunOrDressingCombo.getItemAt(0));

            sauceOrGreensLabel.setText("Select Which sauce(s) you'd like to add");

            // Delete all the elements in the sauceOrGreensModel (this will be a list of either sauce of leafy greens)
            sauceOrGreensModel.removeAllElements();

            // For every sauce in the Sauce enum
            for (Sauce s : Sauce.values()) {
                // Add each sauce to the List model
                sauceOrGreensModel.addElement(s);
            }

            sauceOrGreensList.setSelectedIndex(0);

            // Set the cucumber radio button to invisible
            cucumberYes.setEnabled(false);
            cucumberNo.setEnabled(false);
            cucumberNeither.setEnabled(false);

        }
        // If the user selects salad
        else if (typeOfFood.equals(Type.SALAD)) {

            // Set the type selection to Salad
            selectedOption = Type.SALAD;

            // Set the bun and dressing label to the salad option
            bunOrDressingLabel.setText("Select which Dressing you'd like");

            // Delete all items in the combo box related to the bun's and dressing's
            bunOrDressingCombo.removeAllItems();


            // For every Dressing in the Dressing Enum
            for (Object dressing : Dressing.values()) {
                // Add the dressing to the combo Box
                bunOrDressingCombo.addItem(dressing.toString());
            }

            // Select the default item in the combo box to the first item
            bunOrDressingCombo.setSelectedItem(bunOrDressingCombo.getItemAt(0));

            // Set the default value in the combo box
//            bunOrDressingCombo.setSelectedItem(Dressing.NA);

            sauceOrGreensLabel.setText("Select which Leafy Green(s) you'd like to add");

            // Delete all the elements in the sauceOrGreensModel (this will be a list of either sauce of leafy greens)
            sauceOrGreensModel.removeAllElements();

            // For every leaf in the database
            for (Object leaf : menu.getAllIngredientTypes(Filter.LEAFY_GREENS)) {
                // Add the leaf to the model list
                sauceOrGreensModel.addElement(leaf.toString());
            }

            sauceOrGreensList.setSelectedIndex(0);

            // Set the cucumber radio to visible
            cucumberYes.setEnabled(true);
            cucumberNo.setEnabled(true);
            cucumberNeither.setEnabled(true);
        }
        // If neither salad nor burger is selected
        else {

            // Set the type selection to Salad
            selectedOption = Type.SELECT;

            // Reset the bun and dressing label to empty
            bunOrDressingLabel.setText("Select a type to reveal options");
            sauceOrGreensLabel.setText("Select a type to reveal options");

            // Remove all options from the combo box and add the default option
            bunOrDressingCombo.removeAllItems();
            bunOrDressingCombo.addItem("Select an Item");

            // Delete all options from the List area and add the default option
            sauceOrGreensModel.removeAllElements();
            sauceOrGreensModel.addElement("Select an Item");

            // Set the cucumber radio to invisible
            cucumberYes.setEnabled(false);
            cucumberNo.setEnabled(false);
            cucumberNeither.setEnabled(false);
        }
    }

    // CENTER PANEL CONTENT

    public JPanel leftPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setAlignmentX(0);

        panel.add(picklePanel(), BorderLayout.NORTH);
        panel.add(tomatoPanel(), BorderLayout.WEST);

        return panel;

    }

    public JPanel rightPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setAlignmentX(0);

        panel.add(meatPanel(), BorderLayout.NORTH);
        panel.add(cheesePanel(), BorderLayout.WEST);
//
        return panel;

    }

    /**
     * Method that creates a set of radio buttons and returns them on a panel
     * @return a panel including the radio buttons for selection of pickles
     */
    public JPanel picklePanel() {
        // Create the main panel
        JPanel mainPanel = new JPanel();
//        mainPanel.setPreferredSize(new Dimension(500, 50));
        mainPanel.setAlignmentX(0);

        // Create a panel to hold the radio buttons
        JPanel radioPanel = new JPanel();
        radioPanel.setAlignmentX(0);
        // Assign the pickle button group to a button group and add all of the options to the button group
        pickleButtonGroup = new ButtonGroup();
        JLabel pickleLabel = new JLabel("Pickles? ");
        JRadioButton yes = new JRadioButton("yes");
        JRadioButton no = new JRadioButton("no");
        JRadioButton neither = new JRadioButton("I don't mind");

        pickleLabel.setAlignmentX(0);
        yes.setAlignmentX(0);
        no.setAlignmentX(0);
        neither.setAlignmentX(0);

        yes.requestFocusInWindow();
        pickleButtonGroup.add(yes);
        pickleButtonGroup.add(no);
        pickleButtonGroup.add(neither);
        pickleButtonGroup.setSelected(neither.getModel(), true);

        // Add the options to the radio panel
        radioPanel.add(pickleLabel);
        radioPanel.add(yes);
        radioPanel.add(no);
        radioPanel.add(neither);

        // Set the action commands for the buttons
        yes.setActionCommand("yes");
        no.setActionCommand("no");
        neither.setActionCommand("I don't mind");

        // Set the default value
        pickles = pickleButtonGroup.getSelection().getActionCommand();

        // Create an action listener to list for changes from the customer
        ActionListener listenForPickles = e -> {
            pickles = pickleButtonGroup.getSelection().getActionCommand();
        };

        // Add the listener to the buttons
        yes.addActionListener(listenForPickles);
        no.addActionListener(listenForPickles);
        neither.addActionListener(listenForPickles);

        // Add the radio panel to the main panel
        mainPanel.add(radioPanel);

        return mainPanel;
    }

    /**
     * Method that creates a set of radio buttons and returns them on a panel
     * @return a panel including the radio buttons for selection of tomatoes
     */
    public JPanel tomatoPanel() {
        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setAlignmentX(0);
//        mainPanel.setPreferredSize(new Dimension(500, 50));

        // Create a panel to hold the radio buttons
        JPanel radioPanel = new JPanel();
        radioPanel.setAlignmentX(0);
        // Assign the tomato button group to a button group and add all the options to the button group
        tomatoButtonGroup = new ButtonGroup();
        JLabel tomatoLabel = new JLabel("Tomato?");
        JRadioButton yes = new JRadioButton("yes");
        JRadioButton no = new JRadioButton("no");
        JRadioButton neither = new JRadioButton("I don't mind");

        tomatoLabel.setAlignmentX(0);
        yes.setAlignmentX(0);
        no.setAlignmentX(0);
        neither.setAlignmentX(0);

        yes.requestFocusInWindow();
        tomatoButtonGroup.add(yes);
        tomatoButtonGroup.add(no);
        tomatoButtonGroup.add(neither);
        tomatoButtonGroup.setSelected(neither.getModel(), true);


        // Set the action commands for the buttons
        yes.setActionCommand("yes");
        no.setActionCommand("no");
        neither.setActionCommand("I don't mind");

        // Set the default value
        tomato = tomatoButtonGroup.getSelection().getActionCommand();

        // Create an action listener to list for changes from the customer
        ActionListener theTomatoIsTalking = e -> {

            tomato = tomatoButtonGroup.getSelection().getActionCommand();
        };

        // Add the listener to the buttons
        yes.addActionListener(theTomatoIsTalking);
        no.addActionListener(theTomatoIsTalking);
        neither.addActionListener(theTomatoIsTalking);

        // Add the buttons to the radio panel
        radioPanel.add(tomatoLabel);
        radioPanel.add(yes);
        radioPanel.add(no);
        radioPanel.add(neither);

        // Add the radio panel to the main panel
        mainPanel.add(radioPanel);

        return mainPanel;
    }

    /**
     * Method for creating a J combo Box full of the values from the Meat enum
     * @return JCombo box  comprised of Meat enum options
     */
    public JPanel meatPanel() {
        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setAlignmentX(0);

        JLabel meatLabel = new JLabel("Meat ");
        meatLabel.setAlignmentX(0);
        // Initialize the combo box to hold all the Meat enum values
        meatCombo = new JComboBox<>(Meat.values());
        meatCombo.setAlignmentX(0);
        // Set the size and default option
//        meatCombo.setPreferredSize(new Dimension(300, 40));
        meatCombo.setSelectedItem(Meat.BEEF);
        meat = (Meat) meatCombo.getSelectedItem();

        // Add an listener to listen for the users selection
        meatCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                meat = (Meat) meatCombo.getSelectedItem();

            }
        });

        // Add the combo box
        mainPanel.add(meatLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(10,0)));
        mainPanel.add(meatCombo);

        return mainPanel;
    }

    /**
     * Method that returns a panel containing a check box for the cheese selection
     * @return - A panel for the cheese selection
     */
    public JPanel cheesePanel() {
        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setAlignmentX(0);

        // Create the label for the cheese
        JLabel cheeseLabel = new JLabel("Cheese?");
        cheeseLabel.setAlignmentX(0);

        // Assign the cheese check box to a new check box
        cheeseCheck = new JCheckBox();
        cheeseCheck.setAlignmentX(0);
        // Listen for the users selection
        ChangeListener Nacholistener = e -> cheese = cheeseCheck.isSelected();

        // Add the listener
        cheeseCheck.addChangeListener(Nacholistener);

        // Add the items to the main panel
        mainPanel.add(cheeseLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(10,0)));
        mainPanel.add(cheeseCheck);

        return mainPanel;
    }

    // SOUTH PANEL CONTENT

    /**
     * Adapted from lecture 10 SearchView.java - getUserInputAgeRange lines - 290 - 363
     * Method that returns a panel of the users price input range. It uses a document listener to
     * inform the user if their input is correct and prompts them with how to fix it .
     * @return - A Panel containing all price related components.
     */
    public JPanel pricePanel(){
        // Create the labels for the min and max price fields
        JLabel minLabel = new JLabel("Min. Price");
        JLabel maxLabel = new JLabel("Max. Price");

        // Create the Text fields to hold the max and min prices
        JTextField min = new JTextField(12);
        JTextField max = new JTextField(12);

        // Initialize the min and max fields
        min.setText(String.valueOf(minPrice));
        max.setText(String.valueOf(maxPrice));

        // Controls the font and style of the feedback message
        feedbackMin.setFont(new Font("", Font. ITALIC, 10));
        feedbackMin.setForeground(Color.RED);
        feedbackMin.setText(" ");
        feedbackMax.setFont(new Font("", Font. ITALIC, 10));
        feedbackMax.setForeground(Color.RED);

        // Create a document listener for the min price, this will monitor the text field input for the min price
        // and update the feedback label with instructions for the user.
        min.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // If the check max method returns false, request user addresses the invalid input
                if(!checkMin(min)) min.requestFocus();

                // Check the max after the min has been updated
                checkMax(max);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                // removing and inserting should be subjected to the same checks
                if(!checkMin(min))min.requestFocus();
                // Check the max after the min has been updated
                checkMax(max);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {} //NA
        });

        // Create a document listener for the max price, this will monitor the text field input for the max price
        // and update the feedback label with instructions for the user.
        max.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // If the check max method returns false, request user addresses the invalid input
                if(!checkMax(max)) max.requestFocusInWindow();
                // Check the min after the max has been updated
                checkMin(min);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if(!checkMax(max))max.requestFocusInWindow();
                // Check the min after the max has been updated
                checkMin(min);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        // Create panels to hold the prices and the feedback components
        JPanel pricePanel = new JPanel();
        JPanel feedBackPanel = new JPanel();
        feedBackPanel.setLayout(new BoxLayout(feedBackPanel, BoxLayout.Y_AXIS));
        feedBackPanel.setPreferredSize(new Dimension(600, 30));

        // Add the min price components to the price panel
        pricePanel.add(minLabel);
        pricePanel.add(Box.createRigidArea(new Dimension(10,0)));
        pricePanel.add(min);
        pricePanel.add(Box.createRigidArea(new Dimension(10,0)));

        // ADD the max price components to the price panel
        pricePanel.add(maxLabel);
        pricePanel.add(Box.createRigidArea(new Dimension(10,0)));
        pricePanel.add(max);

        // Add the feedback to the feedback panel
        feedBackPanel.add(feedbackMin);
        feedBackPanel.add(feedbackMax);

        // Create the main panel to hold all components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Add padding and add the price panel and feedback labels
        mainPanel.add(Box.createRigidArea(new Dimension(0,10)));
        mainPanel.add(pricePanel);
        mainPanel.add(feedBackPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0,10)));

        return mainPanel;
    }

    /**
     * Adapted from lecture 10 SearchView.java, checkMin lines 372 - 391: Besides change ints to floats, Only variables
     * and comments were changed

     * Validates user input for the min price range
     * @param minEntry the JTextField used to enter the minimum price
     * @return true if valid price, false if invalid
     */
    private boolean checkMin(JTextField minEntry){
        // Initialise the feedback string to an empty string
        feedbackMin.setText(" ");

        // Try to parse the users input and set the feedback text field to help them make a better input choice
        try{
            float tempMin = Float.parseFloat(minEntry.getText());
            if(tempMin < -1 || tempMin>maxPrice) {
                feedbackMin.setText("Min price must be >= "+ -1 +" and <= "+maxPrice+". Defaulting to "+minPrice+" - "+maxPrice+".");
                minEntry.selectAll();
                return false;
            }else {
                minPrice=tempMin;
                feedbackMin.setText("");
                return true;
            }
            // If they don't enter a number, let them know the price will default
        }catch (NumberFormatException n){
            feedbackMin.setText("Please enter a valid number for min price. Defaulting to "+minPrice+" - "+maxPrice+".");
            minEntry.selectAll();
            return false;
        }
    }

    /**
     * Adapted from lecture 10 SearchView.java  checkMin lines 392 - 415: Besides change ints to floats, Only variables
     * and comments were changed

     * validates user input for the max price range
     * @param maxEntry the JTextField used to enter the maximum price
     * @return true if valid price, false if invalid
     */
    private boolean checkMax(JTextField maxEntry){
        // Initialise the feedback string to an empty string
        feedbackMax.setText(" ");

        // Try to parse the users input and set the feedback text field to help them make a better input choice
        try{
            float tempMax = Float.parseFloat(maxEntry.getText());
            if(tempMax < minPrice) {
                feedbackMax.setText("Max price must be >= min price. Defaulting to "+minPrice+" - "+maxPrice+".");
                maxEntry.selectAll();
                return false;
            }else {
                maxPrice = tempMax;
                feedbackMax.setText("");
                return true;
            }
            // If they don't enter a number, let them know the price will default
        }catch (NumberFormatException n){
            feedbackMax.setText("Please enter a valid number for max price. Defaulting to "+minPrice+" - "+maxPrice+".");
            maxEntry.selectAll();
            return false;
        }
    }

    // Methods concerning View 3 of the Program

    /**
     * Method for creating the order form for the 3rd view of the program
     * @return userInputPanel - A panel containing all the user input fields for the customer to enter their
     * information into.
     */
    public JPanel orderForm(){
        // Create the labels for the text fields
        JLabel enterName = new JLabel(" Full name (First and Last)");
        enterName.setAlignmentX(0);
        JLabel enterPhoneNumber = new JLabel(" Phone number");
        enterPhoneNumber.setAlignmentX(0);
        JLabel enterMessage = new JLabel("Any additional information?");
        enterMessage.setAlignmentX(0);

        // Create the text fields for the user information variables
        name = new JTextField(22);
        name.setAlignmentX(0);
        phoneNumber = new JTextField(22);
        phoneNumber.setAlignmentX(0);
        customMessage = new JTextArea(6,16);

        // Align the feedback labels
        feedbackName.setAlignmentX(0);
        feedbackNumber.setAlignmentX(0);

        // Create a Doc listener for the name textfield to let the customer know what they need to do to
        // enter the correct information
        name.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Run a check to see if the name field has the correct input, update the feedback is it isnt
                if(checkName(name)) name.requestFocus();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                // Run a check to see if the name field has the correct input, remove the feedback is it is
                if(checkName(name))name.requestFocus();
            }
            // Not used, but implemented
            @Override
            public void changedUpdate(DocumentEvent e) {}
        });

        // Create a Doc listener for the phoneNumber textfield to let the customer know what they need to do to
        // enter the correct information
        phoneNumber.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                // Run a check to see if the phoneNumber field has the correct input, update the feedback is it isnt
                if(checkNumber(phoneNumber)) phoneNumber.requestFocusInWindow();

            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                // Run a check to see if the phone number field has the correct input, remove the feedback is it is
                if(checkNumber(phoneNumber))phoneNumber.requestFocusInWindow();

            }
            @Override
            // Not used, but implemented
            public void changedUpdate(DocumentEvent e) {
            }
        });

        // ScrollPane to hold the custom message for the order
        JScrollPane jScrollPane = new JScrollPane(customMessage);
        jScrollPane.setAlignmentX(0);
        jScrollPane.setPreferredSize(new Dimension(300, 200));

        // Create a panel to hold all name elements
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS));
        namePanel.setPreferredSize(new Dimension(300, 60));
        namePanel.add(enterName);
        namePanel.add(name);
        namePanel.add(feedbackName);

        // Create a panel to hold all phone number elements
        JPanel numberPanel = new JPanel();
        numberPanel.setLayout(new BoxLayout(numberPanel, BoxLayout.Y_AXIS));
        numberPanel.setPreferredSize(new Dimension(300, 60));
        numberPanel.add(enterPhoneNumber);
        numberPanel.add(phoneNumber);
        numberPanel.add(feedbackNumber);

        // Create a panel to hold all custom message elements
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setPreferredSize(new Dimension(300, 200));
        messagePanel.add(enterMessage);
        messagePanel.add(jScrollPane);

        // Create a panel to hold all the JComponents
        JPanel userInputPanel = new JPanel();

        // Set all parameters of the panel
        userInputPanel.setLayout(new BoxLayout(userInputPanel,BoxLayout.Y_AXIS));
        userInputPanel.setAlignmentX(0);

        // Add all elements to the main panel
        userInputPanel.add(Box.createRigidArea(new Dimension(10,0)));
        userInputPanel.add(Box.createRigidArea(new Dimension(0,10)));
        userInputPanel.add(namePanel);
        userInputPanel.add(Box.createRigidArea(new Dimension(0,10)));
        userInputPanel.add(numberPanel);
        userInputPanel.add(Box.createRigidArea(new Dimension(0,10)));
        userInputPanel.add(messagePanel);

        return userInputPanel;
    }

    /**
     * Method for controlling the feedback for the name text input field
     * @param nameField the JTextField containing the users name
     * @return true or false depending on if the user input is correct or not
     */
    private boolean checkName(JTextField nameField){
        // If the name input by the user meets the correct format inform the user
        if(isValidFullName(name.getText())) {
            feedbackName.setText("Correct Input detected");
            feedbackName.setFont(new Font("", Font. ITALIC, 10));
            feedbackName.setForeground(Color.BLUE);
            return false;
            // If the input is incorrect, hint them to the correct response
        }else {
            feedbackName.setText("Capitalized names, A-Z only");
            feedbackName.setFont(new Font("", Font. ITALIC, 10));
            feedbackName.setForeground(Color.RED);
            nameField.selectAll();
            return true;
        }
    }

    /**
     * Method for controlling the feedback for the phone number text input field
     * @param numberField the JTextField containing the users phone number
     * @return true or false depending on if the user input is correct or not
     */
    private boolean checkNumber(JTextField numberField){
         // If the phone number input by the user meets the correct format inform the user
        if(isValidPhoneNumber(phoneNumber.getText())) {
            feedbackNumber.setText("Correct Input detected");
            feedbackNumber.setFont(new Font("", Font. ITALIC, 10));
            feedbackNumber.setForeground(Color.BLUE);
            return false;
         // If the input is incorrect, hint them to the correct response
        }else {
            feedbackNumber.setText("10 digit number starting with 04");
            feedbackNumber.setFont(new Font("", Font. ITALIC, 10));
            feedbackNumber.setForeground(Color.RED);
            numberField.selectAll();
            return true;
        }
    }

    /**
     * Adapted from my Assignment 2 - ItemSearcher.java lines 505 - 515
     * Compares a given string against a predetermined sequence of charters to determine if
     * customer input is correct. In this case the format of the users first and last name
     * @param fullName - User input of their first and last name
     * @return boolean True of False whether the input matched the required format
     */
    public boolean isValidFullName(String fullName) {

        Pattern pattern = Pattern.compile("^([A-Z][a-z '.-]*(\\s))+[A-Z][a-z '.-]*$");

        // Match the users input against the required format
        Matcher matcher = pattern.matcher(fullName);

        // Return the result
        return matcher.matches();
    }

    /**
     * * Adapted from my Assignment 2 - ItemSearcher.java lines 525 - 545
     * Compares a given string against a predetermined sequence of charters to determine if
     * customer input is correct. In this case the format of a phone number
     * @param phoneNumber - customer phone number asked to be input
     * @return boolean True of False whether the input matched the required format
     */
    public boolean isValidPhoneNumber(String phoneNumber) {
        // Create a pattern object containing the required format
        // is 10 digits starting with 04
        Pattern pattern = Pattern.compile("^04\\d{8}$");

        // Match the users input against the required format
        Matcher matcher = pattern.matcher(phoneNumber);

        // Return the result
        return matcher.matches();
    }

    // Getters

    /**
     * Gets the bun selected
     * @return The selected Bun
     */
    public String getBun() {
        return bun;
    }

    /**
     * Gets the sauce selected
     * @return The selected sauce
     */
    public Set<Object> getSauce() {
        return sauce;
    }

    /**
     * Gets the Leafy greens selected
     * @return The selected leafy greens
     */
    public Set<Object> getLeafy() {
        return leafy;
    }

    /**
     * Gets the dressing selected
     * @return The selected dressing
     */
    public Dressing getDressing() {
        return dressing;
    }

    /**
     * Gets the cucumber selected
     * @return The selected cucumber
     */
    public String getCucumber() {
        return cucumber;
    }

    /**
     * Gets the pickles selected
     * @return The selected pickles option
     */
    public String getPickles() {
        return pickles;
    }

    /**
     * Gets the tomato selected
     * @return The selected tomato option
     */
    public String getTomato() {
        return tomato;
    }

    /**
     * Gets the meat selected
     * @return The selected meat
     */
    public Meat getMeat() {
        return meat;
    }

    /**
     * Gets the cheese selected
     * @return The selected cheeese option
     */
    public boolean getCheese() {
        return cheese;
    }

    /**
     * Gets the min price
     * @return The min price
     */
    public float getMinPrice() {
        return minPrice;
    }

    /**
     * Gets the mac price
     * @return The max price
     */
    public float getMaxPrice() {
        return maxPrice;
    }

    /**
     * Gets the users name
     * @return The users name
     */
    public String getName() {
        return name.getText();
    }

    /**
     * Gets the user phone number
     * @return The users phone number
     */
    public String getPhoneNumber() {
        return phoneNumber.getText();
    }

    /**
     * Gets the custom message
     * @return The message
     */
    public String getMessage() {
        return customMessage.getText();
    }

    /**
     * Gets the selected item type
     * @return The selected item type
     */
    public Type getSelectedOption() {
        return selectedOption;
    }
}








//
//// Menu item to get a hold of the database items
//private final Menu menu;
//
//// All variables for each item in the database
//private static String bun;
//private static Set<Object> sauce;
//private static Dressing dressing;
//private static Set<Object> leafy;
//private static String cucumber;
//private static String pickles;
//private static String tomato;
//private static Meat meat;
//private static boolean cheese;
//private static float minPrice = 0;
//private static float maxPrice = 0;
//
//// All fields for customer information input
//private static JTextField name;
//private static JTextField email;
//private static JTextField phoneNumber;
//private static JTextArea customMessage;
//
//// Initialise all main JComponent objects that are used throughout the class
//private static JLabel bunOrDressingLabel;
//private static JComboBox<Object> bunOrDressingCombo;
//private static JComboBox<Meat> meatCombo;
//private final static DefaultListModel<Object> sauceOrGreensModel = new DefaultListModel<>();
//private static JList<Object> sauceOrGreensList;
//private static JPanel cucumberRadio;
//private static ButtonGroup cucumberButtonGroup;
//private static ButtonGroup pickleButtonGroup;
//private static ButtonGroup tomatoButtonGroup;
//private static JCheckBox cheeseCheck;
//private final JLabel feedbackMin = new JLabel(" "); //set to blank to start with
//private final JLabel feedbackMax = new JLabel(" ");
//private final JLabel feedbackName = new JLabel(" ");
//private final JLabel feedbackNumber = new JLabel(" ");
//
//// A variable to keep track of the selected type of meal, burger, salad
//private Type selectedOption;
//
///**
// * Constructor for the class, takes in a menu object that is used to access the database of
// * items available on the menu
// * @param menu - a menu object used for gaining access to the database.
// */
//public UserInput(Menu menu){
//    this.menu = menu;
//}
//
///**
// * The main panel aggregator, all panels of the class are added to this panel which handles the
// * layout and aligning of the user input fields for the GUI
// * @return - filterOptions - a panel used in the main window of the GUI
// */
//public JPanel generateWindow() {
//    // initialize the main panel
//    JPanel filterOption = new JPanel();
//
//    // Set all the parameters for the main panel
//    filterOption.setLayout(new BoxLayout(filterOption,BoxLayout.Y_AXIS));
//
//    // add all other panels to the main panel
//    filterOption.add(typePanel());
//    filterOption.add(bunOrDressingPanel());
//    filterOption.add(sauceOrGreensPanel());
//    filterOption.add(picklePanel());
//    filterOption.add(tomatoPanel());
//    filterOption.add(meatPanel());
//    filterOption.add(cheesePanel());
//    filterOption.add(pricePanel());
//
//    // Return the main panel
//    return filterOption;
//}
//
///**
// * Uses a J combo box to give the user the option of selecting which type of food they would
// * like to search for
// * @return - a panel containing the Types of foods from the database
// */
//public JPanel typePanel() {
//    // initialize the panel
//    JPanel meal = new JPanel();
//
//    // Set the selected type to neither
//    selectedOption = Type.SELECT;
//
//    // Set all the parameters for the panel
//    meal.setLayout(new BoxLayout(meal,BoxLayout.Y_AXIS));
//    meal.setBorder(BorderFactory.createTitledBorder("Please choose between a burger or a salad?"));
//
//    // Create a combobox and populate it with all options from the type enum
//    JComboBox<Type> typesOfMeals = new JComboBox<>(Type.values());
//    typesOfMeals.setSelectedItem(Type.SELECT);
//    typesOfMeals.setPreferredSize(new Dimension(300, 30));
//
//    // Add action listener to listen for user select of type of meal
//    ActionListener listener = e -> {
////            System.out.println(selectedOption);
//        bunOrDressingCombo.setEnabled(!(typesOfMeals.getSelectedItem() == Type.SELECT));
//        sauceOrGreensList.setEnabled(!(typesOfMeals.getSelectedItem() == Type.SELECT));
//
//        // Call the burger Or salad method sending the users choice to initialize the rest of the selection option
//        burgerOrSalad((Type) Objects.requireNonNull(typesOfMeals.getSelectedItem()));
//    };
//
//    // Add the listener to the combo box
//    typesOfMeals.addActionListener(listener);
//
//    // Create a panel to hold the combo box
//    JPanel typePane = new JPanel();
//    typePane.setPreferredSize(new Dimension(300, 50));
//
//    // Add the combo box to the panel
//    typePane.add(typesOfMeals);
//
//    // Add the combo box panel to the main panel
//    meal.add(Box.createRigidArea(new Dimension(0,20)));
//    meal.add(typePane);
//    meal.add(Box.createRigidArea(new Dimension(0,20)));
//
//    return meal;
//}
//
///**
// * Method for returning a panel that contains either the bun options or the dressing and cucumber options
// * depending on what meal the user has chosen, also deals with returning the chosen options for comparison
// * @return - A panel containing the dressing/bun and cucumber options
// */
//public JPanel bunOrDressingPanel() {
//
//    // Create the main panel to hold all the components
//    JPanel mainPanel = new JPanel();
//
//    // Set it's preferred size
//    mainPanel.setPreferredSize(new Dimension(300, 200));
//
//    // Create a panel to hold the
//    JPanel comboPane = new JPanel();
//
//    // Create an information label to let the customer know what they are selecting
//    bunOrDressingLabel = new JLabel(" ");
////        bunOrDressingLabel.setAlignmentX(0);
//
//    // Create a combo box to hold all the buns and dressings depending on the customers choice of type of meal
//    bunOrDressingCombo = new JComboBox<>();
//    bunOrDressingCombo.setPreferredSize(new Dimension(300, 30));
//
//    // Add the default item and set it to be disables
//    bunOrDressingCombo.addItem("Select Item");
//    bunOrDressingCombo.setEnabled(false);
//
//
//    // Add an item listener to help pass which option was selected for sauce or dressing
//    bunOrDressingCombo.addItemListener(e -> {
//
//        // If something was selected, check what it was
//        if (e.getStateChange() == ItemEvent.SELECTED) {
////                System.out.println(bun);
//            // If it was a burger, set all the defaults to burger related options
//            if (selectedOption == Type.BURGER) {
//
//                // Controls the customers input to be sent to the MenuSearcher class
//                bun = (String) bunOrDressingCombo.getSelectedItem();
//                assert bun != null;
//                if (bun.equals("I don't mind")) {
//                    bun = "NA";
//                }
//            }
//            // If it was a salad, send all salad information to the MenuSearcher class
//            else if (selectedOption == Type.SALAD){
//
//
//                if (bunOrDressingCombo.getSelectedItem().toString().equals("I don't mind...")) {
//                    dressing = Dressing.NA;
//
//                } else {
//                    // Parsing the values from the dressings enum
//                    dressing = Dressing.valueOf(bunOrDressingCombo.getSelectedItem().toString().toUpperCase().replace(" ", "_"));
//                }
//
//            }
//        }
//    });
//
//    // Create a panel for the cucumber option
//    cucumberRadio = new JPanel();
//
//    // Set it to invisible
//    cucumberRadio.setVisible(false);
//
//    // Create a button group to hold the cucumber options
//    cucumberButtonGroup = new ButtonGroup();
//    JLabel cucumberLabel = new JLabel("Cucumber?");
//
//    // Create the radio buttons with all the options we want to give the customer
//    JRadioButton yes = new JRadioButton("Yes");
//    JRadioButton no = new JRadioButton("No");
//    JRadioButton neither = new JRadioButton("I don't mind");
//
//    // Add the options to the radiobutton group
//    cucumberButtonGroup.add(yes);
//    cucumberButtonGroup.add(no);
//    cucumberButtonGroup.add(neither);
//    // Set the default selection
//    cucumberButtonGroup.setSelected(neither.getModel(), true);
//
//    // Set the output of the action commands
//    yes.setActionCommand("yes");
//    no.setActionCommand("no");
//    neither.setActionCommand("I don't mind");
//
//    // Handles passing the users selection to the MenuSearcher class
//    cucumber = cucumberButtonGroup.getSelection().getActionCommand();
//    ActionListener listener = e -> {
//        cucumber = cucumberButtonGroup.getSelection().getActionCommand();
//    };
//
//    // Add the listener to the radio buttons
//    yes.addActionListener(listener);
//    no.addActionListener(listener);
//    neither.addActionListener(listener);
//
//    // Add the radio buttons to the radio panel
//    cucumberRadio.add(cucumberLabel);
//    cucumberRadio.add(yes);
//    cucumberRadio.add(no);
//    cucumberRadio.add(neither);
//
//    // Set the layout and add all panels and components to the comboPane
//    comboPane.setLayout(new BoxLayout(comboPane, BoxLayout.Y_AXIS));
//    comboPane.add(Box.createRigidArea(new Dimension(0,50)));
//    comboPane.add(bunOrDressingLabel);
//    comboPane.add(Box.createRigidArea(new Dimension(0,10)));
//    comboPane.add(bunOrDressingCombo);
//    comboPane.add(Box.createRigidArea(new Dimension(0,20)));
//    comboPane.add(cucumberRadio);
//
//    // Finally add the combo pane to the main panel to be returned
//    mainPanel.add(Box.createRigidArea(new Dimension(0,20)));
//    mainPanel.add(comboPane);
//    mainPanel.add(Box.createRigidArea(new Dimension(0,20)));
//
//    return mainPanel;
//}
//
///**
// * Method for creating a Panel containing a list area and assigning its contents to the MenuSearcher
// * class for comparison. The list will change content depending on the users choice of selected meal
// * @return - A Panel containing a List area
// */
//public JPanel sauceOrGreensPanel() {
//
//    // Create the main panel
//    JPanel mainPanel = new JPanel();
//
//    // Create the panel to hold the List area
//    JPanel listPane = new JPanel();
//
//    // Create the label for the list
//    JLabel sauceOrGreensLabel = new JLabel(" ");
//
//    // Remove any items in the list area and add a default item to view in the JListArea
//    sauceOrGreensModel.removeAllElements();
//    sauceOrGreensModel.addElement("Select an Item");
//
//    // Initialize the JList with the predefined default list model
//    sauceOrGreensList = new JList<>(sauceOrGreensModel);
//
//    // Create a scroll pane passing it the list and setting its basic settings
//    JScrollPane scrollPane = new JScrollPane(sauceOrGreensList);
//    scrollPane.setPreferredSize(new Dimension(300, 100));
//    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//
//    // Allow multiple selections
//    sauceOrGreensList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
//
//    // Set it as disabled by default, will only be enabled if the customer selections a meal option
//    sauceOrGreensList.setEnabled(false);
//
//    // Create a listener to focus on the selections in the list, if the burger type was selected by the user
//    // set the sauce variable to a set of selected items, otherwise set it to the same for leafy greens
//    ListSelectionListener listener = e -> {
//        // TODO - Might have to specifically address the select case, instead of burger or else
//        if (selectedOption == Type.BURGER) {
//            sauce = new HashSet<>(sauceOrGreensList.getSelectedValuesList());
//        }
//        else {
//            leafy = new HashSet<>(sauceOrGreensList.getSelectedValuesList());
//        }
//    };
//
//    // Add the listener to the list
//    sauceOrGreensList.addListSelectionListener(listener);
//
//    // Add the Components to the list panel
//    listPane.add(sauceOrGreensLabel);
//    listPane.add(scrollPane);
//
//    // Add the list panel to the main panel as well as some padding
//    mainPanel.add(Box.createRigidArea(new Dimension(0,20)));
//    mainPanel.add(listPane);
//    mainPanel.add(Box.createRigidArea(new Dimension(0,20)));
//
//    return mainPanel;
//}
//
///**
// * Method that creates a set of radio buttons and returns them on a panel
// * @return a panel including the radio buttons for selection of pickles
// */
//public JPanel picklePanel() {
//    // Create the main panel
//    JPanel mainPanel = new JPanel();
//    mainPanel.setPreferredSize(new Dimension(500, 50));
//
//    // Create a panel to hold the radio buttons
//    JPanel radioPanel = new JPanel();
//
//    // Assign the pickle button group to a button group and add all of the options to the button group
//    pickleButtonGroup = new ButtonGroup();
//    JLabel pickleLabel = new JLabel("Pickles?");
//    JRadioButton yes = new JRadioButton("yes");
//    JRadioButton no = new JRadioButton("no");
//    JRadioButton neither = new JRadioButton("I don't mind");
//    yes.requestFocusInWindow();
//    pickleButtonGroup.add(yes);
//    pickleButtonGroup.add(no);
//    pickleButtonGroup.add(neither);
//    pickleButtonGroup.setSelected(neither.getModel(), true);
//
//    // Add the options to the radio panel
//    radioPanel.add(pickleLabel);
//    radioPanel.add(yes);
//    radioPanel.add(no);
//    radioPanel.add(neither);
//
//    // Set the action commands for the buttons
//    yes.setActionCommand("yes");
//    no.setActionCommand("no");
//    neither.setActionCommand("I don't mind");
//
//    // Set the default value
//    pickles = pickleButtonGroup.getSelection().getActionCommand();
//
//    // Create an action listener to list for changes from the customer
//    ActionListener listenForPickles = e -> {
//        pickles = pickleButtonGroup.getSelection().getActionCommand();
//    };
//
//    // Add the listener to the buttons
//    yes.addActionListener(listenForPickles);
//    no.addActionListener(listenForPickles);
//    neither.addActionListener(listenForPickles);
//
//    // Add the radio panel to the main panel
//    mainPanel.add(radioPanel);
//
//    return mainPanel;
//}
//
///**
// * Method that creates a set of radio buttons and returns them on a panel
// * @return a panel including the radio buttons for selection of tomatoes
// */
//public JPanel tomatoPanel() {
//    // Create the main panel
//    JPanel mainPanel = new JPanel();
//    mainPanel.setPreferredSize(new Dimension(500, 50));
//
//    // Create a panel to hold the radio buttons
//    JPanel radioPanel = new JPanel();
//
//    // Assign the tomato button group to a button group and add all the options to the button group
//    tomatoButtonGroup = new ButtonGroup();
//    JLabel pickleLabel = new JLabel("Tomato?");
//    JRadioButton yes = new JRadioButton("Yes");
//    JRadioButton no = new JRadioButton("No");
//    JRadioButton neither = new JRadioButton("I don't mind");
//    yes.requestFocusInWindow();
//    tomatoButtonGroup.add(yes);
//    tomatoButtonGroup.add(no);
//    tomatoButtonGroup.add(neither);
//    tomatoButtonGroup.setSelected(neither.getModel(), true);
//
//    // Set the action commands for the buttons
//    yes.setActionCommand("yes");
//    no.setActionCommand("no");
//    neither.setActionCommand("I don't mind");
//
//    // Set the default value
//    tomato = tomatoButtonGroup.getSelection().getActionCommand();
//
//    // Create an action listener to list for changes from the customer
//    ActionListener theTomatoIsTalking = e -> {
//
//        tomato = tomatoButtonGroup.getSelection().getActionCommand();
//    };
//
//    // Add the listener to the buttons
//    yes.addActionListener(theTomatoIsTalking);
//    no.addActionListener(theTomatoIsTalking);
//    neither.addActionListener(theTomatoIsTalking);
//
//    // Add the buttons to the radio panel
//    radioPanel.add(pickleLabel);
//    radioPanel.add(yes);
//    radioPanel.add(no);
//    radioPanel.add(neither);
//
//    // Add the radio panel to the main panel
//    mainPanel.add(radioPanel);
//
//    return mainPanel;
//}
//
///**
// * Method for creating a J combo Box full of the values from the Meat enum
// * @return JCombo box  comprised of Meat enum options
// */
//public JPanel meatPanel() {
//    // Create the main panel
//    JPanel mainPanel = new JPanel();
//
//    // Initialize the combo box to hold all the Meat enum values
//    meatCombo = new JComboBox<>(Meat.values());
//
//    // Set the size and default option
//    meatCombo.setPreferredSize(new Dimension(300, 40));
//    meatCombo.setSelectedItem(Meat.BEEF);
//    meat = (Meat) meatCombo.getSelectedItem();
//
//    // Add an listener to listen for the users selection
//    meatCombo.addItemListener(e -> {
//        if (e.getStateChange() == ItemEvent.SELECTED) {
//            meat = (Meat) meatCombo.getSelectedItem();
//
//        }
//    });
//
//    // Add the combo box
//    mainPanel.add(meatCombo);
//
//    return mainPanel;
//}
//
///**
// * Method that returns a panel containing a check box for the cheese selection
// * @return - A panel for the cheese selection
// */
//public JPanel cheesePanel() {
//    // Create the main panel
//    JPanel mainPanel = new JPanel();
//
//    // Create the label for the cheese
//    JLabel cheeseLabel = new JLabel("Cheese?");
//
//    // Assign the cheese check box to a new check box
//    cheeseCheck = new JCheckBox();
//
//    // Listen for the users selection
//    ChangeListener Nacholistener = e -> cheese = cheeseCheck.isSelected();
//
//    // Add the listener
//    cheeseCheck.addChangeListener(Nacholistener);
//
//    // Add the items to the main panel
//    mainPanel.add(cheeseLabel);
//    mainPanel.add(cheeseCheck);
//
//    return mainPanel;
//}
//
///**
// * Adapted from lecture 10 SearchView.java - getUserInputAgeRange lines - 290 - 363
// * Method that returns a panel of the users price input range. It uses a document listener to
// * inform the user if their input is correct and prompts them with how to fix it .
// * @return - A Panel containing all price related components.
// */
//public JPanel pricePanel(){
//    // Create the labels for the min and max price fields
//    JLabel minLabel = new JLabel("Min. Price");
//    JLabel maxLabel = new JLabel("Max. Price");
//
//    // Create the Text fields to hold the max and min prices
//    JTextField min = new JTextField(4);
//    JTextField max = new JTextField(4);
//
//    // Initialize the min and max fields
//    min.setText(String.valueOf(minPrice));
//    max.setText(String.valueOf(maxPrice));
//
//    // Controls the font and style of the feedback message
//    feedbackMin.setFont(new Font("", Font. ITALIC, 12));
//    feedbackMin.setForeground(Color.RED);
//    feedbackMax.setFont(new Font("", Font. ITALIC, 12));
//    feedbackMax.setForeground(Color.RED);
//
//    // Create a document listener for the min price, this will monitor the text field input for the min price
//    // and update the feedback label with instructions for the user.
//    min.getDocument().addDocumentListener(new DocumentListener() {
//        @Override
//        public void insertUpdate(DocumentEvent e) {
//            // If the check max method returns false, request user addresses the invalid input
//            if(!checkMin(min)) min.requestFocus();
//
//            // Check the max after the min has been updated
//            checkMax(max);
//        }
//        @Override
//        public void removeUpdate(DocumentEvent e) {
//            // removing and inserting should be subjected to the same checks
//            if(!checkMin(min))min.requestFocus();
//            // Check the max after the min has been updated
//            checkMax(max);
//        }
//        @Override
//        public void changedUpdate(DocumentEvent e) {} //NA
//    });
//
//    // Create a document listener for the max price, this will monitor the text field input for the max price
//    // and update the feedback label with instructions for the user.
//    max.getDocument().addDocumentListener(new DocumentListener() {
//        @Override
//        public void insertUpdate(DocumentEvent e) {
//            // If the check max method returns false, request user addresses the invalid input
//            if(!checkMax(max)) max.requestFocusInWindow();
//            // Check the min after the max has been updated
//            checkMin(min);
//        }
//        @Override
//        public void removeUpdate(DocumentEvent e) {
//            if(!checkMax(max))max.requestFocusInWindow();
//            // Check the min after the max has been updated
//            checkMin(min);
//        }
//        @Override
//        public void changedUpdate(DocumentEvent e) {
//        }
//    });
//
//    // Create a panel to hold all the price related J components
//    JPanel priceRangePanel = new JPanel();
//    priceRangePanel.add(minLabel);
//    priceRangePanel.add(min);
//    priceRangePanel.add(maxLabel);
//    priceRangePanel.add(max);
//
//    // Create the main panel to hold all components
//    JPanel mainPanel = new JPanel();
//
//    // Set the panels default settings and add the price panel and feedback labels
//    mainPanel.setBorder(BorderFactory.createTitledBorder("Enter desired Price range"));
//    mainPanel.setPreferredSize(new Dimension(300, 100));
//    mainPanel.add(priceRangePanel);
//    feedbackMin.setAlignmentX(0);
//    feedbackMax.setAlignmentX(0);
//    mainPanel.add(feedbackMin);
//    mainPanel.add(feedbackMax);
//
//    return mainPanel;
//}
//
///**
// * Adapted from lecture 10 SearchView.java, checkMin lines 372 - 391: Besides change ints to floats, Only variables
// * and comments were changed
//
// * Validates user input for the min price range
// * @param minEntry the JTextField used to enter the minimum price
// * @return true if valid price, false if invalid
// */
//private boolean checkMin(JTextField minEntry){
//    // Initialise the feedback string to an empty string
//    feedbackMin.setText("");
//
//    // Try to parse the users input and set the feedback text field to help them make a better input choice
//    try{
//        float tempMin = Float.parseFloat(minEntry.getText());
//        if(tempMin < -1 || tempMin>maxPrice) {
//            feedbackMin.setText("Min price must be >= "+ -1 +" and <= "+maxPrice+". Defaulting to "+minPrice+" - "+maxPrice+".");
//            minEntry.selectAll();
//            return false;
//        }else {
//            minPrice=tempMin;
//            feedbackMin.setText("");
//            return true;
//        }
//        // If they don't enter a number, let them know the price will default
//    }catch (NumberFormatException n){
//        feedbackMin.setText("Please enter a valid number for min price. Defaulting to "+minPrice+" - "+maxPrice+".");
//        minEntry.selectAll();
//        return false;
//    }
//}
//
///**
// * Adapted from lecture 10 SearchView.java  checkMin lines 392 - 415: Besides change ints to floats, Only variables
// * and comments were changed
//
// * validates user input for the max price range
// * @param maxEntry the JTextField used to enter the maximum price
// * @return true if valid price, false if invalid
// */
//private boolean checkMax(JTextField maxEntry){
//    // Initialise the feedback string to an empty string
//    feedbackMax.setText("");
//
//    // Try to parse the users input and set the feedback text field to help them make a better input choice
//    try{
//        float tempMax = Float.parseFloat(maxEntry.getText());
//        if(tempMax < minPrice) {
//            feedbackMax.setText("Max price must be >= min price. Defaulting to "+minPrice+" - "+maxPrice+".");
//            maxEntry.selectAll();
//            return false;
//        }else {
//            maxPrice = tempMax;
//            feedbackMax.setText("");
//            return true;
//        }
//        // If they don't enter a number, let them know the price will default
//    }catch (NumberFormatException n){
//        feedbackMax.setText("Please enter a valid number for max price. Defaulting to "+minPrice+" - "+maxPrice+".");
//        maxEntry.selectAll();
//        return false;
//    }
//}
//
///**
// * Method that sets which ingredients are available to choose from relating to menu specific items. Sets a combo
// * box and a list area with a user type selections options, it also takes care of resetting and switch the values
// * if the user changes which meal they want to order.
// * @param typeOfFood - Enum value of the users selection
// */
//public void burgerOrSalad(Type typeOfFood) {
//
//    // If the user selects Burger for their meal
//    if (typeOfFood.equals(Type.BURGER)){
//
//        // Set the selection Type to burger
//        selectedOption = Type.BURGER;
//
//        // Set the bun and dressing label to the bun option
//        bunOrDressingLabel.setText("Type of Bun");
//
//        // Delete all items in the combo box related to the bun's and dressing's
//        bunOrDressingCombo.removeAllItems();
//
//        // For every bun in the database
//        for (Object bun : menu.getAllIngredientTypes(Filter.BUN)) {
//            // Add the bun to the combo box
//            bunOrDressingCombo.addItem(bun.toString());
//        }
//        // Select the default item in the combo box to the first item
//        bunOrDressingCombo.setSelectedItem(bunOrDressingCombo.getItemAt(0));
//
//        // Delete all the elements in the sauceOrGreensModel (this will be a list of either sauce of leafy greens)
//        sauceOrGreensModel.removeAllElements();
//
//        // For every sauce in the Sauce enum
//        for (Sauce s : Sauce.values()) {
//            // Add each sauce to the List model
//            sauceOrGreensModel.addElement(s);
//        }
//
//        // Set the cucumber radio button to invisible
//        cucumberRadio.setVisible(false);
//
//    }
//    // If the user selects salad
//    else if (typeOfFood.equals(Type.SALAD)) {
//
//        // Set the type selection to Salad
//        selectedOption = Type.SALAD;
//
//        // Set the bun and dressing label to the salad option
//        bunOrDressingLabel.setText("Type of Dressing");
//
//        // Delete all items in the combo box related to the bun's and dressing's
//        bunOrDressingCombo.removeAllItems();
//
//
//        // For every Dressing in the Dressing Enum
//        for (Object dressing : Dressing.values()) {
//            // Add the dressing to the combo Box
//            bunOrDressingCombo.addItem(dressing.toString());
//        }
//
//        // Select the default item in the combo box to the first item
//        bunOrDressingCombo.setSelectedItem(bunOrDressingCombo.getItemAt(0));
//
//        // Set the default value in the combo box
////            bunOrDressingCombo.setSelectedItem(Dressing.NA);
//
//        // Delete all the elements in the sauceOrGreensModel (this will be a list of either sauce of leafy greens)
//        sauceOrGreensModel.removeAllElements();
//
//        // For every leaf in the database
//        for (Object leaf : menu.getAllIngredientTypes(Filter.LEAFY_GREENS)) {
//            // Add the leaf to the model list
//            sauceOrGreensModel.addElement(leaf.toString());
//        }
//
//        // Set the cucumber radio to visible
//        cucumberRadio.setVisible(true);
//    }
//    // If neither salad nor burger is selected
//    else {
//
//        // Set the type selection to Salad
//        selectedOption = Type.SELECT;
//
//        // Reset the bun and dressing label to empty
//        bunOrDressingLabel.setText("");
//
//        // Remove all options from the combo box and add the default option
//        bunOrDressingCombo.removeAllItems();
//        bunOrDressingCombo.addItem("Select an Item");
//
//        // Delete all options from the List area and add the default option
//        sauceOrGreensModel.removeAllElements();
//        sauceOrGreensModel.addElement("Select an Item");
//
//        // Set the cucumber radio to invisible
//        cucumberRadio.setVisible(false);
//    }
//}
//
//// Methods concerning View 3 of the Program
//
///**
// * Method for creating the order form for the 3rd view of the program
// * @return userInputPanel - A panel containing all the user input fields for the customer to enter their
// * information into.
// */
//public JPanel orderForm(){
//    // Create the labels for the text fields
//    JLabel enterName = new JLabel("Full name");
//    JLabel enterPhoneNumber = new JLabel("Phone number");
//    JLabel enterMessage = new JLabel("Any additional information?");
//
//    // Create the text fields for the user information variables
//    name = new JTextField(12);
//    phoneNumber = new JTextField(12);
//    customMessage = new JTextArea(6,12);
//
//    // Set the preferred size
//    name.setPreferredSize(new Dimension(100, 40));
//    phoneNumber.setPreferredSize(new Dimension(100, 40));
//
//
//    // Create a Doc listener for the name textfield to let the customer know what they need to do to
//    // enter the correct information
//    name.getDocument().addDocumentListener(new DocumentListener() {
//        @Override
//        public void insertUpdate(DocumentEvent e) {
//            // Run a check to see if the name field has the correct input, update the feedback is it isnt
//            if(checkName(name)) name.requestFocus();
//        }
//
//        @Override
//        public void removeUpdate(DocumentEvent e) {
//            // Run a check to see if the name field has the correct input, remove the feedback is it is
//            if(checkName(name))name.requestFocus();
//        }
//        // Not used, but implemented
//        @Override
//        public void changedUpdate(DocumentEvent e) {}
//    });
//
//    // Create a Doc listener for the phoneNumber textfield to let the customer know what they need to do to
//    // enter the correct information
//    phoneNumber.getDocument().addDocumentListener(new DocumentListener() {
//        @Override
//        public void insertUpdate(DocumentEvent e) {
//            // Run a check to see if the phoneNumber field has the correct input, update the feedback is it isnt
//            if(checkNumber(phoneNumber)) phoneNumber.requestFocusInWindow();
//
//        }
//        @Override
//        public void removeUpdate(DocumentEvent e) {
//            // Run a check to see if the phone number field has the correct input, remove the feedback is it is
//            if(checkNumber(phoneNumber))phoneNumber.requestFocusInWindow();
//
//        }
//        @Override
//        // Not used, but implemented
//        public void changedUpdate(DocumentEvent e) {
//        }
//    });
//
//    // ScrollPane to hold the custom message for the order
//    JScrollPane jScrollPane = new JScrollPane(customMessage);
//
//    // Create a panel to hold all the JComponents
//    JPanel userInputPanel = new JPanel();
//
//    // Set all parameters of the panel
//    userInputPanel.setLayout(new BoxLayout(userInputPanel,BoxLayout.Y_AXIS));
//    userInputPanel.add(Box.createRigidArea(new Dimension(0,10)));
//    userInputPanel.setAlignmentX(0);
//
//    // Align and add the name components
//    enterName.setAlignmentX(0);
//    name.setAlignmentX(0);
//    userInputPanel.add(enterName);
//    userInputPanel.add(name);
//    userInputPanel.add(Box.createRigidArea(new Dimension(0,10)));
//    userInputPanel.add(feedbackName);
//    userInputPanel.add(Box.createRigidArea(new Dimension(0,10)));
//
//    // Align and add the Phone number components
//    enterPhoneNumber.setAlignmentX(0);
//    phoneNumber.setAlignmentX(0);
//    userInputPanel.add(enterPhoneNumber);
//    userInputPanel.add(phoneNumber);
//    userInputPanel.add(Box.createRigidArea(new Dimension(0,10)));
//    userInputPanel.add(feedbackNumber);
//    userInputPanel.add(Box.createRigidArea(new Dimension(0,10)));
//
//    // Align and add the CustomMessage components
//    enterMessage.setAlignmentX(0);
//    customMessage.setAlignmentX(0);
//    userInputPanel.add(enterMessage);
//    jScrollPane.setAlignmentX(0);
//    userInputPanel.add(jScrollPane);
//    userInputPanel.add(Box.createRigidArea(new Dimension(0,10)));
//
//    return userInputPanel;
//}
//
///**
// * Method for controlling the feedback for the name text input field
// * @param nameField the JTextField containing the users name
// * @return true or false depending on if the user input is correct or not
// */
//private boolean checkName(JTextField nameField){
//    // If the name input by the user meets the correct format inform the user
//    if(isValidFullName(name.getText())) {
//        feedbackName.setText("Correct Input detected");
//        feedbackName.setFont(new Font("", Font. ITALIC, 12));
//        feedbackName.setForeground(Color.BLUE);
//        return false;
//        // If the input is incorrect, hint them to the correct response
//    }else {
//        feedbackName.setText("Please make sure to enter your Full name Starting with a Capital letter");
//        feedbackName.setFont(new Font("", Font. ITALIC, 12));
//        feedbackName.setForeground(Color.RED);
//        nameField.selectAll();
//        return true;
//    }
//}
//
///**
// * Method for controlling the feedback for the phone number text input field
// * @param numberField the JTextField containing the users phone number
// * @return true or false depending on if the user input is correct or not
// */
//private boolean checkNumber(JTextField numberField){
//    // If the phone number input by the user meets the correct format inform the user
//    if(isValidPhoneNumber(phoneNumber.getText())) {
//        feedbackNumber.setText("Correct Input detected");
//        feedbackNumber.setFont(new Font("", Font. ITALIC, 12));
//        feedbackNumber.setForeground(Color.BLUE);
//        return false;
//        // If the input is incorrect, hint them to the correct response
//    }else {
//        feedbackNumber.setText("Please Enter a 10 digit phone number starting with 04");
//        feedbackNumber.setFont(new Font("", Font. ITALIC, 12));
//        feedbackNumber.setForeground(Color.RED);
//        numberField.selectAll();
//        return true;
//    }
//}
//
///**
// * Adapted from my Assignment 2 - ItemSearcher.java lines 505 - 515
// * Compares a given string against a predetermined sequence of charters to determine if
// * customer input is correct. In this case the format of the users first and last name
// * @param fullName - User input of their first and last name
// * @return boolean True of False whether the input matched the required format
// */
//public boolean isValidFullName(String fullName) {
//
//    Pattern pattern = Pattern.compile("^([A-Z][a-z '.-]*(\\s))+[A-Z][a-z '.-]*$");
//
//    // Match the users input against the required format
//    Matcher matcher = pattern.matcher(fullName);
//
//    // Return the result
//    return matcher.matches();
//}
//
///**
// * * Adapted from my Assignment 2 - ItemSearcher.java lines 525 - 545
// * Compares a given string against a predetermined sequence of charters to determine if
// * customer input is correct. In this case the format of a phone number
// * @param phoneNumber - customer phone number asked to be input
// * @return boolean True of False whether the input matched the required format
// */
//public boolean isValidPhoneNumber(String phoneNumber) {
//    // Create a pattern object containing the required format
//    // is 10 digits starting with 04
//    Pattern pattern = Pattern.compile("^04\\d{8}$");
//
//    // Match the users input against the required format
//    Matcher matcher = pattern.matcher(phoneNumber);
//
//    // Return the result
//    return matcher.matches();
//}
//
//// Getters
//
///**
// * Gets the bun selected
// * @return The selected Bun
// */
//public String getBun() {
//    return bun;
//}
//
///**
// * Gets the sauce selected
// * @return The selected sauce
// */
//public Set<Object> getSauce() {
//    return sauce;
//}
//
///**
// * Gets the Leafy greens selected
// * @return The selected leafy greens
// */
//public Set<Object> getLeafy() {
//    return leafy;
//}
//
///**
// * Gets the dressing selected
// * @return The selected dressing
// */
//public Dressing getDressing() {
//    return dressing;
//}
//
///**
// * Gets the cucumber selected
// * @return The selected cucumber
// */
//public String getCucumber() {
//    return cucumber;
//}
//
///**
// * Gets the pickles selected
// * @return The selected pickles option
// */
//public String getPickles() {
//    return pickles;
//}
//
///**
// * Gets the tomato selected
// * @return The selected tomato option
// */
//public String getTomato() {
//    return tomato;
//}
//
///**
// * Gets the meat selected
// * @return The selected meat
// */
//public Meat getMeat() {
//    return meat;
//}
//
///**
// * Gets the cheese selected
// * @return The selected cheeese option
// */
//public boolean getCheese() {
//    return cheese;
//}
//
///**
// * Gets the min price
// * @return The min price
// */
//public float getMinPrice() {
//    return minPrice;
//}
//
///**
// * Gets the mac price
// * @return The max price
// */
//public float getMaxPrice() {
//    return maxPrice;
//}
//
///**
// * Gets the users name
// * @return The users name
// */
//public String getName() {
//    return name.getText();
//}
//
///**
// * Gets the user phone number
// * @return The users phone number
// */
//public String getPhoneNumber() {
//    return phoneNumber.getText();
//}
//
///**
// * Gets the custom message
// * @return The message
// */
//public String getMessage() {
//    return customMessage.getText();
//}
//
///**
// * Gets the selected item type
// * @return The selected item type
// */
//public Type getSelectedOption() {
//    return selectedOption;
//}



