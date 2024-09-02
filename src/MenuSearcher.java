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

    // Initialize our default JFrame and Panels to be used throughout the class
    private static JFrame mainFrame;
    private static JPanel defaultPane = null;
    private static JPanel userInformationView = null; //view 3
    private static JPanel orderPane = null;

    // Initialize our user input class to be used throughout the class
    private static UserInput userInput;

    // Initialize the enum type to be used throughout the class
    private static Type type;




    private static JComboBox<String> optionsCombo = null;

    public static void main(String[] args) {
        menu = loadMenu(filePath);

        // Create the main frame for our gui
        mainFrame = new JFrame(appName);

        // Set basic settings for our gui
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setIconImage(icon.getImage());
        mainFrame.setMinimumSize(new Dimension(400, 900));

        // Assign the output of our main panel method to the set content pane method of the JFrame to initialise our gui
        defaultPane = mainPanel();
        mainFrame.setContentPane(defaultPane);

        // Jam all our panels into the frame and make it visible
        mainFrame.pack();
        mainFrame.setVisible(true);

//        DreamMenuItem dreamMenuItem = getFilters();
//        processSearchResults(dreamMenuItem);
//        System.exit(0);
    }

    public static JPanel mainPanel() {
        JPanel mainWindowPanel = new JPanel();
        userInput = new UserInput(menu);

        mainWindowPanel.setLayout(new BorderLayout());

        JButton searchButt = new JButton("Search for yo' berger");
        ActionListener listener = e -> searchForItems(userInput);
        searchButt.addActionListener(listener);

//        JLabel mainImage = new JLabel(new ImageIcon("gobbledy_geek_graphic.png"));
//        mainImage.setSize(new Dimension(200, 200));
//        mainWindowPanel.add(mainImage, BorderLayout.WEST);

        mainWindowPanel.add(userInput.generateWindow(), BorderLayout.CENTER);
        mainWindowPanel.add(searchButt, BorderLayout.SOUTH);

        return mainWindowPanel;
    }

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
            // Add bun and sauce if the user has chosen burger
            String bun = userInput.getBun();
            if (Objects.equals(bun, "NA")) {
                List<Object> allBuns = new ArrayList<>(menu.getAllIngredientTypes(Filter.BUN));
                filtersMap.put(Filter.MEAT, allBuns);
            } else {
                filtersMap.put(Filter.MEAT, bun);
            }


            Set<Object> sauces = userInput.getSauce();
            if (!sauces.isEmpty()) {
                filtersMap.put(Filter.SAUCE_S, sauces);
            }
        } else {
            // Add dressing, cucumber and leafy greens if the user has chosen salad
            Dressing dressing = userInput.getDressing();
            filtersMap.put(Filter.DRESSING, dressing);

            String cucumber = userInput.getCucumber();
            if (cucumber.equals("I don't mind")) {
                List<Object> cucumberList = new ArrayList<>(menu.getAllIngredientTypes(Filter.CUCUMBER));
                filtersMap.put(Filter.CUCUMBER, cucumberList);
            }
            else {
                filtersMap.put(Filter.CUCUMBER, cucumber);
            }




            Set<Object> leafy = userInput.getLeafy();
            if (!leafy.isEmpty()) {
                filtersMap.put(Filter.LEAFY_GREENS, leafy);
            }
        }

        String pickles = userInput.getPickles();
        if (pickles.equals("I don't mind")) {
            List<Object> picklesList = new ArrayList<>(menu.getAllIngredientTypes(Filter.PICKLES));
            filtersMap.put(Filter.PICKLES, picklesList);
        }
        else {
            filtersMap.put(Filter.PICKLES, pickles);
        }

        String tomato = userInput.getTomato();
        if (tomato.equals("I don't mind")) {
            List<Object> tomatoList = new ArrayList<>(menu.getAllIngredientTypes(Filter.TOMATO));
        filtersMap.put(Filter.TOMATO, tomatoList);
        }
        else {
        filtersMap.put(Filter.TOMATO, tomato);
        }

        Meat meat = userInput.getMeat();
        if (meat == Meat.NA) {
            List<Meat> allMeat = new ArrayList<>(List.of(Meat.values()));
            filtersMap.put(Filter.MEAT, allMeat);
        }
        else {
            filtersMap.put(Filter.MEAT, meat);
        }

        if (userInput.getCheese()) {
            filtersMap.put(Filter.CHEESE, true);
        }
        else {
            filtersMap.put(Filter.CHEESE, false);
        }

        float minimumPrice = userInput.getMinPrice();
        float maximumPrice = userInput.getMaxPrice();

        DreamMenuItem dreamMenuItem = new DreamMenuItem(filtersMap, minimumPrice, maximumPrice);
        List<MenuItem> potentialMatches = menu.findMatch(dreamMenuItem);

        resultsMainPanel(potentialMatches);
