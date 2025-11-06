import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardDAO {

    // CREATE: Add new card to the database
    public void addCard(Card card) {
        String sql = """
            INSERT INTO Card (name, manaCost, type, rarity, setName, collectorNumber, marketValue, isFoil)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, card.getName());
            pstmt.setString(2, card.getManaCost());
            pstmt.setString(3, card.getType());
            pstmt.setString(4, card.getRarity());
            pstmt.setString(5, card.getSetName());
            pstmt.setInt(6, card.getCollectorNumber());
            pstmt.setDouble(7, card.getMarketValue());
            pstmt.setBoolean(8, card.isFoil());

            pstmt.executeUpdate();
            System.out.println(" Card added successfully to database!");

        } catch (SQLException e) {
            System.out.println(" Error adding card: " + e.getMessage());
        }
    }

    //READ: Load all cards from the database
    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM Card ORDER BY name ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Card card = new Card(
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
            System.out.println(" Error loading cards: " + e.getMessage());
        }

        return cards;
    }

    // UPDATE: Change a card's market value
    public void updateMarketValue(String name, double newValue) {
        String sql = "UPDATE Card SET marketValue = ? WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newValue);
            pstmt.setString(2, name);

            int rows = pstmt.executeUpdate();
            if (rows > 0)
                System.out.println("Market value updated successfully!");
            else
                System.out.println("No card found with that name.");

        } catch (SQLException e) {
            System.out.println("Error updating card: " + e.getMessage());
        }
    }

    // DELETE: Remove card from database
    public void deleteCard(String name) {
        String sql = "DELETE FROM Card WHERE name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            int rows = pstmt.executeUpdate();

            if (rows > 0)
                System.out.println("Card removed successfully!");
            else
                System.out.println("No card found with that name.");

        } catch (SQLException e) {
            System.out.println("Error deleting card: " + e.getMessage());
        }
    }

    //SEARCH: Find a card by name (optional helper)
    public Card findCardByName(String name) {
        String sql = "SELECT * FROM Card WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Card(
                    rs.getString("name"),
                    rs.getString("manaCost"),
                    rs.getString("type"),
                    rs.getString("rarity"),
                    rs.getString("setName"),
                    rs.getInt("collectorNumber"),
                    rs.getDouble("marketValue"),
                    rs.getBoolean("isFoil")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error finding card: " + e.getMessage());
        }
        return null;
    }
}
