import com.byteme.bytemeapplication.Models.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    private final String testEmail = "Jim.Jones@gmail.com";
    private final String testPass = "P@ssW0rd8#";

    private User TestAuth(String email, String password) {
        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            return null;
        }
        if (email.equalsIgnoreCase(testEmail) && password.equals(testPass)) {
            return new User(102, "Jim", "Jones", testEmail);
        }
        return null;
    }

    @Test
    public void testSuccessfulLogin() {
        User user = TestAuth("Jim.Jones@gmail.com", "P@ssW0rd8#");
        assertNotNull(user, "User successfully authenticated");
        assertEquals("Jim Jones", user.getFullName());
    }

    @Test
    public void testLoginWrongPassword() {
        User user = TestAuth("Jim.Jones@gmail.com", "WrongPassword");
        assertNull(user, "Login failed with incorrect password");
    }

    @Test
    public void testLoginNoEmail() {
        User user = TestAuth("notJimmy@smail.com", "P@ssW0rd8#");
        assertNull(user, "Login failed for unknown email");
    }

    @Test
    public void testLoginEmpty() {
        User user = TestAuth("", "");
        assertNull(user, "Login failed with empty fields");
    }

    @Test
    public void testLoginNull() {
        User user = TestAuth(null, null);
        assertNull(user, "Login failed with null inputs");
    }
}

