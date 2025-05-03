import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AddSubjectTest {
    // Inner class to simulate Subject creation logic
    static class SubLogic {
        private final int userId;
        private final String name;
        private final String color;

        public SubLogic(int userId, String name, String color) {
            this.userId = userId;
            this.name = name;
            this.color = color;
        }

        public int getUserId() { return userId; }
        public String getName() { return name; }
        public String getColor() { return color; }
    }

    // Simulated subject creation method
    public static SubLogic createSub(int userId, String name, String color) {
        if (name == null || name.trim().isEmpty()) return null;
        if (color == null || color.trim().isEmpty()) return null;
        return new SubLogic(userId, name.trim(), color);
    }

    @Test
    public void testSuccessfulSubjectCreation() {
        SubLogic subject = createSub(1, "Math", "#bca8ff");
        assertNotNull(subject);
        assertEquals("Math", subject.getName());
        assertEquals("#bca8ff", subject.getColor());
        assertEquals(1, subject.getUserId());
    }

    @Test
    public void testSubjectCreationEmptyName() {
        assertNull(createSub(1, "", "#bca8ff"));
    }

    @Test
    public void testSubjectCreationWithNullColor() {
        assertNull(createSub(1, "Science", null));
    }

    @Test
    public void testSubjectCreationWhitespaceName() {
        assertNull(createSub(1, "   ", "#bca8ff"));
    }

    @Test
    public void testSubjectCreationNullName() {
        assertNull(createSub(1, null, "#bca8ff"));
    }
}
