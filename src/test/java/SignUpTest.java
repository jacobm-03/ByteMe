import com.byteme.bytemeapplication.Models.User;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class SignUpTest {
    private final Set<String> emailExists = new HashSet<>();

    private boolean isValidPassword(String password) {
        return password.length() >= 6 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()].*");
    }

    private User SignupSimulation(String firstName, String lastName, String email, String password, String confirmPassword) {
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            return null;
        }

        if (!password.equals(confirmPassword)) {
            return null; // Password mismatched
        }

        if (!isValidPassword(password)) {
            return null; // Weak password
        }

        if (emailExists.contains(email.toLowerCase())) {
            return null; // Already exists
        }

        emailExists.add(email.toLowerCase());
        return new User(1, firstName, lastName, email); // Simulated user creation
    }

    @Test
    public void testSuccessfulSignup() {
        User user = SignupSimulation("Jim", "Jones", "Jim.Jones@gmail.com", "P@ssW0rd8#", "P@ssW0rd8#");
        assertNotNull(user);
        assertEquals("Jim", user.getFirstName());
    }

    @Test
    public void testSignupMissingFields() {
        User user = SignupSimulation("", "Jones", "Jim.Jones@gmail.com", "P@ssW0rd8#", "P@ssW0rd8#");
        assertNull(user);
    }

    @Test
    public void testSignupWeakPass() {
        User user = SignupSimulation("Jim", "Jones", "Jim.Jones@gmail.com", "pass", "pass");
        assertNull(user);
    }

    @Test
    public void testSignupMismatchedPass() {
        User user = SignupSimulation("Jim", "Jones", "Jim.Jones@gmail.com", "P@ssW0rd8#", "Password8#");
        assertNull(user);
    }

    @Test
    public void testSignupDuplicateEmail() {
        SignupSimulation("Jim", "Jones", "Jim.Jones@gmail.com", "P@ssW0rd8#", "P@ssW0rd8#");
        User secondTry = SignupSimulation("Jimmy", "Jonas", "Jim.Jones@gmail.com", "P@ssW0rd8#", "P@ssW0rd8#");
        assertNull(secondTry, "Email already taken");
    }
}

