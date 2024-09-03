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

public class UserInput {

    private Menu menu;

    private String bun;
    private Set<Object> sauce;
    private Dressing dressing;
    private Set<Object> leafy;
    private String cucumber;
    private String pickles;
    private String tomato;
    private Meat meat;
    private boolean cheese;
    private float minPrice = 0;
    private float maxPrice = 80;

//    private
    private JTextField name;
    private JTextField email;
    private JTextField phoneNumber;
    private JTextArea message;


    private JLabel bunOrSauceLabel;
    private JComboBox<Object> bunOrDressingCombo;
    private JComboBox<Meat> meatCombo;
    private final DefaultListModel<Object> sauceOrGreensModel = new DefaultListModel<>();
    private JList<Object> sauceOrGreensList;
    private JPanel aCuccumberRadio;
    private ButtonGroup cucumberButtonGroup;
    private ButtonGroup pickleButtonGroup;
    private ButtonGroup tomatoButtonGroup;
    private JCheckBox cheeseCheck;
    private final JLabel feedbackMin = new JLabel(" "); //set to blank to start with
    private final JLabel feedbackMax = new JLabel(" ");
//    private JTextField minPrice;
//    private JTextField maxPrice;

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

        JLabel bunOrDressingLabel = new JLabel(" ");
        bunOrDressingCombo = new JComboBox<>();
        bunOrDressingCombo.setPreferredSize(new Dimension(300, 30));
        bunOrDressingCombo.addItem("Select Item");
        bunOrDressingCombo.setEnabled(false);


