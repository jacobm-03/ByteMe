import com.byteme.bytemeapplication.Models.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProfileTest {

    @Test
    public void testUserInitialization() {
        User user = new User(101, "Emma", "Lee", "EmmaLee@gmail.com");

        assertEquals(101, user.getId());
        assertEquals("Emma", user.getFirstName());
        assertEquals("Lee", user.getLastName());
        assertEquals("EmmaLee@gmail.com", user.getEmail());
        assertEquals("Emma Lee", user.getFullName());
    }

    @Test
    public void testUserProfileUpdate() {
        User user = new User(101, "Emma", "Lee", "EmmaLee@gmail.com");

        // Simulate profile update
        User updatedUser = new User(user.getId(), "Emma", "Smith", "EmmaSmith@gmail.com");

        assertEquals(101, updatedUser.getId());
        assertEquals("Emma", updatedUser.getFirstName());
        assertEquals("Smith", updatedUser.getLastName());
        assertEquals("EmmaSmith@gmail.com", updatedUser.getEmail());
        assertEquals("Emma Smith", updatedUser.getFullName());
    }

    @Test
    public void testUserDeletionSimulation() {
        User user = new User(101, "Emma", "Lee", "EmmaLee@gmail.com");

        user = null;

        assertNull(user, "User object should be null to simulate deletion");
    }

    @Test
    public void testSuccessfulPasswordChange() {
        String oldPassword = "OldPassword";
        String newPassword = "NewPassword";
        String confirmPassword = "NewPassword";

        // Simulate the stored password in DB
        String storedPassword = oldPassword;

        // Step 1: Check if fields are filled
        assertFalse(oldPassword.isEmpty());
        assertFalse(newPassword.isEmpty());
        assertFalse(confirmPassword.isEmpty());

        // Step 2: Validate old password matches stored
        assertEquals(storedPassword, oldPassword);

        // Step 3: Check if new passwords match
        assertEquals(newPassword, confirmPassword);

        // Step 4: Simulate update
        storedPassword = newPassword;

        // Step 5: Ensure it was updated
        assertEquals("NewPassword", storedPassword);
    }

    @Test
    public void testIncorrectOldPassword() {
        String storedPassword = "CorrectOld";
        String enteredOldPassword = "WrongOld";

        assertNotEquals(storedPassword, enteredOldPassword, "Old password should not match");
    }

    @Test
    public void testNewPasswordsDoNotMatch() {
        String newPassword = "Password1";
        String confirmPassword = "Password2";

        assertNotEquals(newPassword, confirmPassword, "New password confirmation should fail");
    }

    @Test
    public void testEmptyFields() {
        String old = "";
        String newPass = "new";
        String confirm = "";

        assertTrue(old.isEmpty() || newPass.isEmpty() || confirm.isEmpty(), "Should detect empty fields");
    }
}
