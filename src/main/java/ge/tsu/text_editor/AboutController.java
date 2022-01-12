package ge.tsu.text_editor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

import static java.util.Objects.requireNonNull;

public class AboutController implements Initializable {

    @FXML
    private ImageView imgView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image image = new Image(requireNonNull(AboutController.class.getResourceAsStream("/ge/tsu/text_editor/windows10.jpg")));
        imgView.setImage(image);
    }
}
