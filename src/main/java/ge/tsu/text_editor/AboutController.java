package ge.tsu.text_editor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    @FXML
    private ImageView imgView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(Thread.currentThread().getName());
        try (InputStream inputStream = AboutController.class.getResourceAsStream("/ge/tsu/text_editor/windows10.jpg")) {
            Image image = new Image(inputStream);
            imgView.setImage(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
