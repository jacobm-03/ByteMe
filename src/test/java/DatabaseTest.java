

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseTest {

    @Test
    public void testConnection() throws SQLException {
        String url = "jdbc:sqlite:byteme.db"; // Replace with your actual DB path if needed
        Connection conn = DriverManager.getConnection(url);
        assertNotNull(conn, "Connection to SQLite DB should not be null");
        conn.close();
    }
}
