package ge.tsu.text_editor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TextEditor extends Application {
    public static String fileTitle;

    @Override
    public void start(Stage stage) throws IOException {
        System.out.println(Thread.currentThread().getName());


        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent aboutRoot;
        try (InputStream inputStream = TextEditor.class.getResourceAsStream("views/about.fxml")) {
            aboutRoot = fxmlLoader.load(inputStream);
        }

        fxmlLoader = new FXMLLoader();
        Parent mainRoot;
        try (InputStream inputStream = TextEditor.class.getResourceAsStream("views/main.fxml")) {
            mainRoot = fxmlLoader.load(inputStream);
        }

        // Pass stage object to controller object
        MainController mainController = fxmlLoader.getController();
        mainController.setProp();
        mainController.setStage(stage);
        mainController.setAboutRoot(aboutRoot);

        Scene scene = new Scene(mainRoot, 500, 400);
        //TODO თაითლი გასატანია ცალკე ცვლადად და onNew მეთოდის ალერტშიც უნდა გამოვიყენოთ
        stage.setTitle(mainController.getProp().getProperty("default.title") + " - Notepad");
        stage.setScene(scene);
        stage.setHeight(550);
        stage.setWidth(850);
        stage.setX((Screen.getPrimary().getBounds().getWidth()/2)-(stage.getWidth()/2));
        stage.setY((Screen.getPrimary().getBounds().getHeight()/2)-(stage.getHeight()/2));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}