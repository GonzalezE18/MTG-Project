import java.util.Arrays;
import java.util.List;

/**
 * Utility class containing helper methods for validating Magic: The Gathering card data.
 * <p>
 * This class ensures that each {@link Card} object meets specific validation rules
 * before being added to the collection or database. It checks the validity of types,
 * rarities, numeric values, and prevents blank or invalid fields.
 * </p>
 *
 * <p><b>Example usage:</b></p>
 * <pre>
 * Card card = new Card("Lightning Bolt", "{R}", "Instant", "Common", "Core Set", 150, 1.25, false);
 * boolean isValid = DMSUtils.validateCard(card);
 * </pre>
 *
 * @author Emily
 * @version 1.0
 */
public class DMSUtils {

    /** List of all valid Magic: The Gathering card types. */
    private static final List<String> VALID_TYPES = Arrays.asList(
            "land", "creature", "artifact", "enchantment", "planeswalker", "battle", "instant", "sorcery"
    );

    /** List of all valid Magic: The Gathering card rarities. */
    private static final List<String> VALID_RARITIES = Arrays.asList(
            "common", "uncommon", "rare", "mythic rare"
    );

    /**
     * Validates a {@link Card} object to ensure all properties meet expected rules.
     * <ul>
     *   <li>Card name must not be blank.</li>
     *   <li>Type and rarity must match known values.</li>
     *   <li>Collector number must be greater than zero.</li>
     *   <li>Market value must be zero or higher.</li>
     * </ul>
     *
     * @param card The {@link Card} object to validate
     * @return {@code true} if the card is valid, {@code false} otherwise
     */
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


