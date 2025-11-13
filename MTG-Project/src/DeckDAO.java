import java.sql.*;
import java.sql.*;

/**
 * Data Access Object (DAO) class for handling Deck operations.
 * <p>
 * Provides methods to insert decks, add cards to decks, and display deck contents.
 * Uses {@link DatabaseConnection} for establishing database connectivity.
 * </p>
 *
 * @author Emily
 * @version 1.0
 */
public class DeckDAO {

    /**
     * Adds a new deck to the database.
     *
     * @param deck The {@link Deck} to insert
     * @return The generated deck ID, or -1 if the operation fails
     */
    public int addDeck(Deck deck) {
        String sql = "INSERT INTO Deck (deckName, format, commanderId) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, deck.getDeckName());
            stmt.setString(2, deck.getFormat());
            if (deck.getCommander() != null)
                stmt.setInt(3, deck.getCommander().getCardId());
            else
                stmt.setNull(3, Types.INTEGER);

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int deckId = rs.getInt(1);
                deck.setDeckId(deckId);
                System.out.println("Deck created successfully (ID: " + deckId + ")");
                return deckId;
            }
        } catch (SQLException e) {
            System.out.println("Error adding deck: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Adds a card to an existing deck.
     *
     * @param deckId   The ID of the deck
     * @param cardId   The ID of the card
     * @param quantity The quantity of the card
     */
    public void addCardToDeck(int deckId, int cardId, int quantity) {
        String sql = """
            INSERT INTO DeckCards (deckId, cardId, quantity)
            VALUES (?, ?, ?)
            ON DUPLICATE KEY UPDATE quantity = quantity + ?
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deckId);
            stmt.setInt(2, cardId);
            stmt.setInt(3, quantity);
            stmt.setInt(4, quantity);
            stmt.executeUpdate();
            System.out.println("Card added to deck successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding card to deck: " + e.getMessage());
        }
    }

    /**
     * Displays all cards in a given deck.
     *
     * @param deckId The deck’s ID
     */
    public void viewDeck(int deckId) {
        String deckSql = "SELECT deckName, format FROM Deck WHERE deckId = ?";
        String cardsSql = """
            SELECT c.name, c.manaCost, c.type, c.rarity, c.setName, dc.quantity
            FROM DeckCards dc
            JOIN Card c ON dc.cardId = c.cardId
            WHERE dc.deckId = ?
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement deckStmt = conn.prepareStatement(deckSql);
             PreparedStatement cardsStmt = conn.prepareStatement(cardsSql)) {

            deckStmt.setInt(1, deckId);
            ResultSet deckRs = deckStmt.executeQuery();
            if (deckRs.next()) {
                System.out.println("\nDeck: " + deckRs.getString("deckName") +
                        " (" + deckRs.getString("format") + ")");
            }

            cardsStmt.setInt(1, deckId);
            ResultSet cardRs = cardsStmt.executeQuery();
            System.out.println("\n--- Cards in Deck ---");
            while (cardRs.next()) {
                System.out.printf("%s x%d [%s, %s, %s, %s]%n",
                        cardRs.getString("name"),
                        cardRs.getInt("quantity"),
                        cardRs.getString("manaCost"),
                        cardRs.getString("type"),
                        cardRs.getString("rarity"),
                        cardRs.getString("setName"));
            }
        } catch (SQLException e) {
            System.out.println("Error viewing deck: " + e.getMessage());
        }
    }
}
