import java.util.Arrays;
import java.util.List;

public class DMSUtils {
    private static final List<String> VALID_TYPES = Arrays.asList(
        "land", "creature", "artifact", "enchantment", "planeswalker", "battle", "instant", "sorcery"
    );

    private static final List<String> VALID_RARITIES = Arrays.asList(
        "common", "uncommon", "rare", "mythic rare"
    );

    public static boolean validateCard(Card card) {
        if (card.getName().isEmpty()) {
            System.out.println("Error: Card name cannot be blank.");
            return false;
        }

        if (!card.getType().isEmpty() && !VALID_TYPES.contains(card.getType().toLowerCase())) {
            System.out.println("Error: Invalid card type.");
            return false;
        }

        if (!card.getRarity().isEmpty() && !VALID_RARITIES.contains(card.getRarity().toLowerCase())) {
            System.out.println("Error: Invalid card rarity.");
            return false;
        }

        if (card.getSetName().isEmpty()) {
            System.out.println("Error: Set name cannot be blank.");
            return false;
        }

        if (card.getCollectorNumber() <= 0) {
            System.out.println("Error: Collector number must be greater than 0.");
            return false;
        }

        if (card.getMarketValue() < 0) {
            System.out.println("Error: Market value cannot be negative.");
            return false;
        }

        return true;
    }
}

