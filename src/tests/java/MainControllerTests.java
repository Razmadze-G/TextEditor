import ge.tsu.text_editor.MainController;
import javafx.stage.Stage;
import org.junit.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainControllerTests {
    private final MainController mainController = new MainController();

    @Test
    private void testClearTitle() throws IOException {
        mainController.setProp();
        assertEquals(mainController.getProp().getProperty("default.title"), mainController.getStage().getTitle());
    }

    @Test
    private void testGetProp() {
        assertEquals(mainController.getProp().getClass().getName(), Properties.class.getName());
    }

    @Test
    private void testGetStage() throws IOException {
        assertEquals(mainController.getStage().getClass().getName(), Stage.class.getName());
    }
}