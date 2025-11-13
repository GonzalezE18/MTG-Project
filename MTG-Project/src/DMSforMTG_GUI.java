import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Graphical User Interface (GUI) for the Magic: The Gathering Deck Management System.
 * <p>
 * This class provides a Swing-based interface for viewing, creating, updating,
 * and deleting Magic: The Gathering card records. It also includes a custom feature
 * for building Commander and Standard decks.
 * </p>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *     <li>Load cards from CSV file</li>
 *     <li>CRUD operations on card objects</li>
 *     <li>Build decks (Commander or Standard)</li>
 *     <li>Scrollable output panel</li>
 *     <li>Dark theme with Nimbus UI</li>
 * </ul>
 *
 * @author Emily
 * @version 1.0
 */
public class DMSforMTG_GUI extends JFrame {

    /** List holding all loaded card objects */
    private List<Card> collection = new ArrayList<>();

    /** Output display panel used to show card lists, results, and system messages */
    private JTextArea outputArea = new JTextArea();

    /**
     * Constructs the MTG GUI window and initializes all UI components,
     * buttons, themes, and event listeners.
     */
    public DMSforMTG_GUI() {

        // Load Nimbus theme
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        // Window theme colors
        Color bgColor = new Color(30, 30, 30);
        Color panelColor = new Color(45, 45, 45);
        Color textColor = new Color(220, 220, 220);

        // Window configuration
        setTitle("MTG Deck Management System (GUI)");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header label
        JLabel header = new JLabel("MTG Deck Management System", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(new Color(173, 216, 230));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // Output area
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBackground(bgColor);
        outputArea.setForeground(textColor);
        outputArea.setCaretColor(textColor);
        outputArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 8, 8));
        buttonPanel.setBackground(panelColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons
        JButton loadBtn = new JButton("Load Cards File");
        JButton addBtn = new JButton("Add Card");
        JButton viewBtn = new JButton("View Collection");
        JButton updateBtn = new JButton("Update Card");
        JButton removeBtn = new JButton("Remove Card");
        JButton deckBtn = new JButton("Build Deck");
        JButton clearBtn = new JButton("Clear Display");
        JButton exitBtn = new JButton("Exit");

        // Add buttons to panel
        buttonPanel.add(loadBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(deckBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(exitBtn);

        add(buttonPanel, BorderLayout.WEST);

        // Event listeners
        loadBtn.addActionListener(e -> loadCardsFromFile("src/cards.csv"));
        addBtn.addActionListener(e -> addCard());
        viewBtn.addActionListener(e -> viewCollection());
        updateBtn.addActionListener(e -> updateCard());
        removeBtn.addActionListener(e -> removeCard());
        deckBtn.addActionListener(e -> buildDeck());
        clearBtn.addActionListener(e -> outputArea.setText(""));
        exitBtn.addActionListener(e -> System.exit(0));

        // Auto-load card file
        loadCardsFromFile("src/cards.csv");
    }

    /**
     * Loads card data from a CSV file and stores them in the local collection.
     *
     * @param filename The path to the CSV file
     */
    private void loadCardsFromFile(String filename) {
        try (Scanner fileScanner = new Scanner(new File(filename))) {

            fileScanner.nextLine(); // Skip header
            collection.clear();

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty()) continue;

                // Handle CSV fields (including quoted values)
                List<String> partsList = new ArrayList<>();
                boolean inQuotes = false;
                StringBuilder current = new StringBuilder();

                for (char c : line.toCharArray()) {
                    if (c == '"') {
                        inQuotes = !inQuotes;
                    } else if (c == ',' && !inQuotes) {
                        partsList.add(current.toString());
                        current.setLength(0);
                    } else {
                        current.append(c);
                    }
                }
                partsList.add(current.toString());
                String[] parts = partsList.toArray(new String[0]);

                if (parts.length < 8) continue;

                String name = parts[0].replace("\"", "").trim();
                String manaCost = parts[1].trim();
                String type = parts[2].trim();
                String rarity = parts[3].trim();
                String setName = parts[4].trim();
                int collectorNumber = Integer.parseInt(parts[5].trim());
                double marketValue = Double.parseDouble(parts[6].trim());
                boolean foil = Boolean.parseBoolean(parts[7].trim());

                Card card = new Card(name, manaCost, type, rarity, setName, collectorNumber, marketValue, foil);
                collection.add(card);
            }

            outputArea.setText("Loaded " + collection.size() + " cards from " + filename + ".\n");

        } catch (Exception e) {
            outputArea.setText("Error loading cards: " + e.getMessage() + "\n");
        }
    }

