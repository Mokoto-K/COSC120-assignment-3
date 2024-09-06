/**
 * @author -
 * Email -
 * created for COSC120 Assignment 3
 * A Programed solution adding more funtionality and a graphical interface for the good people at the
 * gobbledy geek eatery
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class MenuSearcher {

    // Constants for the name of the app, the default image icon, and the file path for the database
    private static final String filePath = "./menu.txt";
    private static final ImageIcon icon = new ImageIcon("./gobbledy_geek_graphic_small.png");
    private static Menu menu = null;
    private static final String appName = "Eets 4 Gobbledy-Geeks";

    // Initialize our user input class to be used throughout the class
    private static UserInput userInput;

    // Initialize the enum type to be used throughout the class
    private static Type type;

    // Initialize our default JFrame and Panels to be used throughout the class
    private static JFrame mainFrame;
    private static JPanel defaultPane = null;
    private static JPanel userInformationView = null; //view 3

    // Initialize the comboBox that will hold all the returned matches from the users order
    private static JComboBox<String> matchingMealsCombo = null;

    public static void main(String[] args) {
        menu = loadMenu(filePath);

        // Create the main frame for our gui
        mainFrame = new JFrame(appName);

        // Set basic settings for our gui
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setIconImage(icon.getImage());
        // Changing frame size for testings, it was 500, 900
        mainFrame.setPreferredSize(new Dimension(1000, 500));

        // Assign the output of our main panel method to the set content pane method of the JFrame to initialise our gui
        defaultPane = mainPanel();
        mainFrame.setContentPane(defaultPane);

        // Jam all our panels into the frame and make it visible
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    /**
     * Method to read in a database and convert the content into a map object that is used to initialize
     * the each item from the data, it does this by create MenuItems from each line of the file.
     * @param filePath - A String containing the location of the database file
     * @return menu - A Menu object containing the entire database to be used throughout the program
     */
    public static Menu loadMenu(String filePath) {
        // Create  Menu object to use to hold all the items from the database
        Menu menu = new Menu();

        // File path of the database file
        Path path = Path.of(filePath);

        // Initialize a list to temporarily hold items from the database
        List<String> fileContents = null;

        // Read the contents of the file, informing the user of an error if one occurs
        try {
            fileContents = Files.readAllLines(path);
        }catch (IOException io){
            System.out.println("File could not be found");
            System.exit(0);
        }

        // Iterate throught the list containing all the lines from the database
        for(int i=1;i<fileContents.size();i++){

            // Split up each line via the comma
            String[] info = fileContents.get(i).split("\\[");

            // Split up the items that weren't in lists
            String[] singularInfo = info[0].split(",");

            // Clean the items that were in lists removing unwanted characters
            String leafyGreensRaw = info[1].replace("]","");
            String saucesRaw = info[2].replace("]","");
            String description = info[3].replace("]","");

            // Read in the id for each menu item
            long menuItemIdentifier = 0;
            try{
                menuItemIdentifier = Long.parseLong(singularInfo[0]);
            }catch (NumberFormatException n) {
                System.out.println("Error in file. Menu item identifier could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+n.getMessage());
                System.exit(0);
            }

            // Read in the type of meal from the database
            Type type = null;
            try{
                type = Type.valueOf(singularInfo[1].toUpperCase().strip());
            }catch (IllegalArgumentException e){
                System.out.println("Error in file. Type data could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                System.exit(0);
            }

            // Read in the name of the item
            String menuItemName = singularInfo[2];

            // Read in the price of the item
            double price = 0;
            try{
                price = Double.parseDouble(singularInfo[3]);
            }catch (NumberFormatException n){
                System.out.println("Error in file. Price could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+n.getMessage());
                System.exit(0);
            }

            // Read in the bun of the item
            String bun = singularInfo[4].toLowerCase().strip();

            // Get the meat for the menu item
            Meat meat = null;
            try {
                meat = Meat.valueOf(singularInfo[5].toUpperCase());
            }catch (IllegalArgumentException e){
                System.out.println("Error in file. Meat data could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                System.exit(0);
            }

            // Get and set the cheese baby
            boolean cheese = false;
            String cheeseRaw = singularInfo[6].strip().toUpperCase();
            if(cheeseRaw.equals("YES")) cheese = true;

            // Who wants pickles? well let's read them in from the database and find out
            boolean pickles = false;
            String pickleRaw = singularInfo[7].strip().toUpperCase();
            if(pickleRaw.equals("YES")) pickles = true;

            // Read in the cucumber
            boolean cucumber = false;
            String cucumberRaw = singularInfo[8].strip().toUpperCase();
            if(cucumberRaw.equals("YES")) cucumber = true;

            // Read in the tomato
            boolean tomato = false;
            String tomatoRaw = singularInfo[9].strip().toUpperCase();
            if(tomatoRaw.equals("YES")) tomato = true;

            // Create a list of dressings for each salad item
            Dressing dressing = null;
            try {
                dressing = Dressing.valueOf(singularInfo[10].toUpperCase().replace(" ","_"));
            }catch (IllegalArgumentException e){
                System.out.println("Error in file. Dressing data could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                System.exit(0);
            }

            // Create a list of leafy greens for the salads
            Set<String> leafyGreens = new HashSet<>();
            for(String l: leafyGreensRaw.split(",")){
                leafyGreens.add(l.toLowerCase().strip());
            }

            // Create a list of sauces for the burgers
            Set<Sauce> sauces = new HashSet<>();
            for(String s: saucesRaw.split(",")){
                Sauce sauce = null;
                try {
                    sauce = Sauce.valueOf(s.toUpperCase().strip());
                }catch (IllegalArgumentException e){
                    System.out.println("Error in file. Sauce/s data could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                    System.exit(0);
                }
                sauces.add(sauce);
            }

            // Create a map to hold all the items for each meal
            Map<Filter,Object> filterMap = new LinkedHashMap<>();

            // Put each type of meal in the map
            filterMap.put(Filter.TYPE,type);

            // If the meal has type burger add only the burger specific items
            if(type.equals(Type.BURGER)){
                filterMap.put(Filter.BUN, bun);
                if(!sauces.isEmpty()) filterMap.put(Filter.SAUCE_S,sauces);
            }

            // Only add meat if there was meat
            if(!meat.equals(Meat.NA)) filterMap.put(Filter.MEAT,meat);

            // Add the extra salad options
            filterMap.put(Filter.PICKLES, pickles);
            filterMap.put(Filter.CHEESE, cheese);
            filterMap.put(Filter.TOMATO, tomato);

            // If the type of meal was salad, add only the salad specific items
            if(type.equals(Type.SALAD)){
                filterMap.put(Filter.DRESSING,dressing);
                filterMap.put(Filter.LEAFY_GREENS,leafyGreens);
                filterMap.put(Filter.CUCUMBER, cucumber);
            }

            // Create a new dream menu item passing in the map of the menu item from the database
            DreamMenuItem dreamMenuItem = new DreamMenuItem(filterMap);

            // Pass the dream menu item as well as the item id and name into the Menu Item constructor
            MenuItem menuItem = new MenuItem(menuItemIdentifier, menuItemName,price,description, dreamMenuItem);

            // Call the addItem method passing it the MenuItem
            menu.addItem(menuItem);
        }
        // Return the menu object loaded up with the database
        return menu;
    }

    /**
     * Method to create the main view of the GUI by adding the contents of the UserInput object and the
     * search button
     * @return a JPanel of potential meal options from the database as well as a search button
     */
    public static JPanel mainPanel() {
        // Create the main panel for the search view part of the GUI, the 1st view
        JPanel mainWindowPanel = new JPanel();

        // Set the layout for the panel
        mainWindowPanel.setLayout(new BorderLayout());

        // Load the database up
        userInput = new UserInput(menu);

        // Creating a button to control searching
        JButton searchUsersButt = new JButton("Search For Meals");
        // Action listener that will call our method for comparing the users choices against the database
        ActionListener listener = e -> searchForItems(userInput);
        // Adding the listener to the button
        searchUsersButt.addActionListener(listener);

        // TODO - Make picture appear if i have time
        // Create an ImageIcon of the chosen meal by looking up it's ID number and searching for the corresponding image
        // in the image file
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(Color.WHITE);
        imagePanel.setLayout(new BorderLayout());
        ImageIcon picOfFood = new ImageIcon(new ImageIcon("./gobbledy_geek_graphic.png")
                .getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));

        // Create a label passing in the imageicon
        JLabel image = new JLabel(picOfFood);
        imagePanel.add(image, BorderLayout.CENTER);

        // Add the returned panel from the UserInput class as well as the button to submit search results.
        mainWindowPanel.add(imagePanel, BorderLayout.WEST);
        mainWindowPanel.add(userInput.generateWindow(), BorderLayout.CENTER);
        mainWindowPanel.add(searchUsersButt, BorderLayout.SOUTH);

        return mainWindowPanel;
    }

    /**
     * Method used to collect the user entered data and send the results to be compared against all items in the
     * database, which will be returned as a list and passed onto the resultsMainPanel Method
     * @param userInput - An instance of the UserINput class which handles the Components for the main view
     */
    public static void searchForItems(UserInput userInput) {
        // Create a new map to hold our customers selections
        Map<Filter, Object> filtersMap = new HashMap<>();

        // Look to see if a customer has selected which type of meal they would like to order and advise them to pick
        // one if they haven't yet
        type = userInput.getSelectedOption();
        if (type == Type.SELECT) {
            JOptionPane.showMessageDialog(mainFrame, "Please select either a Burger or Salad to continue. \n",
                    "Invalid Type of Meal selected", JOptionPane.INFORMATION_MESSAGE, null);
            return;
        }
        // Add the chosen type of meal to the map
        filtersMap.put(Filter.TYPE, type);

        // Add specific menu only items to the map, ie for burger selection or salad selection
        if (type.equals(Type.BURGER)) {
            // If the user selects a specific bun, add it to the map
            String bun = userInput.getBun();
            if (!bun.equals("NA")) filtersMap.put(Filter.BUN, bun);

            // If the user selects at least one sauce option, that isn't "Any will do" add it to the set
            Set<Object> sauces = userInput.getSauce();
            if (!(sauces.isEmpty())) {
//                System.out.println(sauces);
                if (!sauces.contains(Sauce.NA)) {
                    filtersMap.put(Filter.SAUCE_S, sauces);
                }
            }

        }
        else {
            // Add dressing, cucumber and leafy greens if the user has chosen salad
            Dressing dressing = userInput.getDressing();
            if (!(dressing == Dressing.NA)) filtersMap.put(Filter.DRESSING, dressing);

            String cucumber = userInput.getCucumber();
            if (!cucumber.equals("I don't mind")) {
                if (cucumber.equals("yes")) filtersMap.put(Filter.CUCUMBER, true);
                else filtersMap.put(Filter.CUCUMBER, false);
            }

            Set<Object> leafy = userInput.getLeafy();
            if (!(leafy.isEmpty())) {
                if (!leafy.contains("I don't mind")) {
                    filtersMap.put(Filter.LEAFY_GREENS, leafy);
                }
            }

        }

        // Add the pickles and the tomato id the user has chosen values for each
        String pickles = userInput.getPickles();
        if (!pickles.equals("I don't mind")) {
            if (pickles.equals("yes")) filtersMap.put(Filter.PICKLES, true);
            else filtersMap.put(Filter.PICKLES, false);
        }

        String tomato = userInput.getTomato();
        if (!tomato.equals("I don't mind")) {
            if (tomato.equals("yes")) filtersMap.put(Filter.TOMATO, true);
            else filtersMap.put(Filter.TOMATO, false);
        }

        // Add meat if the user has selected an option
        Meat meat = userInput.getMeat();
        if (!(meat == Meat.NA)) filtersMap.put(Filter.MEAT, meat);

        // Get Da cheese
        filtersMap.put(Filter.CHEESE, userInput.getCheese());


        // The min and max price range the customer is looking for
        float minimumPrice = userInput.getMinPrice();
        float maximumPrice = userInput.getMaxPrice();



        // Create a DreamMeniItem passing in the map and the customers price range values
        DreamMenuItem dreamMenuItem = new DreamMenuItem(filtersMap, minimumPrice, maximumPrice);
        // Pass the customers dreamMenuItem to the findmatch method to check for matches in the database
        List<MenuItem> potentialMatches = menu.findMatch(dreamMenuItem);

        // If potential matches returns empty
        if(potentialMatches.isEmpty()) {
            // Inform the customer their choices led to no menu items and let them try again.
            JOptionPane.showMessageDialog(mainFrame, "No meals for you bitch. \n", "No meals Found", JOptionPane.INFORMATION_MESSAGE, icon);
            // Call restart default pne to reset the main window of the GUI
            restartDefaultPane();
        }
        else {
            // Return all the matches to the resultsMainPanel method which will generate a panel containing
            resultsMainPanel(potentialMatches);
        }
    }

    /**
     * Re-initiates the defaultPane and restarts the mainFrame with the defaultPane
     */
    public static void restartDefaultPane(){
        // Initiate the defaultPane with the output of the mainPanel
        defaultPane = mainPanel();
        // Reset the mainFrame with the defaultPane
        mainFrame.setContentPane(defaultPane);
        // Reset the hierarchy of the app with the defaultPane reestablished at the top
        mainFrame.revalidate();
    }

    /**
     * Adapted from SeekAGeek.java Lecture 10
     * The method that displays the results for the customers search, it does this by calling the methods needed
     * to get the information of the returned matches from the database as well as those same items in a combobox
     * to be selected by the customer and a JButton to let the user search again.
     * @param potentialMatches - A List of menuitems from the database that matched the users search criteria
     */
    public static void resultsMainPanel(List<MenuItem> potentialMatches) {

        // Generate the Base panel for the search results to be displayed on and set the layout
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());

        // Add padding to the top of the window
        main.add(Box.createRigidArea(new Dimension(0,10)),BorderLayout.NORTH);

        // Add the JPanel that will contain all matches from the menu database
        main.add(generateResultsList(potentialMatches),BorderLayout.CENTER);

        // Add the Search again button and ComboBox full of all matched database items for the customer to select from
        main.add(selectFromResultsList(potentialMatches),BorderLayout.SOUTH);

        // Add padding to the left and the right of the window
        main.add(Box.createRigidArea(new Dimension(20,0)),BorderLayout.WEST);
        main.add(Box.createRigidArea(new Dimension(20,0)),BorderLayout.EAST);

        // Set the mainFrame to show this panel as the main panel
        mainFrame.setContentPane(main);
        mainFrame.revalidate();
    }

    // TODO - Extract description method
    /**
     * Adapted from SeekAGeek.java Lecture 10
     * A Method that displays all the menu items that matched the users choices from the defaultPane, allows the user
     * to also make a selection of which menu item they would like or to search again if the results were not what
     * they were looking for
     * @param potentialMatches - A List of menuitems from the database that matched the users search criteria
     * @return verticalScrollBar - A JScrollBar object containing all the matched menu items from the users selections
     */
    public static JScrollPane generateResultsList(List<MenuItem> potentialMatches) {
        // Initialize a string array to hold all the names of the menu items that will populate the comboBox
        String[] menuOptions = new String[potentialMatches.size()];

        // Main panel to display all matching menu items on and the Combo box for item selection
        JPanel mainPanel = new JPanel();

        // Prettifying the border
        mainPanel.setBorder(BorderFactory.createTitledBorder("Menu Items Matching your Description: "));

        // Setting the layout to display one item after another
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

        // Adding some padding to the top of the display area
        mainPanel.add(Box.createRigidArea(new Dimension(0,10)));

        // Iterate through all matches
        for (int i = 0; i < potentialMatches.size(); i++) {
            // Create a base panel to visually bring all elements together
            JPanel basePanel = new JPanel();
            // Set the panels layout and colour
            basePanel.setLayout(new BorderLayout());
            basePanel.setBackground(Color.white);

            // Create a Panel to hold all components for each Menu Item returned
            JPanel contentPanel = new JPanel();
            // Set its background color to white
            contentPanel.setBackground(Color.white);

            // Create an ImageIcon passing it the file path of the current MenuItems ID to return the image related
            // to the MenuItem
            ImageIcon picOfFood = new ImageIcon(new ImageIcon("./images/"+ potentialMatches.get(i).menuItemIdentifier() +".png")
                    .getImage().getScaledInstance(325, 325, Image.SCALE_DEFAULT));

            // Create a label passing in the image icon
            JLabel currentItemImage = new JLabel(picOfFood);

            // Create a Text area to display a matches information
            JTextArea mealDescription = new JTextArea();

            // Depending on the type of meal a customer selected, get the information of the items only for that meal type
            if (type.equals(Type.BURGER)) mealDescription = new JTextArea(potentialMatches.get(i).toString());
            if (type.equals(Type.SALAD)) mealDescription = new JTextArea(potentialMatches.get(i).toString());

            // Set the font and size of the TextArea
            mealDescription.setFont(new Font("", Font. PLAIN, 17));
            // Stop the text field from being editable
            mealDescription.setEditable(false);

            // Control the text to wrap around lines and only break on spaces
            mealDescription.setLineWrap(true);
            mealDescription.setWrapStyleWord(true);
            mealDescription.setPreferredSize(new Dimension(550, 350));

            // Add padding and both the Image and the item description to the contentpanel
            contentPanel.add(Box.createRigidArea(new Dimension(5, 0)));;
            contentPanel.add(currentItemImage);
            contentPanel.add(Box.createRigidArea(new Dimension(20, 0)));
            contentPanel.add(mealDescription);

            // Add the content Panel to hte basPanel
            basePanel.add(contentPanel, BorderLayout.WEST);

            // Add the BasePanel and some padding to the main panel holding all items
            mainPanel.add(basePanel);
            mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            // Add the item to the String array of options to choose from for the combo Box
            menuOptions[i] = potentialMatches.get(i).menuItemName();
        }
        // Create the Combo box passing it in the list of menu items
        matchingMealsCombo = new JComboBox<>(menuOptions);

        // Initialize a scroll bar to search the returned menu item descriptions
        JScrollPane verticalScrollBar = new JScrollPane(mainPanel);
        verticalScrollBar.setPreferredSize(new Dimension(300, 450));

        // Set the bar to always appear
        verticalScrollBar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // The comment below is a really good comment, so good that im commenting about the comment!
        //this positions the scrollbar at the top (without it, the scrollbar loads part way through
        //adding of the text areas to the JPanel, resulting in the scrollbar being halfway down the results
        SwingUtilities.invokeLater(() -> verticalScrollBar.getViewport().setViewPosition( new Point(0, 0) ));
        return verticalScrollBar;
    }

    /**
     * Adapted from seekAGeek.java Lecture 10 - There were no changes made to this method besides variable names & comments
     * A Method used to create a panel that contains both a combobox for the user to select an item from and a button
     * that can be clicked to take a user back to the start of the program.
     * @param potentialMatches - A List of menuitems from the database that matched the users search criteria
     * @return - JPanel containing a combobox and Jbutton
     */
    public static JPanel selectFromResultsList(List<MenuItem> potentialMatches) {
        // Let the user know their options if they don't like the displayed list of menu items
        JLabel noneMessage = new JLabel("If the displayed items do not please you, try search again or exit");

        // Create the button to be used to refresh the mainFrame with the default display
        JButton editSearchCriteriaButton = new JButton("Try again");

        // Add the listener to take the user back to the beginning
        ActionListener actionListenerEditCriteria = e -> restartDefaultPane();
        editSearchCriteriaButton.addActionListener(actionListenerEditCriteria);

        // Set the default item in the comboBox
        String defaultOption = "Select Item";
        matchingMealsCombo.addItem(defaultOption);
        matchingMealsCombo.setSelectedItem(defaultOption);

        // If any option is clicked on the actionListener will detect it and trigger the checkUserSelection method
        // passing in the selected option
        ActionListener actionListener = e -> checkUserSelection(potentialMatches);
        matchingMealsCombo.addActionListener(actionListener);

        // Create a panel to hold both the combo box and the button and add them both
        JPanel buttonOptionPanel = new JPanel();
        buttonOptionPanel.add(matchingMealsCombo);
        buttonOptionPanel.add(editSearchCriteriaButton);

        // We call this panel-ception, create a panel to hold a panel and return the panel to control the layout of the panel
        JPanel selectionPanel = new JPanel();

        // Set the layout to box
        selectionPanel.setLayout(new BoxLayout(selectionPanel,BoxLayout.Y_AXIS));

        // Add padding and a title to prettify the panel
        selectionPanel.add(Box.createRigidArea(new Dimension(0,10)));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Please select which meal you'd like to order:"));

        // Add the label to search again as well as the button and combobox panel
        selectionPanel.add(noneMessage);
        selectionPanel.add(buttonOptionPanel);
        selectionPanel.add(Box.createRigidArea(new Dimension(10,0)));
        return selectionPanel;
    }

    /**
     * Adapted for seekAGeek.java Lecture 10
     * A method used to get the item from the database that a user has selected and passing that item into the
     * next display window
     * @param potentialMatches - A List of menuItems from the database that matched the users search criteria
     */
    public static void checkUserSelection(List<MenuItem> potentialMatches){

        // Variable for the users selected meal from the combo box
        String selectedItem = (String) matchingMealsCombo.getSelectedItem();

        // Let java know that the selection isn't null
        assert selectedItem != null;

        // Iterate through the menu database
        for (MenuItem meal : potentialMatches) {
            // If the selectedItem from the combo box matches an item in the database, then return its information
            if (selectedItem.equals(meal.menuItemName())) {
                // Call the placeOrder method passing in the meal returned from the database.
                placeOrder(meal);
            }
        }
    }

    // TODO - Check the 3rd viewing window isn't broken due to images
    /**
     * A Method that takes in the users order and builds the main window for the customer to enter their details to
     * order their meal. It creates a series of panels using the customers choice of meal to display all the information
     * required and then sets the main frame to use the panels as the main view for the program, it also sends the
     * users information off to be turned into an order file.
     * @param chosenMeal - The users selected meal they would like to order
     */
    public static void placeOrder(MenuItem chosenMeal){

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);


        // Title for the top of the window including the name of the meal and instructions for the customer
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JLabel panelTitle = new JLabel("To place an order for a "+chosenMeal.menuItemName()+" Please enter your details below  ");
        titlePanel.add(Box.createRigidArea(new Dimension(0,10)), BorderLayout.NORTH);
        titlePanel.add(Box.createRigidArea(new Dimension(10,0)), BorderLayout.WEST);
        titlePanel.add(panelTitle, BorderLayout.CENTER);
        titlePanel.add(Box.createRigidArea(new Dimension(0,10)), BorderLayout.SOUTH);
        // Create a ScrollPane and populate it with the details of the users choice of meal
//        JScrollPane jScrollPane = new JScrollPane(chosenItemDescription(chosenMeal));

        JTextArea itemDescription = chosenItemDescription(chosenMeal);
        itemDescription.setPreferredSize(new Dimension(350, 350));
        itemDescription.setFont(new Font("", Font. PLAIN, 15));
        // Set the size of the Scroll pane
//        jScrollPane.getViewport().setPreferredSize(new Dimension(400,400));
//        jScrollPane.setBorder(BorderFactory.createLineBorder(Color.WHITE));
//        jScrollPane.setLayout(new BorderLayout());
        // Create a Panel to hold the panel that has the title, the image and the item description
//        JPanel controlPane = new JPanel();
//        controlPane.setPreferredSize(new Dimension(500, 600));
//        controlPane.setLayout(new BorderLayout());

        // Create a panel to hold the title, image and item description
//        JPanel itemDescriptionPanel = new JPanel();
//        itemDescriptionPanel.setLayout(new BorderLayout());

        // Create an ImageIcon of the chosen meal by looking up it's ID number and searching for the corresponding image
        // in the image file
        ImageIcon picOfFood = new ImageIcon(new ImageIcon("./images/"+ chosenMeal.menuItemIdentifier() +".png")
                .getImage().getScaledInstance(250, 250, Image.SCALE_DEFAULT));

        // Create a label passing in the imageicon
        JLabel image = new JLabel(picOfFood);

        contentPanel.add(Box.createRigidArea(new Dimension(10,0)));
        contentPanel.add(image);
        contentPanel.add(Box.createRigidArea(new Dimension(10,0)));
//        contentPanel.add(jScrollPane);
        contentPanel.add(itemDescription);
//        jScrollPane.add(image, BorderLayout.WEST);
        // Add the title, Image and description to the panel
//        itemDescriptionPanel.add(paneTitle, BorderLayout.NORTH);
//        itemDescriptionPanel.add(image, BorderLayout.CENTER);
//        itemDescriptionPanel.add(jScrollPane, BorderLayout.SOUTH);

        // Add padding to the left and right of the main container
//        controlPane.add(Box.createRigidArea(new Dimension(20,0)), BorderLayout.WEST);
//        controlPane.add(Box.createRigidArea(new Dimension(20,0)), BorderLayout.EAST);
//        controlPane.add(itemDescriptionPanel);

        // Create a Panel and pass it the return value from the contactForm method
        JPanel userInputPanel = userInput.orderForm();

        JPanel onePanel = new JPanel();
//        onePanel.add(Box.createRigidArea(new Dimension(10,0)));
        onePanel.add(userInputPanel);
        onePanel.add(Box.createRigidArea(new Dimension(10,0)));
        onePanel.add(contentPanel);
//        onePanel.add(Box.createRigidArea(new Dimension(10,0)));

        // Create a button for confirmation by calling the submitOrderButton Method
        JButton submit = submitOrderButton(chosenMeal);

        // Create the main panel for the entire view, tweak its parameters and add all the other panels and the button
//        mainPanel.add(Box.createRigidArea(new Dimension(20,20)));
        mainPanel.add(titlePanel, BorderLayout.NORTH);
//        mainPanel.add(Box.createRigidArea(new Dimension(20,0)));
//        mainPanel.add(userInputPanel);
//        mainPanel.add(Box.createRigidArea(new Dimension(20,0)));
        mainPanel.add(onePanel, BorderLayout.CENTER);
//        mainPanel.add(Box.createRigidArea(new Dimension(20,0)));
//        mainFramePanel.add(Box.createRigidArea(new Dimension(20,0)),BorderLayout.EAST);
        mainPanel.add(submit, BorderLayout.SOUTH);

        // Inform the MainFrame that we are changing which panel is our new viewing panel
        userInformationView = mainPanel;
        mainFrame.setContentPane(userInformationView);
        mainFrame.revalidate();
    }

    /**
     * Method used to help write a customers order to a file, it creates a button that has an action listener
     * attached that will append the details of the users meal to a string and will return the button to the order
     * Panel
     * @param chosenMeal - The item a customer has chosen to purchase
     * @return - submit - A JButton containing an action listener that will write the customers order to a string
     */
    private static JButton submitOrderButton(MenuItem chosenMeal) {
        JButton submit = new JButton("Submit");

        // The listener and the string that will be sent to be turned into a txtfile for ordering
        ActionListener actionListener = e -> {

            // As long as the user input is correct, append all details to the string
            if (userInput.isValidFullName(userInput.getName()) && userInput.isValidPhoneNumber(userInput.getPhoneNumber())) {
                String lineToWrite = "Order Details: \n" + "\tName: " + userInput.getName() + " (" + userInput.getPhoneNumber()
                        + ")\n\t" + "Item: " + chosenMeal.menuItemName() + "(" + chosenMeal.menuItemIdentifier() + ")" +
                        "\n\nCustomisation: " + userInput.getMessage();
                createCustomerOrderFile(lineToWrite);
            } else {
                // Let the customer know if they have made a mistake or missed a text book
                JOptionPane.showMessageDialog(mainFrame, "Please Enter your Full Name and Phone Number \n" +
                        "to place an order", "Error In your Information", JOptionPane.INFORMATION_MESSAGE, icon);
            }

        };
        // Add the action listener to the button and return the button
        submit.addActionListener(actionListener);
        return submit;
    }

    /**
     * Method to take a passed in string and write it to a file of the users order
     * @param orderContent - A String containing the users name, number, and order
     */
    public static void createCustomerOrderFile(String orderContent){
        // Initialise a string to contain the filepath of where we will save our order
        String orderPath = userInput.getName().replace(" ","_")+"_query.txt";
        Path path = Path.of(orderPath);

        // Try write the content of the passed in string to a file, alert the customer of the outcome.
        try {
            Files.writeString(path, orderContent);
            JOptionPane.showMessageDialog(mainFrame,"Thank you for your order. \nWe be cooking up a storm for you now. \nClick close to end this transaction."
                    ,"Message Sent", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }catch (IOException io){
            JOptionPane.showMessageDialog(mainFrame,"Error: Your order could not be processed. Please try again"
                    ,null, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adapted for FindAPet.java Tutorial 10
     * A Method that takes a MenuItem selected by the user to purchase and returns the details of the item as a
     * textfield
     * @param menuItem - The order a user has selected
     * @return  itemDescription - A JTEXTFIELD containing the description of the item a user is ordering
     */
    public static JTextArea chosenItemDescription(MenuItem menuItem){
        // Create a Textarea filled in with the details of the item the customer has ordered
        JTextArea itemDescription = new JTextArea(menuItem.toString());

        // Ensure that the text field is not editable
        itemDescription.setEditable(false);

        // Make sure the text wraps to the next line and only wraps on spaces
        itemDescription.setLineWrap(true);
        itemDescription.setWrapStyleWord(true);
        return itemDescription;
    }

}


//// TODO - Check the 3rd viewing window isn't broken due to images
///**
// * A Method that takes in the users order and builds the main window for the customer to enter their details to
// * order their meal. It creates a series of panels using the customers choice of meal to display all the information
// * required and then sets the main frame to use the panels as the main view for the program, it also sends the
// * users information off to be turned into an order file.
// * @param chosenMeal - The users selected meal they would like to order
// */
//public static void placeOrder(MenuItem chosenMeal){
//
//    // Title for the top of the window including the name of the meal and instructions for the customer
//    JLabel paneTitle = new JLabel("To place an order for a "+chosenMeal.menuItemName()+" Please enter your details below  ");
//
//    // Create a ScrollPane and populate it with the details of the users choice of meal
//    JScrollPane jScrollPane = new JScrollPane(describeIndividualPet(chosenMeal));
//
//    // Set the size of the Scroll pane
//    jScrollPane.getViewport().setPreferredSize(new Dimension(300,150));
//
//    // Create a Panel to hold the panel that has the title, the image and the item description
//    JPanel controlPane = new JPanel();
//    controlPane.setPreferredSize(new Dimension(500, 600));
//    controlPane.setLayout(new BorderLayout());
//
//    // Create a panel to hold the title, image and item description
//    JPanel itemDescriptionPanel = new JPanel();
//    itemDescriptionPanel.setLayout(new BorderLayout());
//
//    // Create an ImageIcon of the chosen meal by looking up it's ID number and searching for the corresponding image
//    // in the image file
//    ImageIcon picOfFood = new ImageIcon(new ImageIcon("./images/"+ chosenMeal.menuItemIdentifier() +".png")
//            .getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
//
//    // Create a label passing in the imageicon
//    JLabel image = new JLabel(picOfFood);
//
//    // Add the title, Image and description to the panel
//    itemDescriptionPanel.add(paneTitle, BorderLayout.NORTH);
//    itemDescriptionPanel.add(image, BorderLayout.CENTER);
//    itemDescriptionPanel.add(jScrollPane, BorderLayout.SOUTH);
//
//    // Add padding to the left and right of the main container
//    controlPane.add(Box.createRigidArea(new Dimension(20,0)), BorderLayout.WEST);
//    controlPane.add(Box.createRigidArea(new Dimension(20,0)), BorderLayout.EAST);
//    controlPane.add(itemDescriptionPanel);
//
//    // Create a Panel and pass it the return value from the contactForm method
//    JPanel userInputPanel = userInput.orderForm();
//
//    // Create a button for confirmation by calling the submitOrderButton Method
//    JButton submit = submitOrderButton(chosenMeal);
//
//    // Create the main panel for the entire view, tweak its parameters and add all the other panels and the button
//    JPanel mainFramePanel = new JPanel();
//    mainFramePanel.setLayout(new BorderLayout());
//    mainFramePanel.add(controlPane,BorderLayout.NORTH);
//    mainFramePanel.add(userInputPanel,BorderLayout.CENTER);
//    mainFramePanel.add(Box.createRigidArea(new Dimension(20,0)),BorderLayout.WEST);
//    mainFramePanel.add(Box.createRigidArea(new Dimension(20,0)),BorderLayout.EAST);
//    mainFramePanel.add(submit,BorderLayout.SOUTH);
//
//    // Inform the MainFrame that we are changing which panel is our new viewing panel
//    userInformationView = mainFramePanel;
//    mainFrame.setContentPane(userInformationView);
//    mainFrame.revalidate();
//}



























