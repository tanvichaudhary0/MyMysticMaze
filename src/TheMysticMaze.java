import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JTextField;
import java.awt.Color;



/**
 * TheMysticMaze is a Java Swing-based game window that simulates a maze game with a
 * grid, player stats, and menu options. This class handles the UI, background image,
 * custom fonts, and grid creation along with player interaction elements.
 */

public class TheMysticMaze extends JFrame {
	
	private static final long serialVersionUID = 1L;  // Added serialVersionUID to avoid warning

    private static final int GRID_SIZE = 7;  // 7x7 grid
    private static final int CELL_SIZE = 90; // Each cell is 100x100 pixels
    private static final int WINDOW_WIDTH = 1920;
    private static final int WINDOW_HEIGHT = 1080;

    /**
     * The background image for the game window.
     */
    private Image backgroundImage;
   
    /**
     * Constructor to set up the main game window, background image, default fonts, 
     * grid, and UI components.
     *
     * @param imagePath Path to the background image for the game window.
     */
    public TheMysticMaze(String imagePath) {
        super("THE MYSTIC MAZE");  // Set the title of the window
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);  // Set the window size to 1920x1080
        setResizable(false);
        setLocationRelativeTo(null);  // Center the frame on the screen
            
        try {
            // Use getResourceAsStream to load the image from within the JAR
        	backgroundImage = ImageIO.read(new File(imagePath));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load image: bg.jpg");
        }

        // Create a custom JPanel for the background
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image, scaled to fit the panel
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        backgroundPanel.setLayout(new BorderLayout());