    /**
     * Adds a card to the local collection using dialog prompts.
     * Displays the created card inside the output window.
     */
    private void addCard() {
        try {
            String name = JOptionPane.showInputDialog(this, "Card name:");
            if (name == null || name.isBlank()) return;

            String type = selectType();
            String manaCost = selectMana(type);
            String rarity = selectRarity();
            String setName = JOptionPane.showInputDialog(this, "Set name:");
            int collectorNumber = Integer.parseInt(JOptionPane.showInputDialog(this, "Collector number:"));
            double marketValue = Double.parseDouble(JOptionPane.showInputDialog(this, "Market value ($):"));
            int foilOption = JOptionPane.showConfirmDialog(this, "Is this card foil?", "Foil", JOptionPane.YES_NO_OPTION);
            boolean foil = foilOption == JOptionPane.YES_OPTION;

            Card card = new Card(name, manaCost, type, rarity, setName, collectorNumber, marketValue, foil);
            collection.add(card);

            outputArea.append("Card added:\n" + card + "\n\n");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Displays all cards currently loaded in the system.
     */
    private void viewCollection() {
        if (collection.isEmpty()) {
            outputArea.setText("No cards in collection.\n");
            return;
        }

        StringBuilder sb = new StringBuilder("--- Collection (" + collection.size() + " cards) ---\n");
        for (Card c : collection) sb.append(c).append("\n");

        outputArea.setText(sb.toString());
    }

    /**
     * Updates the market value of a selected card.
     * Prompts the user for card name and new value.
     */
    private void updateCard() {
        String name = JOptionPane.showInputDialog(this, "Enter card name to update:");
        if (name == null || name.isBlank()) return;

        for (Card c : collection) {
            if (c.getName().equalsIgnoreCase(name)) {
                try {
                    double newVal = Double.parseDouble(JOptionPane.showInputDialog(this, "New market value ($):"));
                    c.setMarketValue(newVal);
                    outputArea.append("Updated " + name + " to $" + newVal + "\n");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
        }

        outputArea.append("Card not found: " + name + "\n");
    }

    /**
     * Removes a card from the collection based on user input.
     */
    private void removeCard() {
        String name = JOptionPane.showInputDialog(this, "Enter card name to remove:");
        if (name == null || name.isBlank()) return;

        boolean removed = collection.removeIf(c -> c.getName().equalsIgnoreCase(name));
        outputArea.append(removed ? "Removed card: " + name + "\n" : "Card not found.\n");
    }

    /**
     * Creates a new deck using dialog prompts.
     * This is the GUI version of the "custom feature" required by the assignment.
     */
    private void buildDeck() {
        String[] formats = {"Commander", "Standard"};
        String format = (String) JOptionPane.showInputDialog(
                this, "Select Deck Format:", "Deck Builder",
                JOptionPane.PLAIN_MESSAGE, null, formats, "Commander");

        if (format == null) return;

        String deckName = JOptionPane.showInputDialog(this, "Enter deck name:");
        if (deckName == null || deckName.isBlank()) return;

        outputArea.append("Deck created: " + deckName + " (" + format + ")\n");
    }

    /**
     * Displays a selection dialog for choosing a card type.
     *
     * @return The selected card type
     */
    private String selectType() {
        String[] options = {"Land", "Creature", "Artifact", "Enchantment", "Planeswalker", "Battle", "Instant", "Sorcery"};
        return (String) JOptionPane.showInputDialog(this, "Select Type:", "Card Type",
                JOptionPane.PLAIN_MESSAGE, null, options, "Creature");
    }

    /**
     * Allows the user to choose one or more mana symbols for a card.
     *
     * @param type The card type (lands skip mana selection)
     * @return The mana cost string
     */
    private String selectMana(String type) {
        if (type.equalsIgnoreCase("Land")) return "None";

        String[] manaOptions = {"{W}", "{U}", "{B}", "{R}", "{G}", "{C}", "{X}"};
        StringBuilder mana = new StringBuilder();

        while (true) {
            String selected = (String) JOptionPane.showInputDialog(
                    this, "Select Mana (Cancel to finish):",
                    "Mana Cost", JOptionPane.PLAIN_MESSAGE,
                    null, manaOptions, "{W}");

            if (selected == null) break;
            mana.append(selected);
        }

        return mana.length() == 0 ? "None" : mana.toString();
    }

    /**
     * Displays a rarity selection dialog.
     *
     * @return The chosen card rarity
     */
    private String selectRarity() {
        String[] rarities = {"Common", "Uncommon", "Rare", "Mythic Rare"};
        return (String) JOptionPane.showInputDialog(this, "Select Rarity:",
                "Card Rarity", JOptionPane.PLAIN_MESSAGE, null, rarities, "Common");
    }

    /**
     * Launches the GUI application.
     *
     * @param args Command-line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DMSforMTG_GUI gui = new DMSforMTG_GUI();
            gui.setVisible(true);
        });
    }
}