//        processSearchResults(dreamMenuItem);

    }

    // Adapted from SeekAGeek.java Lecture 10
    public static void resultsMainPanel(List<MenuItem> potentialMatches) {

        // If no matches are found in the database
        if(potentialMatches.isEmpty()){
            // Call noMatches method to display a dialog informing the customer of the result.
            noMatches();
            return;
        }
        // Generate the Base panel for the search results to be displayed on.
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.add(Box.createRigidArea(new Dimension(0,10)),BorderLayout.NORTH); //add padding to the top of the panel
        main.add(generateResultsList(potentialMatches),BorderLayout.CENTER); //add the scroll pane - containing geek descriptions
        main.add(selectFromResultsList(potentialMatches),BorderLayout.SOUTH); //add the dropdown list and search again button to the bottom
        main.add(Box.createRigidArea(new Dimension(20,0)),BorderLayout.WEST); //add padding on the left/right sides of the panel
        main.add(Box.createRigidArea(new Dimension(20,0)),BorderLayout.EAST);
        mainFrame.setContentPane(main); //set main window (JFrame) to the results panel (view 2)
        mainFrame.revalidate();

    }

    // Adapted from SeekAGeek.java Lecture 10
    public static void noMatches() {
        JOptionPane.showMessageDialog(mainFrame, "No meals for you bitch. \n", "No meals Found", JOptionPane.INFORMATION_MESSAGE, icon);
        reGenerateSearchView();
    }

    // Adapted from SeekAGeek.java Lecture 10
    public static void reGenerateSearchView(){
        defaultPane = mainPanel();
        mainFrame.setContentPane(defaultPane);
        mainFrame.revalidate();
    }

    // Adapted from SeekAGeek.java Lecture 10
    public static JScrollPane generateResultsList(List<MenuItem> potentialMatches) {
        //this array will contain all the user's options - a collection of geek names they can choose from
        String[] options = new String[potentialMatches.size()];
        //panel to contain one text area per geek (each text area describes 1 geek)
        JPanel mealDescriptions = new JPanel();
        mealDescriptions.setBorder(BorderFactory.createTitledBorder("Matches found!! The following geeks meet your criteria: "));
        mealDescriptions.setLayout(new BoxLayout(mealDescriptions,BoxLayout.Y_AXIS)); //stack vertically
        mealDescriptions.add(Box.createRigidArea(new Dimension(0,10))); //padding

        //loop through the matches, generating a description of each - description varies based on the type of relationship the user is after
        for (int i = 0; i < potentialMatches.size(); i++) {
            JTextArea mealDescription = new JTextArea();
            if (type.equals(Type.BURGER)) mealDescription = new JTextArea(potentialMatches.get(i).getMenuItemInformation());
            if (type.equals(Type.SALAD)) mealDescription = new JTextArea(potentialMatches.get(i).getMenuItemInformation());

            mealDescription.setEditable(false); //ensure the description can't be edited!
            //this will ensure that if the description is long, it 'overflows'
            mealDescription.setLineWrap(true);
            mealDescription.setWrapStyleWord(true); //ensure words aren't broken across new lines
            //add the text area to the above panel
            mealDescriptions.add(mealDescription);//add the text area to the panel above
            options[i] = potentialMatches.get(i).getMenuItemName();  //populate the array used for the dropdown list
        }
        //next, initialise the combo box with the geek names (key set)
        optionsCombo = new JComboBox<>(options);

        //add a scroll pane to the results window, so that if there are many results, users can scroll as needed
        JScrollPane verticalScrollBar = new JScrollPane(mealDescriptions);
        verticalScrollBar.setPreferredSize(new Dimension(300, 450));
        verticalScrollBar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //this positions the scrollbar at the top (without it, the scrollbar loads part way through
        //adding of the text areas to the JPanel, resulting in the scrollbar being halfway down the results
        SwingUtilities.invokeLater(() -> verticalScrollBar.getViewport().setViewPosition( new Point(0, 0) ));
        return verticalScrollBar;
    }

    // Adapted from seekAGeek.java Lecture 10
    public static JPanel selectFromResultsList(List<MenuItem> potentialMatches) {
        //give the user the option to search again if they don't like their results
        JLabel noneMessage = new JLabel("If none meet your criteria, close to exit, or search again with different criteria");
        JButton editSearchCriteriaButton = new JButton("Search again");
        ActionListener actionListenerEditCriteria = e -> reGenerateSearchView();
        editSearchCriteriaButton.addActionListener(actionListenerEditCriteria);

        //the user must choose from one of the real geeks - set the default string to instruct them
        String defaultOption = "Select geek";
        optionsCombo.addItem(defaultOption);
        optionsCombo.setSelectedItem(defaultOption);
        //if the user selects an option, e.g. a geek from the dropdown list, this action listener will pick up on it
        ActionListener actionListener = e -> checkUserSelection(potentialMatches);
        optionsCombo.addActionListener(actionListener);

        //create a panel for the button and the dropdown list - this has a flowlayout - left to right placement
        JPanel buttonOptionPanel = new JPanel();
        buttonOptionPanel.add(optionsCombo);
        buttonOptionPanel.add(editSearchCriteriaButton);

        //create and return a panel containing the panel with button/dropdown list, as well as a border/title and padding
        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel,BoxLayout.Y_AXIS)); //stack vertically
        selectionPanel.add(Box.createRigidArea(new Dimension(0,10)));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Please select which geek you'd like to contact:"));
        selectionPanel.add(noneMessage);
        selectionPanel.add(buttonOptionPanel);
        selectionPanel.add(Box.createRigidArea(new Dimension(10,0)));
        return selectionPanel;
    }

    // Adapted for seekAGeek.java Lecture 10
    public static void checkUserSelection(List<MenuItem> potentialMatches){
        String decision = (String) optionsCombo.getSelectedItem();
        assert decision != null; //we know it can't be null
        //if the user has selected a real geek, see which one it is and return their details
        for (MenuItem g : potentialMatches) {
            if (decision.equals(g.getMenuItemName())) {
//                JOptionPane.showMessageDialog(mainFrame, g.getMenuItemInformation());
//                break; //once the matching geek is found, no need to keep looping
//                MenuItem chosenPet = options.get(petName);
                //this will switch the contents of the main frame to the user contact field with chosen pet's description
                placeAdoptionRequest(g);
            }
        }
    }

    // Adapted for FindAPet.java Tutorial 10
    public static void placeAdoptionRequest(MenuItem chosenMeal){
        //instruct the user to fill out the form
        JLabel petMessage = new JLabel("To place an adoption request for "+chosenMeal.getMenuItemName()+" fill in the form below");
        petMessage.setAlignmentX(0);
        JScrollPane jScrollPane = new JScrollPane(describeIndividualPet(chosenMeal));
        jScrollPane.getViewport().setPreferredSize(new Dimension(300,150));
        jScrollPane.setAlignmentX(0);

        //add both the instruction and pet description to a new panel
        JPanel petDescriptionPanel = new JPanel();
        petDescriptionPanel.setAlignmentX(0);
        petDescriptionPanel.add(petMessage);
        petDescriptionPanel.add(jScrollPane);

        //use the contactForm method to get a panel containing components that allow the user to input info
        JPanel userInputPanel = userInput.contactForm();
        userInputPanel.setAlignmentX(0);
        //create a button, which when clicked, writes the user's request to a file
        JButton submit = new JButton("Submit");
        ActionListener actionListener = e -> {
            String lineToWrite = "Name: "+userInput.getName()+" \nEmail: "+userInput.getEmail()+"\nPhone number: "
                    +userInput.getPhoneNumber()+"\n\nMessage: "+userInput.getMessage()+
                    "\n\n"+userInput.getName()+" wishes to adopt "+chosenMeal.getMenuItemName()+" ("+chosenMeal.getMenuItemName()+")";
            writeMessageToFile(lineToWrite);
        };
        submit.addActionListener(actionListener);

        //add the pet description panel, contact form panel and button to a new frame, and assign it to view 3
        JPanel mainFramePanel = new JPanel();
        mainFramePanel.setLayout(new BorderLayout());
        mainFramePanel.add(petDescriptionPanel,BorderLayout.NORTH);
        mainFramePanel.add(userInputPanel,BorderLayout.CENTER);
        mainFramePanel.add(Box.createRigidArea(new Dimension(20,0)),BorderLayout.WEST);
        mainFramePanel.add(Box.createRigidArea(new Dimension(20,0)),BorderLayout.EAST);
        mainFramePanel.add(submit,BorderLayout.SOUTH);

        userInformationView = mainFramePanel;
        mainFrame.setContentPane(userInformationView);
        mainFrame.revalidate();
    }

    // Adapted for FindAPet.java Tutorial 10
    public static void writeMessageToFile(String lineToWrite){
        String filePath = userInput.getName().replace(" ","_")+"_query.txt";
        Path path = Path.of(filePath);
        try {
            Files.writeString(path, lineToWrite);
            JOptionPane.showMessageDialog(mainFrame,"Thank you for your message. \nOne of our friendly staff will be in touch shortly. \nClose this dialog to terminate."
                    ,"Message Sent", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }catch (IOException io){
            JOptionPane.showMessageDialog(mainFrame,"Error: Message could not be sent! Please try again!"
                    ,null, JOptionPane.ERROR_MESSAGE);
        }
    }

    // Adapted for FindAPet.java Tutorial 10
    public static JTextArea describeIndividualPet(MenuItem menuItem){
        //create a text area to contain the pet description
        JTextArea petDescription = new JTextArea(menuItem.getMenuItemInformation());
        petDescription.setEditable(false);
        //this will ensure that if the description is long, it 'overflows'
        petDescription.setLineWrap(true);
        petDescription.setWrapStyleWord(true);
        return petDescription;
    }

    public static Menu loadMenu(String filePath) {
        Menu menu = new Menu();
        Path path = Path.of(filePath);
        List<String> fileContents = null;
        try {
            fileContents = Files.readAllLines(path);
        }catch (IOException io){
            System.out.println("File could not be found");
            System.exit(0);
        }

        for(int i=1;i<fileContents.size();i++){

            String[] info = fileContents.get(i).split("\\[");
            String[] singularInfo = info[0].split(",");

            String leafyGreensRaw = info[1].replace("]","");
            String saucesRaw = info[2].replace("]","");
            String description = info[3].replace("]","");

            long menuItemIdentifier = 0;
            try{
                menuItemIdentifier = Long.parseLong(singularInfo[0]);
            }catch (NumberFormatException n) {
                System.out.println("Error in file. Menu item identifier could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+n.getMessage());
                System.exit(0);
            }

            Type type = null;
            try{
                type = Type.valueOf(singularInfo[1].toUpperCase().strip());
            }catch (IllegalArgumentException e){
                System.out.println("Error in file. Type data could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                System.exit(0);
            }

            String menuItemName = singularInfo[2];

            double price = 0;
            try{
                price = Double.parseDouble(singularInfo[3]);
            }catch (NumberFormatException n){
                System.out.println("Error in file. Price could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+n.getMessage());
                System.exit(0);
            }

            String bun = singularInfo[4].toLowerCase().strip();

            Meat meat = null;
            try {
                meat = Meat.valueOf(singularInfo[5].toUpperCase());
            }catch (IllegalArgumentException e){
                System.out.println("Error in file. Meat data could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                System.exit(0);
            }

            boolean cheese = false;
            String cheeseRaw = singularInfo[6].strip().toUpperCase();
            if(cheeseRaw.equals("YES")) cheese = true;

            boolean pickles = false;
            String pickleRaw = singularInfo[7].strip().toUpperCase();
            if(pickleRaw.equals("YES")) pickles = true;

            boolean cucumber = false;
            String cucumberRaw = singularInfo[8].strip().toUpperCase();
            if(cucumberRaw.equals("YES")) cucumber = true;

            boolean tomato = false;
            String tomatoRaw = singularInfo[9].strip().toUpperCase();
            if(tomatoRaw.equals("YES")) tomato = true;

            Dressing dressing = null;
            try {
                dressing = Dressing.valueOf(singularInfo[10].toUpperCase().replace(" ","_"));
            }catch (IllegalArgumentException e){
                System.out.println("Error in file. Dressing data could not be parsed for item on line "+(i+1)+". Terminating. \nError message: "+e.getMessage());
                System.exit(0);
            }

            Set<String> leafyGreens = new HashSet<>();
            for(String l: leafyGreensRaw.split(",")){
                leafyGreens.add(l.toLowerCase().strip());
            }

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

            Map<Filter,Object> filterMap = new LinkedHashMap<>();
            filterMap.put(Filter.TYPE,type);
            if(type.equals(Type.BURGER)){
                filterMap.put(Filter.BUN, bun);
                if(sauces.size()>0) filterMap.put(Filter.SAUCE_S,sauces);
            }
            if(!meat.equals(Meat.NA)) filterMap.put(Filter.MEAT,meat);
            filterMap.put(Filter.PICKLES, pickles);
            filterMap.put(Filter.CHEESE, cheese);
            filterMap.put(Filter.TOMATO, tomato);
            if(type.equals(Type.SALAD)){
                filterMap.put(Filter.DRESSING,dressing);
                filterMap.put(Filter.LEAFY_GREENS,leafyGreens);
                filterMap.put(Filter.CUCUMBER, cucumber);
            }

            DreamMenuItem dreamMenuItem = new DreamMenuItem(filterMap);
            MenuItem menuItem = new MenuItem(menuItemIdentifier, menuItemName,price,description, dreamMenuItem);
            menu.addItem(menuItem);
        }
        return menu;
    }


}






