        // Add an item listener to help pass which option was selected for sauce or dressing
        bunOrDressingCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (selectedOption == Type.BURGER) {
                    bun = (String) bunOrDressingCombo.getSelectedItem();
                    assert bun != null;
                    if (bun.equals("I don't mind")) {
                        bun = "NA";
                    }

                }
                else {
                    if (bunOrDressingCombo.getSelectedItem().toString().equals("I don't mind...")) {
                        dressing = Dressing.NA;
                    }
                    dressing = Dressing.valueOf(bunOrDressingCombo.getSelectedItem().toString().toUpperCase().replace(" ", "_"));
                }
            }
        });

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
        cucumberButtonGroup.setSelected(neither.getModel(), true);

        yes.setActionCommand("yes");
        no.setActionCommand("no");
        neither.setActionCommand("I don't mind");

        cucumber = cucumberButtonGroup.getSelection().getActionCommand();
        ActionListener listener = e -> {
            cucumber = cucumberButtonGroup.getSelection().getActionCommand();
        };
        yes.addActionListener(listener);
        no.addActionListener(listener);
        neither.addActionListener(listener);

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

        ListSelectionListener listener = e -> {
            if (selectedOption == Type.BURGER) {
                sauce = new HashSet<>(sauceOrGreensList.getSelectedValuesList());
            }
            else {
                leafy = new HashSet<>(sauceOrGreensList.getSelectedValuesList());
            }
        };

        sauceOrGreensList.addListSelectionListener(listener);

        listPane.add(sauceOrGreensLabel);
        listPane.add(scrollPane);

        comboAndListPane.add(Box.createRigidArea(new Dimension(0,20)));
        comboAndListPane.add(listPane);
        comboAndListPane.add(Box.createRigidArea(new Dimension(0,20)));

        return comboAndListPane;
    }

    /**
     * Method that creates a set of radio buttons and returns them on a panel
     * @return a panel including the radio buttons for selection of pickles
     */
    public JPanel picklePanel() {
        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(500, 50));

        // Create a panel to hold the radio buttons
        JPanel radioPanel = new JPanel();

        // Assign the pickle button group to a button group and add all of the options to the button group
        pickleButtonGroup = new ButtonGroup();
        JLabel pickleLabel = new JLabel("Pickles?");
        JRadioButton yes = new JRadioButton("yes");
        JRadioButton no = new JRadioButton("no");
        JRadioButton neither = new JRadioButton("I don't mind");
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
        mainPanel.setPreferredSize(new Dimension(500, 50));

        // Create a panel to hold the radio buttons
        JPanel radioPanel = new JPanel();

        // Assign the tomato button group to a button group and add all the options to the button group
        tomatoButtonGroup = new ButtonGroup();
        JLabel pickleLabel = new JLabel("Tomato?");
        JRadioButton yes = new JRadioButton("Yes");
        JRadioButton no = new JRadioButton("No");
        JRadioButton neither = new JRadioButton("I don't mind");
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
        radioPanel.add(pickleLabel);
        radioPanel.add(yes);
        radioPanel.add(no);
        radioPanel.add(neither);

        // Add the radio panel to the main panel
        mainPanel.add(radioPanel);

        return mainPanel;
    }

    public JPanel meatPanel() {
        JPanel mainPanel = new JPanel();

        meatCombo = new JComboBox<>(Meat.values());
        meatCombo.setPreferredSize(new Dimension(300, 40));
        meatCombo.setSelectedItem(Meat.BEEF);
        meat = (Meat) meatCombo.getSelectedItem();

        meatCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                meat = (Meat) meatCombo.getSelectedItem();

            }
        });

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

        // Create the label for the cheese
        JLabel cheeseLabel = new JLabel("Cheese?");

        // Assign the cheese check box to a new check box
        cheeseCheck = new JCheckBox();

        // Listen for the users selection
        ChangeListener Nacholistener = e -> cheese = cheeseCheck.isSelected();

        // Add the listener
        cheeseCheck.addChangeListener(Nacholistener);

        // Add the items to the main panel
        mainPanel.add(cheeseLabel);
        mainPanel.add(cheeseCheck);

        return mainPanel;
    }

    // This was adapted from lecture 10 SearchView.java
    public JPanel pricePanel(){
        //labels for the text boxes
        JLabel minLabel = new JLabel("Min. age");
        JLabel maxLabel = new JLabel("Max. age");
        //create text boxes...
        JTextField min = new JTextField(4);
        JTextField max = new JTextField(4);
        //set default values for the age range text boxes (editable)
        min.setText(String.valueOf(minPrice));
        max.setText(String.valueOf(maxPrice));

        //this is how you change the font and size of text
        feedbackMin.setFont(new Font("", Font. ITALIC, 12));
        feedbackMin.setForeground(Color.RED);
        feedbackMax.setFont(new Font("", Font. ITALIC, 12));
        feedbackMax.setForeground(Color.RED);

        //EDIT 15: let’s add a document listener to the min and max age text fields.
        //You will see that the insertUpdate, removeUpdate and changedUpdate method declarations will
        //be automatically added. You must implement these (or leave them blank).
        //We’ll implement the first two, so that whenever the user enters or removes text from the fields,
        //we check whether the contents are valid.
        min.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                //if the check min method returns false, request user addresses invalid input
                if(!checkMin(min)) min.requestFocus();
                checkMax(max); //after min has been updated, check max is still valid
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                //removing and inserting should be subjected to the same checks
                if(!checkMin(min))min.requestFocus();
                checkMax(max);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {} //NA
        });
        max.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(!checkMax(max)) max.requestFocusInWindow();
                checkMin(min);
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                if(!checkMax(max))max.requestFocusInWindow();
                checkMin(min);
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        //add the text fields and labels to a panel
        JPanel ageRangePanel = new JPanel(); //flowlayout by default
        ageRangePanel.add(minLabel);
        ageRangePanel.add(min);
        ageRangePanel.add(maxLabel);
        ageRangePanel.add(max);

        JPanel finalPanel = new JPanel();
        finalPanel.setBorder(BorderFactory.createTitledBorder("Enter desired age range"));
        finalPanel.setPreferredSize(new Dimension(300, 100));
