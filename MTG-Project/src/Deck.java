import java.util.*;

/**
 * Represents a Magic: The Gathering deck.
 * <p>
 * Supports Commander and Standard formats, allowing users to
 * associate cards, track a commander, and summarize contents.
 * </p>
 *
 * @author Emily
 * @version 1.0
 */
public class Deck {
    private int deckId;
    private String deckName;
    private String format;
    private Card commander;
    private List<Card> cards = new ArrayList<>();

    /**
     * Constructs a new Deck object.
     *
     * @param deckName The name of the deck
     * @param format   The format (Commander or Standard)
     */
    public Deck(String deckName, String format) {
        this.deckName = deckName;
        this.format = format;
    }

    /** @return the deck’s unique ID */
    public int getDeckId() { return deckId; }

    /** @param deckId sets the deck’s unique ID */
    public void setDeckId(int deckId) { this.deckId = deckId; }

    /** @return the deck’s name */
    public String getDeckName() { return deckName; }

    /** @param deckName sets the deck’s name */
    public void setDeckName(String deckName) { this.deckName = deckName; }

    /** @return the deck’s format (Commander or Standard) */
    public String getFormat() { return format; }

    /** @param format sets the deck’s format */
    public void setFormat(String format) { this.format = format; }

    /** @return the Commander card */
    public Card getCommander() { return commander; }

    /** @param commander sets the Commander card */
    public void setCommander(Card commander) { this.commander = commander; }

    /** @return all cards in this deck */
    public List<Card> getCards() { return cards; }

    /**
     * Adds a card to the deck list.
     *
     * @param card The {@link Card} to add
     */
    public void addCard(Card card) { cards.add(card); }

    /**
     * Prints a simple deck summary including commander and card count.
     */
    public void showDeckSummary() {
        System.out.println("\n--- Deck Summary ---");
        System.out.println("Deck Name: " + deckName);
        System.out.println("Format: " + format);
        if (commander != null) {
            System.out.println("Commander: " + commander.getName());
        }
        System.out.println("Total Cards: " + cards.size());
        for (Card c : cards) {
            System.out.println(" - " + c.getName() + " (" + c.getType() + ")");
        }
    }
}
