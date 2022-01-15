package ge.tsu.text_editor;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import utils.DBConnection;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Properties;

import static ge.tsu.text_editor.TextEditor.dbConnection;
import static ge.tsu.text_editor.TextEditor.fileTitle;
import static java.lang.String.valueOf;

@Slf4j
public class MainController {

    private Stage stage;
    private int fontSize = 18;
    private String textColor = "black";
    private boolean isDark = false;
    private final FileChooser fileOpenChooser;
    private final FileChooser fileSaveChooser;
    private final Properties prop = new Properties();

    private Path currentFile = null;
    private final Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
    int caretPosition = 0;
    private DateTimeFormatter dtf;

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
    private void onNew() {
        if (textArea.getText().length() > 0) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Your text will be lost!",
                    ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setTitle("Confirmation");
            alert.setResizable(false);
            alert.setContentText("Do you want to Save Changes to " + fileTitle + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.YES) {
                    onSaveAs();
                    currentFile = null;
                    clearTitle();
                    textArea.clear();
                } else if (result.get() == ButtonType.NO) {
                    currentFile = null;
                    clearTitle();
                    textArea.clear();
                }
            }
            log.info("user created new file");
        }
    }

    @FXML
    private void onNewWindow() {
        new TextEditor().start(new Stage());
        log.info("user opened new window");
    }

    @FXML
    private void onOpen() throws IOException {
        File selectedFile = fileOpenChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            currentFile = selectedFile.toPath();
            setTitleToCurrentFile();
            textArea.setText(Files.readString(currentFile));
        }
    }

    @FXML
    private void onSave() {
        if (!fileTitle.equals("Untitled")) {
            try {
                Files.writeString(currentFile, textArea.getText());
                log.info("user has saved text to " + fileTitle);
            } catch (IOException exception) {
                log.error("user was not able to save file because IOException");
            }
            dbConnection.updateSavedFileContentAndType(fileTitle, textArea.getText().substring(0, Math.min(254, textArea.getText().length())));
        } else onSaveAs();
    }

    @FXML
    private void onSaveAs() {
        File selectedFile = fileSaveChooser.showSaveDialog(stage);
        if (selectedFile != null) {
            currentFile = selectedFile.toPath();
            try {
                Files.writeString(currentFile, textArea.getText());
                log.info("user has saved text to {}", fileTitle);
            } catch (IOException exception) {
                log.error("user was not able to save file because IOException", exception);
            }
            setTitleToCurrentFile();
            Alert alertSaved = new Alert(Alert.AlertType.INFORMATION, "File was saved!",
                    ButtonType.OK);
            dbConnection.insertNewFileInfo(fileTitle, textArea.getText());
            alertSaved.setTitle("Information");
            alertSaved.setResizable(false);
            alertSaved.setHeaderText("File \"" + fileTitle + "\" was saved.");
            alertSaved.setContentText("File saved to " + currentFile + ".");
            alertSaved.show();
        }
    }

    @FXML
    private void OnZoomIn() {
        fontSize += 2;
        textArea.setStyle("-fx-text-fill:" + textColor + "; -fx-font-size: " + fontSize);
    }

    @FXML
    private void OnZoomOut() {
        fontSize -= 2;
        textArea.setStyle("-fx-text-fill:" + textColor + "; -fx-font-size: " + fontSize);
    }

    @FXML
    private void onZoomReset() {
        fontSize = Integer.parseInt(prop.getProperty("font.size"));
        textArea.setStyle("-fx-text-fill:" + textColor + "; -fx-font-size: " + fontSize);
    }

    @FXML
    private void onExit() {
        Platform.exit();
        log.info("user has ended working session");
    }

    @FXML
    private void onCopy() {
        cb.setContents(new StringSelection(textArea.getSelectedText()), null);
    }

    @FXML
    private void onCut() throws IndexOutOfBoundsException {
        onCopy();
        textArea.replaceSelection("");
    }

    @FXML
    private void onPaste() throws IOException, UnsupportedFlavorException {
//        updateCaretPosition();
        textArea.insertText(caretPosition, (String) cb.getData(DataFlavor.stringFlavor));
    }

    @FXML
    private void onDelete() {
        textArea.replaceSelection("");
    }

    @FXML
    private void getTimeDate() {
        LocalDateTime now = LocalDateTime.now();
        updateCaretPosition();
        textArea.insertText(caretPosition, dtf.format(now));
    }

    @FXML
    private void onAbout() {
        Alert alert = new Alert(Alert.AlertType.NONE, "This is my epic Notepad--",
                ButtonType.OK);
        alert.setTitle("About Notepad");
        alert.showAndWait();
    }

    @FXML
    private void setDarkMode() {
        if (!isDark) {
            stage.getScene().getRoot().setStyle("-fx-base:black");
            textColor = prop.getProperty("secondary.textColor");
            textArea.setStyle("-fx-text-fill:" + textColor + "; -fx-font-size: " + fontSize);
            isDark = true;
        } else {
            stage.getScene().getRoot().setStyle("-fx-base:white");
            textColor = prop.getProperty("default.textColor");
            textArea.setStyle("-fx-text-fill:" + textColor + "; -fx-font-size: " + fontSize);
            isDark = false;
        }
    }

    private void updateCaretPosition() {
        caretPosition = textArea.getCaretPosition();
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
        textArea.setStyle("-fx-font-size: " + fontSize);
    }

    public void setProp() {
        try(InputStream inputStream = MainController.class.getResourceAsStream("/properties/props.properties")){
            prop.load(inputStream);
            dtf = DateTimeFormatter.ofPattern(prop.getProperty("dateTime.format"));
            fontSize = Integer.parseInt(prop.getProperty("font.size"));
        }catch (IOException ex){
            log.error("Resource path is NOT correct. " + ex);
        }
    }

    public Properties getProp(){
        return prop;
    }
}