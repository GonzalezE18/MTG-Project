import java.sql.*;
import java.util.*;

/**
 * Data Access Object (DAO) class for interacting with the Card table.
 * <p>
 * Provides CRUD (Create, Read, Update, Delete) operations for managing
 * Magic: The Gathering card data stored in the MySQL database.
 * </p>
 *
 * @author Emily Gonzalez
 * @version 1.0
 */
public class CardDAO {

    /**
     * Adds a new card to the database.
     *
     * @param card The {@link Card} object to insert
     */
    public void addCard(Card card) {
        String sql = "INSERT INTO Card (name, manaCost, type, rarity, setName, collectorNumber, marketValue, isFoil) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, card.getName());
            stmt.setString(2, card.getManaCost());
            stmt.setString(3, card.getType());
            stmt.setString(4, card.getRarity());
            stmt.setString(5, card.getSetName());
            stmt.setInt(6, card.getCollectorNumber());
            stmt.setDouble(7, card.getMarketValue());
            stmt.setBoolean(8, card.isFoil());

            stmt.executeUpdate();
            System.out.println("Card added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding card: " + e.getMessage());
        }
    }

    /**
     * Retrieves all cards stored in the database.
     *
     * @return A {@link List} of {@link Card} objects
     */
    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM Card";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Card card = new Card(
                        rs.getInt("cardId"),
                        rs.getString("name"),
                        rs.getString("manaCost"),
                        rs.getString("type"),
                        rs.getString("rarity"),
                        rs.getString("setName"),
                        rs.getInt("collectorNumber"),
                        rs.getDouble("marketValue"),
                        rs.getBoolean("isFoil")
                );
                cards.add(card);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving cards: " + e.getMessage());
        }
        return cards;
    }

    /**
     * Updates the market value of a card in the database.
     *
     * @param name      The name of the card to update
     * @param newValue  The new market value
     */
    public void updateMarketValue(String name, double newValue) {
        String sql = "UPDATE Card SET marketValue = ? WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, newValue);
            stmt.setString(2, name);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "Card updated successfully!" : "Card not found.");
        } catch (SQLException e) {
            System.out.println("Error updating card: " + e.getMessage());
        }
    }

    /**
     * Deletes a card from the database.
     *
     * @param name The name of the card to delete
     */
    public void deleteCard(String name) {
        String sql = "DELETE FROM Card WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            int rows = stmt.executeUpdate();
            System.out.println(rows > 0 ? "Card deleted successfully!" : "Card not found.");
        } catch (SQLException e) {
            System.out.println("Error deleting card: " + e.getMessage());
        }
    }
}
