package ge.tsu.text_editor;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class MainController {

    private Stage stage;
    private final FileChooser fileOpenChooser;
    private final FileChooser fileSaveChooser;

    private Path currentFile = null;
    private Parent aboutRoot;

    public MainController() {
        // File open chooser
        fileOpenChooser = new FileChooser();
        fileOpenChooser.setTitle("Open File");
        fileOpenChooser.getExtensionFilters().addAll(
                new ExtensionFilter("All Files", "*.*"));

        // File save chooser
        fileSaveChooser = new FileChooser();
        fileSaveChooser.setTitle("Save As");
        fileSaveChooser.getExtensionFilters().addAll(
                new ExtensionFilter("All Files", "*.*"));
    }

    @FXML
    private TextArea textArea;

    @FXML
    private void onNew(ActionEvent e) {
        // TODO if currentFile or textArea is not empty, show the question prompt
        if(textArea.getText().length() > 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "text in this file will be lost!",
                    ButtonType.YES, ButtonType.CANCEL);
            alert.setTitle("Confirmation");
            alert.setResizable(false);
            alert.setContentText("Do you want to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.YES) {
                    currentFile = null;
                    clearTitle();
                    textArea.clear();
                }
            }
        }
    }

    @FXML
    private void onOpen(ActionEvent e) throws IOException {
        File selectedFile = fileOpenChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            currentFile = selectedFile.toPath();
            setTitleToCurrentFile();
            textArea.setText(Files.readString(currentFile));
        }
    }

    @FXML
    private void onSave(ActionEvent e) throws IOException {
        File selectedFile = fileSaveChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            currentFile = selectedFile.toPath();
            Files.writeString(currentFile, textArea.getText());
            setTitleToCurrentFile();
        }
    }

    @FXML
    private void onExit(ActionEvent e) {
        Platform.exit();
    }

    @FXML
    private void onAbout(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.NONE, "This is a dummy Notepad application",
                ButtonType.OK);
        alert.setTitle("About Notepad");
        alert.getDialogPane().setContent(aboutRoot);
        alert.showAndWait();
    }

    public TextArea getTextArea() {
        return textArea;
    }

    private void setTitleToCurrentFile() {
        setTitle(currentFile.getFileName().toString());
    }

    private void clearTitle() {
        setTitle(null);
    }

    private void setTitle(String title) {
        if (title != null) {
            stage.setTitle(title + " - Notepad");
        } else {
            stage.setTitle("Untitled - Notepad");
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAboutRoot(Parent aboutRoot) {
        this.aboutRoot = aboutRoot;
    }
}
