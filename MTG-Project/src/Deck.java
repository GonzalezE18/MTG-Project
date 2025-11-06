import java.util.*;

public class Deck {
    private int deckId;
    private String deckName;
    private String format;
    private Card commander;
    private List<Card> cards = new ArrayList<>();

    // Constructor
    public Deck(String deckName, String format) {
        this.deckName = deckName;
        this.format = format;
    }

    // Getters and Setters
    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Card getCommander() {
        return commander;
    }

    public void setCommander(Card commander) {
        this.commander = commander;
    }

    public List<Card> getCards() {
        return cards;
    }

    // Add a card to the deck
    public void addCard(Card card) {
        cards.add(card);
    }

    //Display a quick summary of the deck
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

    @Override
    public String toString() {
        return "Deck{" +
                "deckId=" + deckId +
                ", deckName='" + deckName + '\'' +
                ", format='" + format + '\'' +
                ", commander=" + (commander != null ? commander.getName() : "None") +
                ", totalCards=" + cards.size() +
                '}';
    }
}
