import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Handles the connection to the MySQL database for the MTG Deck Management System.
 * <p>
 * This class uses the Singleton pattern to ensure that only one database
 * connection is created and shared throughout the application. All DAO classes
 * will use this connection when executing SQL queries.
 * </p>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *     <li>Establishes a connection to the MySQL server</li>
 *     <li>Provides a single shared connection instance</li>
 *     <li>Allows clean shutdown by closing the connection</li>
 *     <li>Error messages printed to help with debugging</li>
 * </ul>
 *
 * @author Emily
 * @version 1.0
 */
public class DatabaseConnection {

    /** JDBC URL used to connect to the MySQL database */
    private static final String URL = "jdbc:mysql://localhost:3306/mtg_deck_manager";

    /** Username for MySQL authentication */
    private static final String USER = "root";

    /** Password for the MySQL account */
    private static final String PASSWORD = "1234";

    /** Shared database connection instance */
    private static Connection connection;

    /**
     * Returns a shared database connection instance.
     * <p>
     * If a connection has not yet been established, this method attempts to open
     * a new connection using the provided URL, username, and password.
     * </p>
     *
     * @return a {@link Connection} object used to communicate with MySQL,
     *         or {@code null} if the connection attempt fails
     */
    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✔ Connected to MySQL successfully!");
            } catch (SQLException e) {
                System.out.println("Database connection failed: " + e.getMessage());
            }
        }
        return connection;
    }

    /**
     * Closes the active database connection, if one exists.
     * <p>
     * This should be called when the application shuts down to free system
     * resources and prevent potential memory leaks.
     * </p>
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
