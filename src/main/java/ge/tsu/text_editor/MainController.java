package ge.tsu.text_editor;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static ge.tsu.text_editor.TextEditor.fileTitle;

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
        //TODO ტექსტია შესაცვლელი. გვჭირდება, რომ ტექსტში ფაილის სახელი იყოს ნახსენები.
        if(textArea.getText().length() > 0) {
            DialogPane dp = new DialogPane();
            Alert alert = new Alert(Alert.AlertType.NONE, "Text of will be lost!",
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
    private void onNewWindow(ActionEvent e) throws IOException {
        new TextEditor().start(new Stage());
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
        if(!fileTitle.equals("Untitled"))
            Files.writeString(currentFile, textArea.getText());
        else onSaveAs(e);
    }

    @FXML
    private void onSaveAs(ActionEvent e) throws IOException {
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
        fileTitle = currentFile.getFileName().toString();
        stage.setTitle(fileTitle + " - Notepad");
    }

    private void clearTitle() {
        fileTitle = "Untitled";
        stage.setTitle(fileTitle + " - Notepad");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setAboutRoot(Parent aboutRoot) {
        this.aboutRoot = aboutRoot;
    }
}
