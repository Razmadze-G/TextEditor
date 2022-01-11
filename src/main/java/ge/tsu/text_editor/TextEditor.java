package ge.tsu.text_editor;

import ge.tsu.text_editor.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class TextEditor extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        System.out.println(Thread.currentThread().getName());

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent aboutRoot;
        try (InputStream inputStream = TextEditor.class.getResourceAsStream("about.fxml")) {
            aboutRoot = fxmlLoader.load(inputStream);
        }

        fxmlLoader = new FXMLLoader();
        Parent mainRoot;
        try (InputStream inputStream = TextEditor.class.getResourceAsStream("main.fxml")) {
            mainRoot = fxmlLoader.load(inputStream);
        }

        // Pass stage object to controller object
        MainController controller = fxmlLoader.getController();
        controller.setStage(stage);
        controller.setAboutRoot(aboutRoot);

        Scene scene = new Scene(mainRoot, 500, 400);
        stage.setTitle("Untitled - Notepad");
        stage.setScene(scene);
        stage.setMinWidth(400);
        stage.setMinHeight(300);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}