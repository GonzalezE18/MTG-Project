import java.util.*;

public class Deck {
    private String name;
    private String format; // "Commander" or "Standard"
    private List<Card> cards = new ArrayList<>();
    private Card commander; // only for Commander format

    public Deck(String name, String format) {
        this.name = name;
        this.format = format;
    }

    public String getName() { return name; }
    public String getFormat() { return format; }
    public List<Card> getCards() { return cards; }
    public Card getCommander() { return commander; }

    public void setCommander(Card commander) {
        this.commander = commander;
    }

    public boolean addCard(Card card) {
        if (format.equalsIgnoreCase("Commander")) {
            return addCommanderCard(card);
        } else if (format.equalsIgnoreCase("Standard")) {
            return addStandardCard(card);
        }
        return false;
    }

    private boolean addCommanderCard(Card card) {
        if (cards.size() >= 100) {
            System.out.println("Error: Commander decks can only contain 100 cards.");
            return false;
        }
        boolean isBasicLand = card.getType().equalsIgnoreCase("Land");
        if (!isBasicLand && cards.stream().anyMatch(c -> c.getName().equalsIgnoreCase(card.getName()))) {
            System.out.println("Error: Only one copy of each non-land card allowed in Commander.");
            return false;
        }
        cards.add(card);
        return true;
    }

    private boolean addStandardCard(Card card) {
        if (cards.size() >= 60) {
            System.out.println("Warning: Deck already has 60 cards (you may still add more).");
        }
        long copies = cards.stream().filter(c -> c.getName().equalsIgnoreCase(card.getName())).count();
        boolean isBasicLand = card.getType().equalsIgnoreCase("Land");
        if (!isBasicLand && copies >= 4) {
            System.out.println("Error: You can only include up to 4 copies of a card in Standard.");
            return false;
        }
        cards.add(card);
        return true;
    }

    public void showDeckSummary() {
        System.out.println("\n--- Deck Summary ---");
        System.out.println("Name: " + name);
        System.out.println("Format: " + format);
        if (format.equalsIgnoreCase("Commander")) {
            System.out.println("Commander: " + (commander != null ? commander.getName() : "Not selected"));
            System.out.println("Starting Life: 40");
            System.out.println("Deck Size: " + cards.size() + "/100");
        } else {
            System.out.println("Starting Life: 20");
            System.out.println("Deck Size: " + cards.size() + " cards (min 60)");
        }
        System.out.println("Cards:");
        for (Card c : cards) {
            System.out.println(" - " + c.getName() + " [" + c.getType() + "] (" + c.getManaCost() + ")");
        }
    }
}
