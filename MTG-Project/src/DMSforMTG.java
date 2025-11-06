import java.util.*;

public class DMSforMTG {
    private List<Card> collection = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private CardDAO cardDAO = new CardDAO();
    private DeckDAO deckDAO = new DeckDAO();


    public static void main(String[] args) {
        DMSforMTG app = new DMSforMTG();
        app.loadCardsFromFile("src/cards.csv");
        app.run();
    }

    public void run() {
        while (true) {
            System.out.println("\n--- MTG Deck Management System ---"); // main menu
            System.out.println("1. Add Card");
            System.out.println("2. View Collection");
            System.out.println("3. Update Card");
            System.out.println("4. Remove Card");
            System.out.println("5. Build Deck");
            System.out.println("6. Exit");
            System.out.print("Select an option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> addCard();
                case 2 -> viewCollection();
                case 3 -> updateCard();
                case 4 -> removeCard();
                case 5 -> buildDeck();
                case 6 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addCard() {
        System.out.print("Card name: "); //card name
        String name = scanner.nextLine();

        // TYPE SELECTION MENU
        String type = "";
        while (true) {
            System.out.println("\nSelect Card Type:"); // type menu
            System.out.println("1. Land");
            System.out.println("2. Creature");
            System.out.println("3. Artifact");
            System.out.println("4. Enchantment");
            System.out.println("5. Planeswalker");
            System.out.println("6. Battle");
            System.out.println("7. Spell (Instant or Sorcery)");
            System.out.print("Enter choice (1 through 7): ");

            String input = scanner.nextLine();

            switch (input) {
                case "1" -> type = "Land";
                case "2" -> type = "Creature";
                case "3" -> type = "Artifact";
                case "4" -> type = "Enchantment";
                case "5" -> type = "Planeswalker";
                case "6" -> type = "Battle";
                case "7" -> {
                    System.out.print("Is it an Instant or Sorcery? ");
                    String spellType = scanner.nextLine().trim().toLowerCase();
                    if (spellType.equals("instant") || spellType.equals("sorcery")) {
                        type = spellType.substring(0, 1).toUpperCase() + spellType.substring(1);
                    } else {
                        System.out.println("Invalid spell type. Please choose again.");
                        continue;
                    }
                }
                default -> {
                    System.out.println("Invalid selection. Try again.");
                    continue;
                }
            }
            break;
        }

        // MANA COST MENU (supports multiple)
        String manaCost = selectManaCost(type);

        // RARITY MENU
        String rarity = "";
        while (true) {
            System.out.println("\nSelect Rarity:");
            System.out.println("1. Black = Common");
            System.out.println("2. Silver = Uncommon");
            System.out.println("3. Gold = Rare");
            System.out.println("4. Orange = Mythic Rare");
            System.out.print("Enter choice (1–4): ");
            String rarityChoice = scanner.nextLine();

            switch (rarityChoice) {
                case "1" -> rarity = "Common";
                case "2" -> rarity = "Uncommon";
                case "3" -> rarity = "Rare";
                case "4" -> rarity = "Mythic Rare";
                default -> {
                    System.out.println("Invalid rarity. Try again.");
                    continue;
                }
            }
            break;
        }

        System.out.print("Set name: "); // set name 
        String setName = scanner.nextLine();
        System.out.print("Collector Number: ");
        int collectorNumber = Integer.parseInt(scanner.nextLine());
        System.out.print("Market value ($): ");
        double marketValue = Double.parseDouble(scanner.nextLine());
        System.out.print("Is it foil? (yes/no): ");
        boolean foil = scanner.nextLine().equalsIgnoreCase("yes");

        Card card = new Card(name, manaCost, type, rarity, setName, collectorNumber, marketValue, foil);

        if (DMSUtils.validateCard(card)) {
            collection.add(card);
            System.out.println("Card added successfully!");
        } else {
            System.out.println("Error: Invalid card input.");
        }
    }
//view collection of cards
    private void viewCollection() {
        if (collection.isEmpty()) {
            System.out.println("No cards in collection.");
        } else {
            System.out.println("\n--- Your Collection ---");
            collection.forEach(System.out::println);
        }
    }

    private void updateCard() { //update cards market value
        System.out.print("Enter card name to update: ");
        String name = scanner.nextLine();
        for (Card c : collection) {
            if (c.getName().equalsIgnoreCase(name)) {
                System.out.print("Enter new market value ($): ");
                double newValue = Double.parseDouble(scanner.nextLine());
                c.setMarketValue(newValue);
                System.out.println("Market value updated successfully!");
                return;
            }
        }
        System.out.println("Card not found.");
    }

    private void removeCard() {
        System.out.print("Enter card name to remove: ");
        String name = scanner.nextLine();
        collection.removeIf(c -> c.getName().equalsIgnoreCase(name));
        System.out.println("Card removed (if it existed).");
    }

    private void loadCardsFromFile(String filename) {
        System.out.println("Current working directory: " + new java.io.File(".").getAbsolutePath());
        try (Scanner fileScanner = new Scanner(new java.io.File(filename))) {
            fileScanner.nextLine(); // Skip header line
            while (fileScanner.hasNextLine()) {
                String[] parts = fileScanner.nextLine().split(",");
                if (parts.length < 8) continue;

                String name = parts[0];
                String manaCost = parts[1];
                String type = parts[2];
                String rarity = parts[3];
                String setName = parts[4];
                int collectorNumber = Integer.parseInt(parts[5]);
                double marketValue = Double.parseDouble(parts[6]);
                boolean foil = Boolean.parseBoolean(parts[7]);

                Card card = new Card(name, manaCost, type, rarity, setName, collectorNumber, marketValue, foil);
                collection.add(card);
            }
            System.out.println("Loaded " + collection.size() + " cards from " + filename + ".");
        } catch (Exception e) {
            System.out.println("Error loading cards: " + e.getMessage());
        }
    }


    private void buildDeck() {
    System.out.println("\n--- Deck Builder ---");
    System.out.println("Choose Format:");
    System.out.println("1. Commander (EDH)");
    System.out.println("2. Standard");
    System.out.print("Enter choice (1 or 2): ");
    String choice = scanner.nextLine();

    String format = choice.equals("1") ? "Commander" : "Standard";
    System.out.print("Enter deck name: ");
    String deckName = scanner.nextLine();

    Deck deck = new Deck(deckName, format);
    List<Card> allCards = cardDAO.getAllCards();

    // Commander selection
    if (format.equals("Commander")) {
        System.out.println("\nSelect your Commander card:");
        allCards.forEach(c -> System.out.println("- " + c.getName()));
        System.out.print("Enter commander card name: ");
        String commanderName = scanner.nextLine();

        Optional<Card> commanderOpt = allCards.stream()
                .filter(c -> c.getName().equalsIgnoreCase(commanderName))
                .findFirst();

        commanderOpt.ifPresent(deck::setCommander);
    }

    // Save the deck first
    int deckId = deckDAO.addDeck(deck);
    if (deckId == -1) {
        System.out.println("Error: Deck could not be saved.");
        return;
    }

    // Add cards to the deck
    System.out.println("\nAdd cards to your " + format + " deck (type 'done' when finished):");
    while (true) {
        System.out.print("Enter card name to add: ");
        String cardName = scanner.nextLine();
        if (cardName.equalsIgnoreCase("done")) break;

        Optional<Card> found = allCards.stream()
                .filter(c -> c.getName().equalsIgnoreCase(cardName))
                .findFirst();

        if (found.isPresent()) {
            System.out.print("Enter quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());
            deckDAO.addCardToDeck(deckId, found.get().getCardId(), quantity);
        } else {
            System.out.println("Card not found.");
        }
    }

    System.out.println("\nDeck saved successfully!");
    deckDAO.viewDeck(deckId);
}


    // Multi-Mana Selection Menu
    private String selectManaCost(String type) {
        if (type.equalsIgnoreCase("Land")) {
            return "None"; // Lands don't have a mana cost
        }

        StringBuilder manaCost = new StringBuilder();
        while (true) {
            System.out.println("\nSelect Mana Cost Symbol (type 'done' when finished):");
            System.out.println("1. Plains = {W} (White)");
            System.out.println("2. Island = {U} (Blue)");
            System.out.println("3. Swamp = {B} (Black)");
            System.out.println("4. Mountain = {R} (Red)");
            System.out.println("5. Forest = {G} (Green)");
            System.out.println("6. Colorless = {C}");
            System.out.println("7. Mixed / Multicolor = {X}");
            System.out.println("8. Numeric (Enter value like 1, 2, 3 for {1}, {2}, {3})");
            System.out.print("Enter choice (1–8 or 'done'): ");

            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("done")) {
                break;
            }

            switch (choice) {
                case "1" -> manaCost.append("{W}");
                case "2" -> manaCost.append("{U}");
                case "3" -> manaCost.append("{B}");
                case "4" -> manaCost.append("{R}");
                case "5" -> manaCost.append("{G}");
                case "6" -> manaCost.append("{C}");
                case "7" -> manaCost.append("{X}");
                case "8" -> {
                    System.out.print("Enter numeric value (e.g. 1, 2, 3): ");
                    String value = scanner.nextLine().trim();
                    if (value.matches("\\d+")) {
                        manaCost.append("{").append(value).append("}");
                    } else {
                        System.out.println("Invalid numeric input.");
                        continue;
                    }
                }
                default -> {
                    System.out.println("Invalid option. Try again.");
                    continue;
                }
            }

            System.out.println("Current mana cost: " + manaCost);
        }

        return manaCost.length() > 0 ? manaCost.toString() : "None";
    }
}

