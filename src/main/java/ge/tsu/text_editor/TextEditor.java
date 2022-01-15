package ge.tsu.text_editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import utils.DBConnection;

import java.io.IOException;
import java.io.InputStream;

import static java.util.Objects.requireNonNull;

@Slf4j
public class TextEditor extends Application {
    static DBConnection dbConnection = new DBConnection();
    public static String fileTitle = "Untitled";

    @Override
    public void start(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent mainRoot = null;
        try (InputStream inputStream = TextEditor.class.getResourceAsStream("main.fxml")) {
            mainRoot = fxmlLoader.load(inputStream);
        } catch (IOException exception) {
            log.error("can't load view");
        }

        // Pass stage object to controller object
        MainController mainController = fxmlLoader.getController();
        mainController.setStage(stage);
        mainController.setProp();

        try {
            Scene scene = new Scene(requireNonNull(mainRoot), 500, 400);
            stage.setTitle(mainController.getProp().getProperty("default.title") + " - Notepad--");
            stage.setScene(scene);
            stage.setHeight(550);
            stage.setWidth(850);
            stage.setX((Screen.getPrimary().getBounds().getWidth()/2)-(stage.getWidth()/2));
            stage.setY((Screen.getPrimary().getBounds().getHeight()/2)-(stage.getHeight()/2));
            stage.show();
        } catch (NullPointerException exception) {
            log.error("mainRoot variable has value \"null\" ");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}