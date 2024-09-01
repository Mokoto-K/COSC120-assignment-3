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
        cucumberButtonGroup.setSelected(yes.getModel(), true);

        yes.setActionCommand("yes");
        no.setActionCommand("no");
        neither.setActionCommand("I don't mind");

        ActionListener listener = e -> {
            cucumber = cucumberButtonGroup.getSelection().getActionCommand();
            if (cucumber.equals("I don't mind")) {
                cucumber = "NA";
            }
        };
        yes.addActionListener(listener);
        no.addActionListener(listener);
        neither.addActionListener(listener);

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
                }
                else {
                    if (bunOrDressingCombo.getSelectedItem().toString().equals("I don't mind...")) {
                        dressing = Dressing.NA;
                    }
                    dressing = Dressing.valueOf(bunOrDressingCombo.getSelectedItem().toString().toUpperCase().replace(" ", "_"));
                }
            }
        });

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

    public JPanel picklePanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(500, 50));

        JPanel radioPanel = new JPanel();

        pickleButtonGroup = new ButtonGroup();
        JLabel pickleLabel = new JLabel("Pickles?");
        JRadioButton yes = new JRadioButton("yes");
        JRadioButton no = new JRadioButton("no");
        JRadioButton neither = new JRadioButton("I don't mind");
        yes.requestFocusInWindow();
        pickleButtonGroup.add(yes);
        pickleButtonGroup.add(no);
        pickleButtonGroup.add(neither);
        pickleButtonGroup.setSelected(yes.getModel(), true);

        radioPanel.add(pickleLabel);
        radioPanel.add(yes);
        radioPanel.add(no);
        radioPanel.add(neither);

        yes.setActionCommand("yes");
        no.setActionCommand("no");
        neither.setActionCommand("I don't mind");

        ActionListener listenForPickles = e -> {

            pickles = pickleButtonGroup.getSelection().getActionCommand();
            if (pickles.equals("I don't mind")) {
                pickles = "NA";
            }
        };

        yes.addActionListener(listenForPickles);
        no.addActionListener(listenForPickles);
        neither.addActionListener(listenForPickles);


        mainPanel.add(radioPanel);

        return mainPanel;
    }

    public JPanel tomatoPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(500, 50));

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
        tomatoButtonGroup.setSelected(yes.getModel(), true);

        yes.setActionCommand("yes");
        no.setActionCommand("no");
        neither.setActionCommand("I don't mind");

        ActionListener theTomatoIsTalking = e -> {

            tomato = tomatoButtonGroup.getSelection().getActionCommand();
            if (tomato.equals("I don't mind")) {
                tomato = "NA";
            }
        };

        yes.addActionListener(theTomatoIsTalking);
        no.addActionListener(theTomatoIsTalking);
        neither.addActionListener(theTomatoIsTalking);

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
        meatCombo.setSelectedItem(Meat.BEEF.toString().toLowerCase());
        meat = (Meat) meatCombo.getSelectedItem();

        meatCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                meat = (Meat) meatCombo.getSelectedItem();
            }
        });

        mainPanel.add(meatCombo);

        return mainPanel;
    }

    public JPanel cheesePanel() {
        JPanel mainPanel = new JPanel();

        JLabel cheeseLabel = new JLabel("Cheese?");
        cheeseCheck = new JCheckBox();

        ChangeListener Nacholistener = e -> cheese = cheeseCheck.isSelected();

        cheeseCheck.addChangeListener(Nacholistener);

        mainPanel.add(cheeseLabel);
        mainPanel.add(cheeseCheck);

        return mainPanel;
    }

//    public JPanel pricePanel() {
//        JPanel mainPanel = new JPanel();
//
//        JLabel minPriceLabel = new JLabel("Min Price: ");
//        minPrice = new JTextField(5);
//
//        JLabel maxPriceLabel = new JLabel("Max Price: ");
//        maxPrice = new JTextField(5);
//
//        mainPanel.add(minPriceLabel);
//        mainPanel.add(minPrice);
//        mainPanel.add(maxPriceLabel);
//        mainPanel.add(maxPrice);
//
//        return mainPanel;
//    }

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

    public String getBun() {
        return bun;
    }

    public Set<Object> getSauce() {
        return sauce;
    }

    public Set<Object> getLeafy() {
        return leafy;
    }

    public Dressing getDressing() {
        return dressing;
    }

    public String getCucumber() {
        return cucumber;
    }

    public String getPickles() {
        return pickles;
    }

    public String getTomato() {
        return tomato;
    }

    public Meat getMeat() {
        return meat;
    }

    public boolean getCheese() {
        return cheese;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    //    public JTextField getMinPrice() {
//        return minPrice;
//    }
//
//    public JTextField getMaxPrice() {
//        return maxPrice;
//    }

    public JCheckBox getCheeseCheck() {
        return cheeseCheck;
    }

    public ButtonGroup getTomatoButtonGroup() {
        return tomatoButtonGroup;
    }

    public ButtonGroup getPickleButtonGroup() {
        return pickleButtonGroup;
    }

    public ButtonGroup getCucumberButtonGroup() {
        return cucumberButtonGroup;
    }

    public JList<Object> getSauceOrGreensList() {
        return sauceOrGreensList;
    }

    public JComboBox<Meat> getMeatCombo() {
        return meatCombo;
    }

    public JComboBox<Object> getBunOrDressingCombo() {
        return bunOrDressingCombo;
    }

    public Type getSelectedOption() {
        return selectedOption;
    }
}
