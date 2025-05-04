import com.byteme.bytemeapplication.Controllers.HomeController;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HomeTest {

    @Test
    public void testSingletonInstance() {
        HomeController controller = new HomeController();
        assertEquals(controller, HomeController.getInstance());
    }

    @Test
    public void testLoadContent_invalidPath_throwsIOException() {
        HomeController controller = new HomeController();

        Exception exception = assertThrows(Exception.class, () -> {
            controller.loadContent("/invalid/path.fxml");
        });

        String expectedMessage = "FXML file not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