        // Create a title panel for "The Mystic Maze"
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);  // Transparent background
     
        JLabel titleLabel = new JLabel("THE MYSTIC MAZE");
        titleLabel.setFont(new Font(Font.SERIF, Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 40)); // Adds 40 pixels padding to the right
        titlePanel.add(titleLabel);

        backgroundPanel.add(titlePanel, BorderLayout.NORTH);  // Add title to the top

        // Create the grid panel and add it to the center
        JPanel gridPanel = createGridPanel();
        JPanel gridContainer = new JPanel(new GridBagLayout());
        gridContainer.setOpaque(false); // Transparent to show the background
        gridContainer.add(gridPanel);
        gridContainer.setBorder(BorderFactory.createEmptyBorder(0, 30, 40, 0));

        backgroundPanel.add(gridContainer, BorderLayout.CENTER);

        // Create the menu options panel on the left
        JPanel menuPanelWithLabel = new JPanel();
        menuPanelWithLabel.setLayout(new BorderLayout());
        menuPanelWithLabel.setOpaque(false);

        // Create a label for "Ingredients"
        JLabel ingredientsLabel = new JLabel("        INGREDIENTS");
        ingredientsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        ingredientsLabel.setForeground(Color.WHITE);
        ingredientsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        menuPanelWithLabel.add(ingredientsLabel, BorderLayout.NORTH);  // Add Ingredients label to the top

        JPanel menuOptionsPanel = createMenuOptionsPanel();  // Create the actual menu options panel
        menuPanelWithLabel.add(menuOptionsPanel, BorderLayout.CENTER);  // Add menu options below the label

        backgroundPanel.add(menuPanelWithLabel, BorderLayout.WEST);  // Add the whole menu + label to the left

        // Create the player stats and chat area panel on the right
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false); // Transparent to show the background
        rightPanel.add(createPlayerStatsPanel(), BorderLayout.NORTH);
        rightPanel.add(createChatAreaPanel(), BorderLayout.SOUTH);

        backgroundPanel.add(rightPanel, BorderLayout.EAST);

        add(backgroundPanel);
        setVisible(true);
    }

    /**
     * Creates the menu options panel with clickable buttons. Each button triggers
     * a popup menu with relevant menu items (e.g., File, Game, Network, etc.).
     *
     * @return A JPanel containing the menu options.
     */
    private JPanel createMenuOptionsPanel() {
        JPanel menuOptionsPanel = new JPanel();
        menuOptionsPanel.setLayout(new GridLayout(3, 2, 70, 10)); // 3 rows, 2 columns
        // Add padding to the left (e.g., 20px from the left)
        menuOptionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 70, 0, 0)); // Adds 70 pixels padding to the left
        menuOptionsPanel.setOpaque(false);

        // Path to the icon images
        String[] iconPaths = {"src/file.png", "src/game.png", "src/network.png", "src/help.png", "src/language.png", "src/brick_full.png"};
        String[] menuOptions = {"File", "Game", "Network", "Help", "Language", "<html>Insert<br> Role</html>"};

        Font menuFont = new Font(Font.SANS_SERIF, Font.BOLD, 16);

        for (int i = 0; i < menuOptions.length; i++) {
            JButton button = new JButton(menuOptions[i]);
            button.setFont(menuFont);
            button.setHorizontalTextPosition(JButton.CENTER);
            button.setVerticalTextPosition(JButton.BOTTOM);
            button.setContentAreaFilled(false);
            button.setForeground(Color.WHITE);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setPreferredSize(new Dimension(90, 90));

            try {
                // Set icon for the button
                Image img = ImageIO.read(new File(iconPaths[i]));
                Image scaledImg = img.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(scaledImg));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Add the popup menu for each button
            JPopupMenu popupMenu = new JPopupMenu();

            // Define actions for each button based on its text (File, Game, Network, Help, Language, Insert Role)
            switch (menuOptions[i]) {
                case "File":
                    createFileMenu(popupMenu);  // Attach File-related menu items
                    break;
                case "Game":
                    createGameMenu(popupMenu);  // Attach Game-related menu items
                    break;
                case "Network":
                    createNetworkMenu(popupMenu);  // Attach Network-related menu items
                    break;
                case "Help":
                    createHelpMenu(popupMenu);  // Attach Help-related menu items
                    break;
                case "Language":
                    createLanguageMenu(popupMenu);  // Attach Language-related menu items
                    break;
                
                default:
                    break;
            }

            // Show the popup menu when the button is clicked
            button.addActionListener(e -> popupMenu.show(button, button.getWidth() / 2, button.getHeight() / 2));

            menuOptionsPanel.add(button);
        }

        return menuOptionsPanel;
    }

    /**
     * Creates a JPopupMenu for the File menu with options like Save Game and Exit.
     *
     * @param popupMenu The JPopupMenu to which the menu items are added.
     */
    private void createFileMenu(JPopupMenu popupMenu) {
        JMenuItem saveGame = new JMenuItem("Save Game");
        JMenuItem exit = new JMenuItem("Exit");

        // Add action listeners to each menu item
        saveGame.addActionListener(e -> JOptionPane.showMessageDialog(null, "Save Game functionality is not implemented yet."));
        exit.addActionListener(e -> JOptionPane.showMessageDialog(null, "Exit the game. Goodbye!"));  // Optionally close game

        popupMenu.add(saveGame);
        popupMenu.add(exit);
    }

    /**
     * Creates a JPopupMenu for the Game menu with options like Start New Game and Reset Game.
     *
     * @param popupMenu The JPopupMenu to which the menu items are added.
     */
    private void createGameMenu(JPopupMenu popupMenu) {
        JMenuItem startNewGame = new JMenuItem("Start New Game");
        JMenuItem resetGame = new JMenuItem("Reset Game");

        // Add action listeners to each menu item
        startNewGame.addActionListener(e -> JOptionPane.showMessageDialog(null, "Starting a new game..."));
        resetGame.addActionListener(e -> JOptionPane.showMessageDialog(null, "Resetting the current game..."));

        popupMenu.add(startNewGame);
        popupMenu.add(resetGame);
    }

    /**
     * Creates a JPopupMenu for the Network menu with options like Connect and Disconnect.
     *
     * @param popupMenu The JPopupMenu to which the menu items are added.
     */
    private void createNetworkMenu(JPopupMenu popupMenu) {
        JMenuItem connect = new JMenuItem("Connect");
        JMenuItem disconnect = new JMenuItem("Disconnect");

        // Add action listeners to each menu item
        connect.addActionListener(e -> JOptionPane.showMessageDialog(null, "Connecting to the network..."));
        disconnect.addActionListener(e -> JOptionPane.showMessageDialog(null, "Disconnecting from the network..."));

        popupMenu.add(connect);
        popupMenu.add(disconnect);
    }

    /**
     * Creates a JPopupMenu for the Help menu with an About option.
     *
     * @param popupMenu The JPopupMenu to which the menu items are added.
     */
    private void createHelpMenu(JPopupMenu popupMenu) {
        JMenuItem about = new JMenuItem("About");

        // Add action listener to About menu item
        about.addActionListener(e -> JOptionPane.showMessageDialog(null, "The Mystic Maze - About: This is a maze adventure game."));

        popupMenu.add(about);
    }

    /**
     * Creates a JPopupMenu for the Language menu with options like English and French.
     *
     * @param popupMenu The JPopupMenu to which the menu items are added.
     */
    private void createLanguageMenu(JPopupMenu popupMenu) {
        JMenuItem english = new JMenuItem("English");
        JMenuItem french = new JMenuItem("French");

        // Add action listeners to each menu item
        english.addActionListener(e -> JOptionPane.showMessageDialog(null, "Language set to English."));
        french.addActionListener(e -> JOptionPane.showMessageDialog(null, "Language set to French."));

        popupMenu.add(english);
        popupMenu.add(french);
    }


    /**
     * Creates the main grid panel with tiles and optional icons. This grid panel forms
     * the main playing area of the game.
     *
     * @return A JPanel containing the grid structure.
     */
    private JPanel createGridPanel() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE, 0, 0)); // No gaps between tiles
        gridPanel.setPreferredSize(new Dimension(630, 630));
       // gridPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));

        int[][] mazeStructure = {
                {1, 1, 2, 4, 2, 1, 6},
                {7, 3, 5, 3, 8, 5, 8},
                {11, 7, 5, 10, 1, 6, 5},
                {3, 2, 4, 3, 8, 6, 11},
                {11, 10, 11, 7, 4, 10, 8},
                {3, 5, 10, 2, 6, 1, 5},
                {7, 4, 10, 8, 10, 10, 8}
        };

        String[] tilePaths = {
                "src/brick_SE.png", "src/brick_Tsouth.png", "src/brick_Teast.png",
                "src/hallway_horiz.png", "src/brick_Twest.png", "src/brick_SW.png",
                "src/brick_NE.png", "src/brick_NW.png", "src/brick_Twest.png",
                "src/brick_Tnorth.png", "src/hallway_vert.png", "src/brick_Teast.png",
                "src/brick_NW.png", "src/brick_SE.png", "src/brick_NE.png"
        };

        // Create a 2D array representing the grid with both number icons and player positions
        Object[][] gridLayout = {
            {null, null, null, null, null, null, null},  // First row (empty)
            {null, "green_16.png", "gold_14.png", "green_6.png", "gold_1.png", "green_8.png", null},  // Second row
            {null, "green_2.png", "yellow.png", "green_5.png", "red.png", "green_20.png", null},      // Third row
            {null, "green_7.png", "green_8.png", "green_25.png", "green_9.png", "green_13.png", null},  // Fourth row
            {null, "green_18.png", "green.png", "green_4.png", "blue.png", "gold_11.png", null},      // Fifth row
            {null, "green_20.png", "gold_19.png", "green_3.png", "green_7.png", "green_14.png", null}, // Sixth row
            {null, null, null, null, null, null, null} // Empty row at the bottom
        };

        // Loop through the maze structure and grid layout to create tiles and place icons
        for (int row = 0; row < gridLayout.length; row++) {
            for (int col = 0; col < gridLayout[row].length; col++) {
                JPanel cell = new JPanel();
                cell.setPreferredSize(new Dimension(90, 90)); // Adjust cell size
                cell.setLayout(new OverlayLayout(cell)); // Using OverlayLayout for stacking icons over tiles

                // Get the tile type from the maze structure
                int tileType = mazeStructure[row][col];
                try {
                    // Load and scale the tile image
                    Image img = ImageIO.read(new File(tilePaths[tileType - 1]));
                    Image scaledImg = img.getScaledInstance(90, 90, Image.SCALE_SMOOTH); // Adjust size
                    JLabel tileLabel = new JLabel(new ImageIcon(scaledImg));
                    cell.add(tileLabel); // Add the tile to the cell
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Check if there is an icon (player or number) to place at this position
                String iconPath = (String) gridLayout[row][col];
                if (iconPath != null) {
                    try {
                        // Load and scale the player or number icon
                        Image img = ImageIO.read(new File("src/" + iconPath));
                        Image scaledImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Adjust size as necessary
                        JLabel iconLabel = new JLabel(new ImageIcon(scaledImg));

                        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);  // Center horizontally
                        iconLabel.setVerticalAlignment(SwingConstants.CENTER);    // Center vertically
                        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0)); // Adds 10px padding to the left side

                        iconLabel.setOpaque(false);  // Ensure the label itself does not have a background

                        // Add the icon on top of the tile in the cell
                        cell.add(iconLabel, 0);  // Add icon at the top level
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // Add the complete cell (with tile and icon) to the grid panel
                gridPanel.add(cell);
            }
        }

        gridPanel.setOpaque(false); // Make grid transparent to show background
        return gridPanel;
    }
    
    /* Method to create a button panel with arrows at specific positions */
    
    private JPanel createButtonPanel(String imagePath, int numButtons, Dimension size, int positions) {
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;

        for (int i = 0; i < numButtons; i++) {
            JButton button = null;
            if (contains(positions, i)) {
                try {
                    Image img = ImageIO.read(new File(imagePath));
                    Image scaledImg = img.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
                    button = new JButton(new ImageIcon(scaledImg));
                } catch (IOException e) {
                    e.printStackTrace();
                    button = new JButton("X");
                }

                button.setPreferredSize(size);
                button.setBorder(BorderFactory.createEmptyBorder());
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.addActionListener(e -> System.out.println("Button clicked"));

                gbc.gridx = i;
                buttonPanel.add(button, gbc);
            }
        }

        return buttonPanel;
    }
   
    
    private JPanel createGridWithArrows() {
        // Create the main panel for the entire grid and arrows setup
        JPanel mainPanel = new JPanel(new BorderLayout()); // Using BorderLayout to place the arrows and grid

        // Create the grid panel (this method should be the one that creates the maze grid)
        JPanel gridPanel = createGridPanel(); // The method you have that generates the maze grid
        gridPanel.setPreferredSize(new Dimension(630, 630)); // Ensure grid size remains 630x630

        // Create a new panel for the top arrows (Insert Down arrows)
        JPanel topArrowPanel = new JPanel(new GridLayout(1, GRID_SIZE)); 
        topArrowPanel.setOpaque(false); 
        topArrowPanel.setPreferredSize(new Dimension(630, 30)); // Fixed height for top arrows

        for (int i = 0; i < GRID_SIZE; i++) {
            if (i == 1 || i == 3 || i == 5) {
                JLabel downArrowLabel = new JLabel(new ImageIcon(new ImageIcon("src/insert down.png").getImage().getScaledInstance(90, 30, Image.SCALE_SMOOTH)));
                downArrowLabel.setHorizontalAlignment(SwingConstants.CENTER);
                downArrowLabel.setVerticalAlignment(SwingConstants.CENTER);
                topArrowPanel.add(downArrowLabel);
            } else {
                topArrowPanel.add(new JLabel()); // Empty space for non-arrow positions
            }
        }

        // Create a new panel for the bottom arrows (Insert Up arrows)
        JPanel bottomArrowPanel = new JPanel(new GridLayout(1, GRID_SIZE)); 
        bottomArrowPanel.setOpaque(false);
        bottomArrowPanel.setPreferredSize(new Dimension(630, 30)); // Fixed height for bottom arrows

        for (int i = 0; i < GRID_SIZE; i++) {
            if (i == 1 || i == 3 || i == 5) {
                JLabel upArrowLabel = new JLabel(new ImageIcon(new ImageIcon("src/insert up.png").getImage().getScaledInstance(90, 30, Image.SCALE_SMOOTH)));
                upArrowLabel.setHorizontalAlignment(SwingConstants.CENTER);
                upArrowLabel.setVerticalAlignment(SwingConstants.CENTER);
                bottomArrowPanel.add(upArrowLabel);
            } else {
                bottomArrowPanel.add(new JLabel()); // Empty space for non-arrow positions
            }
        }

        // Create left arrow panel (Insert Right arrows)
        JPanel leftArrowPanel = new JPanel(new GridLayout(GRID_SIZE, 1)); 
        leftArrowPanel.setOpaque(false); 
        leftArrowPanel.setPreferredSize(new Dimension(30, 630)); // Fixed width for left arrows

        for (int i = 0; i < GRID_SIZE; i++) {
            if (i == 1 || i == 3 || i == 5) {
                JLabel rightArrowLabel = new JLabel(new ImageIcon(new ImageIcon("src/insert right.png").getImage().getScaledInstance(30, 90, Image.SCALE_SMOOTH)));
                rightArrowLabel.setHorizontalAlignment(SwingConstants.CENTER);
                rightArrowLabel.setVerticalAlignment(SwingConstants.CENTER);
                leftArrowPanel.add(rightArrowLabel);
            } else {
                leftArrowPanel.add(new JLabel()); // Empty space for non-arrow positions
            }
        }

        // Create right arrow panel (Insert Left arrows)
        JPanel rightArrowPanel = new JPanel(new GridLayout(GRID_SIZE, 1)); 
        rightArrowPanel.setOpaque(false);
        rightArrowPanel.setPreferredSize(new Dimension(30, 630)); // Fixed width for right arrows

        for (int i = 0; i < GRID_SIZE; i++) {
            if (i == 1 || i == 3 || i == 5) {
                JLabel leftArrowLabel = new JLabel(new ImageIcon(new ImageIcon("src/insert left.png").getImage().getScaledInstance(30, 90, Image.SCALE_SMOOTH)));
                leftArrowLabel.setHorizontalAlignment(SwingConstants.CENTER);
                leftArrowLabel.setVerticalAlignment(SwingConstants.CENTER);
                rightArrowPanel.add(leftArrowLabel);
            } else {
                rightArrowPanel.add(new JLabel()); // Empty space for non-arrow positions
            }
        }

        // Add the arrow panels and grid panel to the main panel
        mainPanel.add(topArrowPanel, BorderLayout.NORTH);  // Add top (Insert Down arrows)
        mainPanel.add(bottomArrowPanel, BorderLayout.SOUTH); // Add bottom (Insert Up arrows)
        mainPanel.add(leftArrowPanel, BorderLayout.WEST); // Add left (Insert Right arrows)
        mainPanel.add(rightArrowPanel, BorderLayout.EAST); // Add right (Insert Left arrows)
        mainPanel.add(gridPanel, BorderLayout.CENTER);    // Add the grid at the center

        return mainPanel; // Return the main panel with the grid and arrows
    }

    /**
     * Creates a panel displaying player statistics and current game information.
     *
     * @return A JPanel containing player stats and game status.
     */ 
    private JPanel createPlayerStatsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setOpaque(false);  // To match the theme's transparency

        // Add padding to the left (e.g., 20px from the left)
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 30)); // Adds 20 pixels padding to the left

        // Title
        JLabel titleLabel = new JLabel("PLAYER'S STAT");
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        statsPanel.add(titleLabel);

        // Instruction label
        JLabel instructionLabel = new JLabel("<html>Select a row or column to insert <br> the piece</html>");
        instructionLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        statsPanel.add(instructionLabel);

        // Player icons and stats with stars
        String[] playerIcons = {"src/green.png", "src/blue.png", "src/yellow.png", "src/red.png"};
        String[] players = {"Player 1", "Player 2", "Player 3", "Player 4"};
        String[][] starIcons = {
            {"src/greenstar.png"},  // Player 1: 1 green star
            {"src/bluestar.png", "src/bluestar.png", "src/bluestar.png"},  // Player 2: 3 blue stars
            {"src/yellowstar.png", "src/yellowstar.png"},  // Player 3: 2 yellow stars
            {"src/redstar.png", "src/redstar.png"}  // Player 4: 2 red stars
        };

        for (int i = 0; i < players.length; i++) {
            try {
                // Load and resize player icon
                ImageIcon playerIcon = new ImageIcon(ImageIO.read(new File(playerIcons[i])));
                Image scaledPlayerImg = playerIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);  // Resize player icon to 30x30
                playerIcon = new ImageIcon(scaledPlayerImg);

                // Create player label
                JLabel playerLabel = new JLabel(players[i], playerIcon, JLabel.LEFT);
                playerLabel.setFont(new Font("Arial", Font.PLAIN, 15));
                playerLabel.setForeground(Color.WHITE);
                playerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                // Create a panel for player and stars together
                JPanel playerPanel = new JPanel();
                playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));  // Arrange them horizontally
                playerPanel.setOpaque(false); // Transparent background
                playerPanel.add(playerLabel);  // Add player label
                playerPanel.add(Box.createRigidArea(new Dimension(10, 0)));  // Add space between player and stars

                // Add stars to the panel
                for (String starIconPath : starIcons[i]) {
                    ImageIcon starIcon = new ImageIcon(ImageIO.read(new File(starIconPath)));
                    Image scaledStarImg = starIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);  // Resize star icon to 30x30
                    starIcon = new ImageIcon(scaledStarImg);
                    JLabel starLabel = new JLabel(starIcon);
                    playerPanel.add(starLabel);  // Add star label next to player
                }

                // Align the playerPanel and add to the main panel
                playerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                statsPanel.add(Box.createVerticalStrut(5));  // Adds vertical space between elements
                statsPanel.add(playerPanel);  // Add player panel to the main stats panel
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Dynamic label for current component to capture
        JLabel currentComponentLabel = new JLabel("Current component to capture is #3");
        currentComponentLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        currentComponentLabel.setForeground(Color.WHITE);
        currentComponentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space before this label
        statsPanel.add(currentComponentLabel);

        return statsPanel;
    }

    /**
     * Creates the chat area where players can type and send messages, 
     * and displays the game chat history.
     *
     * @return A JPanel containing the chat area and input field.
     */
    private JPanel createChatAreaPanel() {
        JPanel chatPanel = new JPanel();
        chatPanel.setOpaque(false);  // Make the main chat panel transparent
        chatPanel.setLayout(new BorderLayout());
        chatPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 30));

        // Create a text area for displaying chat messages (non-editable)
        JTextArea chatDisplay = new JTextArea();
        chatDisplay.setEditable(false);  // Prevent users from editing chat history
        chatDisplay.setLineWrap(true);   // Line wrap for long messages
        chatDisplay.setWrapStyleWord(true);  // Wrap words instead of breaking them
        chatDisplay.setOpaque(false);  // Make background transparent
        chatDisplay.setForeground(Color.WHITE);  // Set the text color to black for better readability
        chatDisplay.setFont(new Font("Arial", Font.PLAIN, 20));  // Set font for chat messages
        chatDisplay.append("GAME INITIALIZED!!\n\n");  // Default text in the chat area

        // Add the chat display to a scroll pane to allow scrolling through messages
        JScrollPane chatScrollPane = new JScrollPane(chatDisplay);
        chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);  // Always show vertical scroll bar
        chatScrollPane.getVerticalScrollBar().setUI(new BrownScrollBarUI());
        chatScrollPane.setPreferredSize(new Dimension(300, 250));  // Set size of the chat display area
        chatScrollPane.setOpaque(false);  // Make the scroll pane transparent
        chatScrollPane.getViewport().setOpaque(false);  // Ensure the viewport of the scroll pane is transparent
        chatScrollPane.setBorder(BorderFactory.createEmptyBorder());  // Remove border around the scroll pane

        
     // Create a text field for users to type their messages with default placeholder text
        JTextField chatInputField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(new ImageIcon("src/box.png").getImage(), 0, 0, getWidth(), getHeight(), null);  // Chat box image as background
                super.paintComponent(g);
            }
        };

        // Add default placeholder text "Type to chat here..."
        chatInputField.setText(" Type to chat here...");
        chatInputField.setForeground(Color.WHITE);  // Set the placeholder text color

        // Add a focus listener to clear the placeholder when the field gains focus and restore it when it loses focus
        chatInputField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (chatInputField.getText().equals(" Type to chat here...")) {
                    chatInputField.setText("");  // Clear the placeholder
                    chatInputField.setForeground(Color.WHITE);  // Set the regular text color
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (chatInputField.getText().isEmpty()) {
                    chatInputField.setText(" Type to chat here...");  // Restore the placeholder
                    chatInputField.setForeground(Color.GRAY);  // Set placeholder text color again
                }
            }
        });

        chatInputField.setOpaque(false);  // Make input field transparent
        chatInputField.setForeground(Color.WHITE);  // Set input text color to black
        chatInputField.setFont(new Font("Arial", Font.PLAIN, 16));  // Set font for input text
        chatInputField.setBorder(null);  // Remove border around the input field

        // Action listener for sending chat messages
        chatInputField.addActionListener(e -> {
            String message = chatInputField.getText();  // Get the message from input
            if (!message.trim().isEmpty()) {
                chatDisplay.append("Player: " + message + "\n");  // Append the message to chat display
                chatInputField.setText("");  // Clear the input field after sending
            }
            // Automatically scroll to the bottom after each message
            chatDisplay.setCaretPosition(chatDisplay.getDocument().getLength());
        });

        // Chat icon on the right
        JLabel chatIcon = new JLabel(new ImageIcon(new ImageIcon("src/icon.png").getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
        
        // Panel to hold chat box and icon
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setOpaque(false);  // Make input panel transparent
        inputPanel.add(chatInputField, BorderLayout.CENTER);  // Chat input box in the center
        inputPanel.add(chatIcon, BorderLayout.WEST);  // Chat icon on the right

        // Add components to the chat panel
        chatPanel.add(chatScrollPane, BorderLayout.CENTER);  // Add the chat history (scrollable)
        chatPanel.add(inputPanel, BorderLayout.SOUTH);  // Add the input panel (text field and icon)

        return chatPanel;
    }
    /**
     * Custom ScrollBarUI for chat display to set a brown color for the scrollbar.
     */
    private static class BrownScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        private final Dimension d = new Dimension();

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(d);
            button.setMinimumSize(d);
            button.setMaximumSize(d);
            return button;
        }

        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(139, 69, 19);  // Brown color for the thumb
            this.trackColor = new Color(222, 184, 135);  // Light brown for the track
        }
    }
    
    
    /**
     * Main method to run the game application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Path to the background image (update this to your actual image path)
        String imagePath = "src/bg.jpg";
        // Create an instance of TheStoneMaze with the background image
        new TheMysticMaze(imagePath);
    }
}