//        finalPanel.setLayout(new BoxLayout(finalPanel,BoxLayout.Y_AXIS)); //stack elements vertically
//        finalPanel.setAlignmentX(0);
        finalPanel.add(ageRangePanel);
        feedbackMin.setAlignmentX(0);
        feedbackMax.setAlignmentX(0);
        finalPanel.add(feedbackMin); //feedback below age entry text boxes
        finalPanel.add(feedbackMax);

        return finalPanel;
    }

    //EDIT 16: to minimize code repetition, let’s outsource the task of validating the user entry to these two methods.
    //They will return true if input is valid, and false if it isn’t. If input is not valid, we will request that
    //the user changes their input.
    /**
     * validates user input for min age
     * @param minEntry the JTextField used to enter min age
     * @return true if valid age, false if invalid
     */
    private boolean checkMin(JTextField minEntry){
        feedbackMin.setText("");
        try{
            int tempMin = Integer.parseInt(minEntry.getText());
            if(tempMin < -1 || tempMin>maxPrice) {
                feedbackMin.setText("Min age must be >= "+ -1 +" and <= "+maxPrice+". Defaulting to "+minPrice+" - "+maxPrice+".");
                minEntry.selectAll();
                return false;
            }else {
                minPrice=tempMin;
                feedbackMin.setText("");
                return true;
            }
        }catch (NumberFormatException n){
            feedbackMin.setText("Please enter a valid number for min age. Defaulting to "+minPrice+" - "+maxPrice+".");
            minEntry.selectAll();
            return false;
        }
    }

    /**
     * validates user input for max age
     * @param maxEntry the JTextField used to enter max age
     * @return true if valid age, false if invalid
     */
    private boolean checkMax(JTextField maxEntry){
        feedbackMax.setText("");
        try{
            int tempMax = Integer.parseInt(maxEntry.getText());
            if(tempMax < minPrice) {
                feedbackMax.setText("Max age must be >= min age. Defaulting to "+minPrice+" - "+maxPrice+".");
                maxEntry.selectAll();
                return false;
            }else {
                maxPrice = tempMax;
                feedbackMax.setText("");
                return true;
            }
        }catch (NumberFormatException n){
            feedbackMax.setText("Please enter a valid number for max age. Defaulting to "+minPrice+" - "+maxPrice+".");
            maxEntry.selectAll();
            return false;
        }
    }

    // Adapted for FindAPet.java Tutorial 10
    public JPanel contactForm(){
        //create labels and text fields for users to enter contact info and message
        JLabel enterName = new JLabel("Full name");
        name = new JTextField(12);
        name.setPreferredSize(new Dimension(100, 40));
        JLabel enterEmail = new JLabel("Email address");
        email = new JTextField(12);
        email.setPreferredSize(new Dimension(100, 40));
        JLabel enterPhoneNumber = new JLabel("Phone number");
        phoneNumber = new JTextField(12);
        phoneNumber.setPreferredSize(new Dimension(100, 40));
        JLabel enterMessage = new JLabel("Type your query below");
        message = new JTextArea(6,12);
        //add input validation for above fields

        JScrollPane jScrollPane = new JScrollPane(message);
//        jScrollPane.getViewport().setPreferredSize(new Dimension(250,100));

        //create a new panel, add padding and user entry boxes/messages to the panel
        JPanel userInputPanel = new JPanel();
//        userInputPanel.setPreferredSize(new Dimension(400, 400));
        userInputPanel.setLayout(new BoxLayout(userInputPanel,BoxLayout.Y_AXIS));
        userInputPanel.add(Box.createRigidArea(new Dimension(0,10)));
        userInputPanel.setAlignmentX(0);
        enterName.setAlignmentX(0);
        name.setAlignmentX(0);
        userInputPanel.add(enterName);
        userInputPanel.add(name);
        userInputPanel.add(Box.createRigidArea(new Dimension(0,10)));
        enterEmail.setAlignmentX(0);
        email.setAlignmentX(0);
        userInputPanel.add(enterEmail);
        userInputPanel.add(email);
        userInputPanel.add(Box.createRigidArea(new Dimension(0,10)));
        enterPhoneNumber.setAlignmentX(0);
        phoneNumber.setAlignmentX(0);
        userInputPanel.add(enterPhoneNumber);
        userInputPanel.add(phoneNumber);
        userInputPanel.add(Box.createRigidArea(new Dimension(0,10)));
        enterMessage.setAlignmentX(0);
        message.setAlignmentX(0);
        userInputPanel.add(enterMessage);
        jScrollPane.setAlignmentX(0);
        userInputPanel.add(jScrollPane);
        userInputPanel.add(Box.createRigidArea(new Dimension(0,10)));
        //return the panel to the calling method - could be either the send-message or adoption-request
        return userInputPanel;
    }

    public void burgerOrSalad(Type typeOfFood) {

        if (typeOfFood.equals(Type.BURGER)){
            selectedOption = Type.BURGER;
            bunOrDressingCombo.removeAllItems();
//            bunOrDressingCombo = new JComboBox<>();
            for (Object s : menu.getAllIngredientTypes(Filter.BUN)) {
                bunOrDressingCombo.addItem(s.toString());
            }
            bunOrDressingCombo.setSelectedItem(bunOrDressingCombo.getItemAt(0));

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

//            bunOrDressingCombo = new JComboBox<>(Dressing.values());
            bunOrDressingCombo.setSelectedItem(Dressing.NA);

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
     * Gets the bun selected
     * @return The selected Bun
     */
    public String getEmail() {
        return email.getText();
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
        return message.getText();
    }

    /**
     * Gets the selected item type
     * @return The selected item type
     */
    public Type getSelectedOption() {
        return selectedOption;
    }
}
