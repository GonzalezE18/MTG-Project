public class Card {
    private String name;
    private String manaCost;
    private String type;
    private String rarity;
    private String setName;
    private int collectorNumber;  
    private double marketValue;
    private boolean isFoil;

    public Card(String name, String manaCost, String type, String rarity,
                String setName, int collectorNumber, double marketValue, boolean isFoil) {
        this.name = name;
        this.manaCost = manaCost;
        this.type = type;
        this.rarity = rarity;
        this.setName = setName;
        this.collectorNumber = collectorNumber;
        this.marketValue = marketValue;
        this.isFoil = isFoil;
    }

    // Getters
    public String getName() { return name; }
    public String getManaCost() { return manaCost; }
    public String getType() { return type; }
    public String getRarity() { return rarity; }
    public String getSetName() { return setName; }
    public int getCollectorNumber() { return collectorNumber; }
    public double getMarketValue() { return marketValue; }
    public boolean isFoil() { return isFoil; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setManaCost(String manaCost) { this.manaCost = manaCost; }
    public void setType(String type) { this.type = type; }
    public void setRarity(String rarity) { this.rarity = rarity; }
    public void setSetName(String setName) { this.setName = setName; }
    public void setCollectorNumber(int collectorNumber) { this.collectorNumber = collectorNumber; }
    public void setMarketValue(double marketValue) { this.marketValue = marketValue; }
    public void setFoil(boolean foil) { this.isFoil = foil; }

    @Override
    public String toString() {
        return String.format(
            "%s (%s) - %s, %s, Set: %s, Collector #: %d, Value: $%.2f, Foil: %s",
            name, manaCost, type, rarity, setName, collectorNumber, marketValue,
            isFoil ? "Yes" : "No"
        );
    }
}
