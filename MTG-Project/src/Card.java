/**
 * Represents a Magic: The Gathering card in the MTG Deck Management System.
 * <p>
 * Each card includes essential information such as name, mana cost, type,
 * rarity, set name, collector number, market value, and foil status.
 * This class supports both database-loaded cards (with ID) and newly created
 * cards (without ID).
 * </p>
 *
 * <p><b>Used by:</b></p>
 * <ul>
 *     <li>{@link CardDAO} for database CRUD operations</li>
 *     <li>{@link DMSforMTG} and {@link DMSforMTG_GUI} for user interactions</li>
 *     <li>{@link Deck} when building decks</li>
 * </ul>
 *
 * @author Emily
 * @version 1.0
 */
public class Card {

    /** Unique identifier for the card (assigned by the database). */
    private int cardId;

    /** Card name given by Magic: The Gathering. */
    private String name;

    /** Mana cost written in symbols (e.g., "{1}{W}{U}"). */
    private String manaCost;

    /** The card type (e.g., Creature, Instant, Sorcery, Land). */
    private String type;

    /** Card rarity classification (Common, Uncommon, Rare, Mythic Rare). */
    private String rarity;

    /** The MTG set in which this card was printed. */
    private String setName;

    /** Collector number assigned within its set. */
    private int collectorNumber;

    /** Market value in U.S. dollars. */
    private double marketValue;

    /** Whether this card is a foil version. */
    private boolean foil;

    /**
     * Creates a new card without an assigned ID.
     * This constructor is used when a user adds a new card manually.
     *
     * @param name the name of the card
     * @param manaCost the mana cost symbols (or "None" for lands)
     * @param type the card type
     * @param rarity the rarity of the card
     * @param setName the set this card belongs to
     * @param collectorNumber the card's set number
     * @param marketValue the monetary value of the card
     * @param foil true if the card is foil, false otherwise
     */
    public Card(String name, String manaCost, String type, String rarity,
                String setName, int collectorNumber, double marketValue, boolean foil) {

        this.name = name;
        this.manaCost = manaCost;
        this.type = type;
        this.rarity = rarity;
        this.setName = setName;
        this.collectorNumber = collectorNumber;
        this.marketValue = marketValue;
        this.foil = foil;
    }

    /**
     * Creates a card with a database-assigned ID.
     * Used when loading cards from MySQL.
     *
     * @param cardId the unique card ID from the database
     * @param name the card name
     * @param manaCost the mana cost symbols
     * @param type the card type
     * @param rarity the rarity classification
     * @param setName the MTG set name
     * @param collectorNumber the collector number
     * @param marketValue the market value
     * @param foil whether the card is foil
     */
    public Card(int cardId, String name, String manaCost, String type, String rarity,
                String setName, int collectorNumber, double marketValue, boolean foil) {

        this(name, manaCost, type, rarity, setName, collectorNumber, marketValue, foil);
        this.cardId = cardId;
    }

    // ------------------ GETTERS & SETTERS ------------------

    /** @return the card's unique ID */
    public int getCardId() { return cardId; }

    /** @param cardId sets the database card ID */
    public void setCardId(int cardId) { this.cardId = cardId; }

    /** @return the card name */
    public String getName() { return name; }

    /** @param name sets the card name */
    public void setName(String name) { this.name = name; }

    /** @return the mana cost string */
    public String getManaCost() { return manaCost; }

    /** @param manaCost sets the mana cost */
    public void setManaCost(String manaCost) { this.manaCost = manaCost; }

    /** @return card type */
    public String getType() { return type; }

    /** @param type sets the card type */
    public void setType(String type) { this.type = type; }

    /** @return card rarity */
    public String getRarity() { return rarity; }

    /** @param rarity sets the card rarity */
    public void setRarity(String rarity) { this.rarity = rarity; }

    /** @return set name */
    public String getSetName() { return setName; }

    /** @param setName sets the set name */
    public void setSetName(String setName) { this.setName = setName; }

    /** @return collector number */
    public int getCollectorNumber() { return collectorNumber; }

    /** @param collectorNumber sets collector number */
    public void setCollectorNumber(int collectorNumber) { this.collectorNumber = collectorNumber; }

    /** @return market value */
    public double getMarketValue() { return marketValue; }

    /** @param marketValue sets the card's market price */
    public void setMarketValue(double marketValue) { this.marketValue = marketValue; }

    /** @return true if the card is foil */
    public boolean isFoil() { return foil; }

    /** @param foil sets foil status */
    public void setFoil(boolean foil) { this.foil = foil; }

    /**
     * Returns a formatted string representing card details.
     *
     * @return a readable card description
     */
    @Override
    public String toString() {
        return String.format(
                "%s [%s, %s, %s, Set: %s, #%d, $%.2f, Foil: %b]",
                name, manaCost, type, rarity, setName, collectorNumber, marketValue, foil
        );
    }
}
