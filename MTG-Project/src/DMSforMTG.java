import java.util.*;

/**
 * The main driver class for the Magic: The Gathering Deck Management System.
 * <p>
 * This class provides a text-based menu interface allowing users to perform
 * CRUD operations (Create, Read, Update, Delete) on cards stored in a MySQL database,
 * as well as to build and view Commander or Standard decks.
 * </p>
 *
 * @author Emily Gonzalez
 * @version 1.0
 */
public class DMSforMTG {
    /** Scanner object for user input */
    private Scanner scanner = new Scanner(System.in);
    /** DAO for performing database operations on Card objects */
    private CardDAO cardDAO = new CardDAO();
    /** DAO for performing database operations on Deck objects */
    private DeckDAO deckDAO = new DeckDAO();

    /**
     * The main entry point for the MTG Deck Management System.
     * Initializes the system and launches the main menu loop.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        DMSforMTG app = new DMSforMTG();
        app.run();
    }

    /**
     * Displays the main menu and handles user input to access CRUD and deck-building features.
     */
    public void run() {
        while (true) {
            System.out.println("\n--- MTG Deck Management System ---");
            System.out.println("1. Add Card");
            System.out.println("2. View Collection");
            System.out.println("3. Update Card");
            System.out.println("4. Remove Card");
            System.out.println("5. Build Deck");
            System.out.println("6. Exit");
            System.out.print("Select an option: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1" -> addCard();
                case "2" -> viewCollection();
                case "3" -> updateCard();
                case "4" -> removeCard();
                case "5" -> buildDeck();
                case "6" -> {
                    System.out.println("Exiting program. Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please choose between 1 and 6.");
            }
        }
    }

    /**
     * Prompts the user to enter card information and saves the card into the MySQL database.
     * Validates input data using {@link DMSUtils#validateCard(Card)}.
     */
    private void addCard() {
        System.out.print("Card name: ");
        String name = scanner.nextLine();

        // Type selection
        String type = "";
        while (true) {
            System.out.println("\nSelect Card Type:");
            System.out.println("1. Land");
            System.out.println("2. Creature");
            System.out.println("3. Artifact");
            System.out.println("4. Enchantment");
            System.out.println("5. Planeswalker");
            System.out.println("6. Battle");
            System.out.println("7. Spell (Instant or Sorcery)");
            System.out.print("Enter choice (1–7): ");

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
                        System.out.println("Invalid spell type. Try again.");
                        continue;
                    }
                }
                default -> {
                    System.out.println("Invalid type. Try again.");
                    continue;
                }
            }
            break;
        }

        String manaCost = selectManaCost(type);

        // Rarity selection
        String rarity = "";
        while (true) {
            System.out.println("\nSelect Rarity:");
            System.out.println("1. Black = Common");
            System.out.println("2. Silver = Uncommon");
            System.out.println("3. Gold = Rare");
            System.out.println("4. Orange = Mythic Rare");
            System.out.print("Enter choice (1–4): ");
            String input = scanner.nextLine();

            switch (input) {
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

        System.out.print("Set name: ");
        String setName = scanner.nextLine();

        System.out.print("Collector Number: ");
        int collectorNumber = Integer.parseInt(scanner.nextLine());

        System.out.print("Market value ($): ");
        double marketValue = Double.parseDouble(scanner.nextLine());

        System.out.print("Is it foil? (yes/no): ");
        boolean foil = scanner.nextLine().equalsIgnoreCase("yes");

        Card card = new Card(name, manaCost, type, rarity, setName, collectorNumber, marketValue, foil);

        if (DMSUtils.validateCard(card)) {
            cardDAO.addCard(card);
        } else {
            System.out.println("Invalid card data. Please check your input.");
        }
    }

    /**
     * Displays all cards currently in the database.
     */
    private void viewCollection() {
        List<Card> cards = cardDAO.getAllCards();
        if (cards.isEmpty()) {
            System.out.println("No cards found in the database.");
        } else {
            System.out.println("\n--- Your Collection ---");
            for (Card card : cards) {
                System.out.println(card);
            }
        }
    }

    /**
     * Updates the market value of a specified card.
     * Prompts the user for the card name and new value.
     */
    private void updateCard() {
        System.out.print("Enter card name to update: ");
        String name = scanner.nextLine();

        System.out.print("Enter new market value ($): ");
        double newValue = Double.parseDouble(scanner.nextLine());

        cardDAO.updateMarketValue(name, newValue);
    }

    /**
     * Removes a card from the database by name.
     */
    private void removeCard() {
        System.out.print("Enter card name to remove: ");
        String name = scanner.nextLine();
        cardDAO.deleteCard(name);
    }

    /**
     * Builds a deck (Commander or Standard format) and stores it in the database.
     * The deck and its associated cards are saved using {@link DeckDAO}.
     */
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

        // Save deck
        int deckId = deckDAO.addDeck(deck);
        if (deckId == -1) {
            System.out.println("Error: Deck could not be saved.");
            return;
        }

        // Add cards
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

    /**
     * Allows users to input multiple mana symbols for a card’s mana cost.
     *
     * @param type The card type (Land cards will skip mana selection)
     * @return The formatted mana cost string
     */
    private String selectManaCost(String type) {
        if (type.equalsIgnoreCase("Land")) {
            return "None";
        }

        StringBuilder manaCost = new StringBuilder();
        while (true) {
            System.out.println("\nSelect Mana Cost Symbol (type 'done' when finished):");
            System.out.println("1. Plains = {W}");
            System.out.println("2. Island = {U}");
            System.out.println("3. Swamp = {B}");
            System.out.println("4. Mountain = {R}");
            System.out.println("5. Forest = {G}");
            System.out.println("6. Colorless = {C}");
            System.out.println("7. Mixed / Multicolor = {X}");
            System.out.println("8. Numeric (Enter {1}, {2}, etc.)");
            System.out.print("Enter choice (1–8 or 'done'): ");

            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("done")) break;

            switch (input) {
                case "1" -> manaCost.append("{W}");
                case "2" -> manaCost.append("{U}");
                case "3" -> manaCost.append("{B}");
                case "4" -> manaCost.append("{R}");
                case "5" -> manaCost.append("{G}");
                case "6" -> manaCost.append("{C}");
                case "7" -> manaCost.append("{X}");
                case "8" -> {
                    System.out.print("Enter numeric value: ");
                    String num = scanner.nextLine().trim();
                    if (num.matches("\\d+")) manaCost.append("{").append(num).append("}");
                    else System.out.println("Invalid numeric input.");
                }
                default -> System.out.println("Invalid option. Try again.");
            }

            System.out.println("Current mana cost: " + manaCost);
        }

        return manaCost.length() > 0 ? manaCost.toString() : "None";
    }
}
