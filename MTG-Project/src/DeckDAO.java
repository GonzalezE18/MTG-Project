import java.sql.*;
import java.util.*;

public class DeckDAO {
    //Insert a new deck into the database
    public int addDeck(Deck deck) {
        String sql = "INSERT INTO Deck (deckName, format, commanderId) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, deck.getDeckName());
            stmt.setString(2, deck.getFormat());
            if (deck.getCommander() != null) {
                stmt.setInt(3, deck.getCommander().getCardId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int deckId = rs.getInt(1);
                deck.setDeckId(deckId);
                System.out.println("Deck saved with ID: " + deckId);
                return deckId;
            }
        } catch (SQLException e) {
            System.out.println("Error adding deck: " + e.getMessage());
        }
        return -1;
    }

    //Add cards to a deck
    public void addCardToDeck(int deckId, int cardId, int quantity) {
        String sql = "INSERT INTO DeckCards (deckId, cardId, quantity) VALUES (?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE quantity = quantity + ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deckId);
            stmt.setInt(2, cardId);
            stmt.setInt(3, quantity);
            stmt.setInt(4, quantity);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding card to deck: " + e.getMessage());
        }
    }

    // Display deck with all cards
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
                System.out.println("\nDeck: " + deckRs.getString("deckName") + " (" + deckRs.getString("format") + ")");
            }

            cardsStmt.setInt(1, deckId);
            ResultSet cardRs = cardsStmt.executeQuery();

            System.out.println("\n--- Cards in Deck ---");
            while (cardRs.next()) {
                System.out.printf("%s x%d [%s, %s, %s, %s]\n",
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
