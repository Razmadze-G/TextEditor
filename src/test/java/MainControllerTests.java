import static org.junit.jupiter.api.Assertions.assertEquals;

import ge.tsu.text_editor.MainController;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

public class MainControllerTests {
    private final MainController mainController = new MainController();

    @Test
    public void testClearTitle() throws IOException {
        mainController.setProp();
        assertEquals(mainController.getProp().getProperty("default.title"), mainController.getStage().getTitle());
    }

    @Test
    public void testGetProp() {
        assertEquals(mainController.getProp().getClass().getName(), Properties.class.getName());
    }

    @Test
    public void testGetStage() throws IOException {
        assertEquals(mainController.getStage().getClass().getName(), Stage.class.getName());
    }
}