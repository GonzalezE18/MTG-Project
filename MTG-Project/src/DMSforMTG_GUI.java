import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

public class DMSforMTG_GUI extends JFrame {
    private List<Card> collection = new ArrayList<>();
    private JTextArea outputArea = new JTextArea();

    public DMSforMTG_GUI() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        // Color
        Color bgColor = new Color(30, 30, 30);
        Color panelColor = new Color(45, 45, 45);
        Color buttonColor = new Color(70, 130, 180);
        Color textColor = new Color(220, 220, 220);

        //Window
        setTitle("MTG Deck Management System (GUI)");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // header 
        JLabel header = new JLabel("MTG Deck Management System", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 20));
        header.setForeground(new Color(173, 216, 230));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // Display
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBackground(bgColor);
        outputArea.setForeground(textColor);
        outputArea.setCaretColor(textColor);
        outputArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Button
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 8, 8));
        buttonPanel.setBackground(panelColor);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton loadBtn = new JButton("Load Cards File");
        JButton addBtn = new JButton("Add Card");
        JButton viewBtn = new JButton("View Collection");
        JButton updateBtn = new JButton("Update Card");
        JButton removeBtn = new JButton("Remove Card");
        JButton deckBtn = new JButton("Build Deck");
        JButton clearBtn = new JButton("Clear Display");
        JButton exitBtn = new JButton("Exit");

        buttonPanel.add(loadBtn);
        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(removeBtn);
        buttonPanel.add(deckBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(exitBtn);

        add(buttonPanel, BorderLayout.WEST);

        // Actions
        loadBtn.addActionListener(e -> loadCardsFromFile("src/cards.csv"));
        addBtn.addActionListener(e -> addCard());
        viewBtn.addActionListener(e -> viewCollection());
        updateBtn.addActionListener(e -> updateCard());
        removeBtn.addActionListener(e -> removeCard());
        deckBtn.addActionListener(e -> buildDeck());
        clearBtn.addActionListener(e -> outputArea.setText(""));
        exitBtn.addActionListener(e -> System.exit(0));

        // Loading card File
        loadCardsFromFile("src/cards.csv");
    }

    // Loading cards
    private void loadCardsFromFile(String filename) {
        try (Scanner fileScanner = new Scanner(new File(filename))) {
            fileScanner.nextLine(); // Skip header line
            collection.clear();

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty()) continue;


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
            outputArea.setText(" Loaded " + collection.size() + " cards from " + filename + ".\n");
        } catch (Exception e) {
            outputArea.setText("Error loading cards: " + e.getMessage() + "\n");
        }
    }

    // Adding cards
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
            boolean foil = (foilOption == JOptionPane.YES_OPTION);

            Card card = new Card(name, manaCost, type, rarity, setName, collectorNumber, marketValue, foil);
            collection.add(card);
            outputArea.append(" Card added successfully!\n" + card + "\n\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Viewing entire collection
    private void viewCollection() {
        if (collection.isEmpty()) {
            outputArea.setText("No cards in collection.\n");
        } else {
            StringBuilder sb = new StringBuilder("\n--- Your Collection (" + collection.size() + " cards) ---\n");
            for (Card c : collection) sb.append(c).append("\n");
            outputArea.setText(sb.toString());
        }
    }

    // updating collection
    private void updateCard() {
        String name = JOptionPane.showInputDialog(this, "Enter card name to update:");
        if (name == null || name.isBlank()) return;

        for (Card c : collection) {
            if (c.getName().equalsIgnoreCase(name)) {
                try {
                    double newVal = Double.parseDouble(JOptionPane.showInputDialog(this, "New market value ($):"));
                    c.setMarketValue(newVal);
                    outputArea.append(" Market value updated for " + c.getName() + " → $" + newVal + "\n");
                    return;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
        outputArea.append(" Card not found: " + name + "\n");
    }

    //Removing Card
    private void removeCard() {
        String name = JOptionPane.showInputDialog(this, "Enter card name to remove:");
        if (name == null || name.isBlank()) return;

        boolean removed = collection.removeIf(c -> c.getName().equalsIgnoreCase(name));
        outputArea.append(removed ? "🗑️ Card removed: " + name + "\n" : " Card not found.\n");
    }

    // Building deck
    private void buildDeck() {
        String[] formats = {"Commander", "Standard"};
        String format = (String) JOptionPane.showInputDialog(this, "Select Deck Format:", "Deck Builder",
                JOptionPane.PLAIN_MESSAGE, null, formats, "Commander");
        if (format == null) return;

        String deckName = JOptionPane.showInputDialog(this, "Enter deck name:");
        if (deckName == null || deckName.isBlank()) return;

        Deck deck = new Deck(deckName, format);
        JOptionPane.showMessageDialog(this, "Deck \"" + deckName + "\" created in " + format + " format.");
        outputArea.append("🛠️ Deck created: " + deckName + " (" + format + ")\n");
    }

    // Menu Selection drop down
    private String selectType() {
        String[] options = {"Land", "Creature", "Artifact", "Enchantment", "Planeswalker", "Battle", "Instant", "Sorcery"};
        return (String) JOptionPane.showInputDialog(this, "Select Type:", "Card Type",
                JOptionPane.PLAIN_MESSAGE, null, options, "Creature");
    }

    private String selectMana(String type) {
        if (type.equalsIgnoreCase("Land")) return "None";

        String[] manaOptions = {"{W}", "{U}", "{B}", "{R}", "{G}", "{C}", "{X}"};
        StringBuilder mana = new StringBuilder();
        boolean done = false;

        while (!done) {
            String selected = (String) JOptionPane.showInputDialog(this,
                    "Select Mana Symbol (or Cancel to finish):", "Mana Cost",
                    JOptionPane.PLAIN_MESSAGE, null, manaOptions, "{W}");
            if (selected == null) break;
            mana.append(selected);
        }
        return mana.length() == 0 ? "None" : mana.toString();
    }

    private String selectRarity() {
        String[] rarities = {"Common", "Uncommon", "Rare", "Mythic Rare"};
        return (String) JOptionPane.showInputDialog(this, "Select Rarity:", "Card Rarity",
                JOptionPane.PLAIN_MESSAGE, null, rarities, "Common");
    }

    // Main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DMSforMTG_GUI gui = new DMSforMTG_GUI();
            gui.setVisible(true);
        });
    }
}
