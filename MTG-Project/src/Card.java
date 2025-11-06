public class Card {
    private int cardId;
    private String name;
    private String manaCost;
    private String type;
    private String rarity;
    private String setName;
    private int collectorNumber;
    private double marketValue;
    private boolean foil;

    // Constructor without ID
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

    // Constructor with ID
    public Card(int cardId, String name, String manaCost, String type, String rarity,
                String setName, int collectorNumber, double marketValue, boolean foil) {
        this(name, manaCost, type, rarity, setName, collectorNumber, marketValue, foil);
        this.cardId = cardId;
    }

    //Getters and Setters
    public int getCardId() { return cardId; }
    public void setCardId(int cardId) { this.cardId = cardId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getManaCost() { return manaCost; }
    public void setManaCost(String manaCost) { this.manaCost = manaCost; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRarity() { return rarity; }
    public void setRarity(String rarity) { this.rarity = rarity; }

    public String getSetName() { return setName; }
    public void setSetName(String setName) { this.setName = setName; }

    public int getCollectorNumber() { return collectorNumber; }
    public void setCollectorNumber(int collectorNumber) { this.collectorNumber = collectorNumber; }

    public double getMarketValue() { return marketValue; }
    public void setMarketValue(double marketValue) { this.marketValue = marketValue; }

    public boolean isFoil() { return foil; }
    public void setFoil(boolean foil) { this.foil = foil; }

    @Override
    public String toString() {
        return String.format("%s [%s, %s, %s, Set: %s, #%d, $%.2f, Foil: %b]",
                name, manaCost, type, rarity, setName, collectorNumber, marketValue, foil);
    }
}
