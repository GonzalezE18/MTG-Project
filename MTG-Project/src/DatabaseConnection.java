import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/mtg_deck_manager";
    private static final String USER = "root"; // Change if your MySQL username is different
    private static final String PASSWORD = "1234"; // Add your MySQL password if you have one

    private static Connection connection;

    // Establish a single connection instance
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println(" Connected to MySQL successfully!");
            } catch (SQLException e) {
                System.out.println(" Database connection failed: " + e.getMessage());
            }
        }
        return connection;
    }

    // Optional: Close connection when exiting the app
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println(" Connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
