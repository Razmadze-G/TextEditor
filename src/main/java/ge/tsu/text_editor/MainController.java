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
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static ge.tsu.text_editor.TextEditor.fileTitle;

public class MainController {

    private Stage stage;
    private int fontSize = 18;
    private boolean isDark = false;
    private final FileChooser fileOpenChooser;
    private final FileChooser fileSaveChooser;

    private Path currentFile = null;
    private Parent aboutRoot;
    private Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
    int caretPosition = 0;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");

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
    private void onNew(ActionEvent e) throws IOException{
        //TODO ტექსტია შესაცვლელი. გვჭირდება, რომ ტექსტში ფაილის სახელი იყოს ნახსენები.
        if(textArea.getText().length() > 0) {
            DialogPane dp = new DialogPane();
            Alert alert = new Alert(Alert.AlertType.NONE, "Text of will be lost!",
                    ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setTitle("Confirmation");
            alert.setResizable(false);
            alert.setContentText("Do you want to Save Changes to " + fileTitle + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent()) {
                if (result.get() == ButtonType.YES) {
                    onSaveAs(e);
                    currentFile = null;
                    clearTitle();
                    textArea.clear();
                }
                else if(result.get() == ButtonType.NO){
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
            Alert alertSaved = new Alert(Alert.AlertType.INFORMATION, "File was saved!",
                    ButtonType.OK);
            alertSaved.setTitle("Information");
            alertSaved.setResizable(false);
            alertSaved.setHeaderText("File \"" + fileTitle + "\" was saved.");
            alertSaved.setContentText("File saved to " + currentFile + ".");
            alertSaved.show();
        }
    }

    @FXML
    private void OnZoomIn(ActionEvent e) {
        fontSize+=2;
        textArea.setStyle("-fx-text-fill:white; -fx-font-size: " + fontSize);
    }

    @FXML
    private void OnZoomOut(ActionEvent e) {
        fontSize-=2;
        textArea.setStyle("-fx-text-fill:white; -fx-font-size: " + fontSize);

    }

    @FXML
    private void onExit(ActionEvent e) {
        Platform.exit();
    }

    @FXML
    private void onCopy(ActionEvent e) {
        cb.setContents(new StringSelection(textArea.getSelectedText()),null);
    }

    @FXML
    private void onCut(ActionEvent e) throws IndexOutOfBoundsException {
        onCopy(e);
        textArea.replaceSelection("");
    }

    @FXML
    private void onPaste(ActionEvent e) throws IOException, UnsupportedFlavorException {
        updateCaretPosition();
        textArea.insertText(caretPosition, (String) cb.getData(DataFlavor.stringFlavor));
    }

    @FXML
    private void onDelete(ActionEvent e){
        textArea.replaceSelection("");
    }

    @FXML
    private void getTimeDate(ActionEvent e){
        LocalDateTime now = LocalDateTime.now();
        updateCaretPosition();
        textArea.insertText(caretPosition, dtf.format(now));
    }

    @FXML
    private void onAbout(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.NONE, "This is a dummy Notepad application",
                ButtonType.OK);
        alert.setTitle("About Notepad");
        alert.getDialogPane().setContent(aboutRoot);
        alert.showAndWait();
    }

    @FXML
    private void setDarkMode(ActionEvent e){
        if(!isDark){
            stage.getScene().getRoot().setStyle("-fx-base:black");
            textArea.setStyle("-fx-text-fill:white; -fx-font-size: " + fontSize);
            isDark = true;
        }else
        {
            stage.getScene().getRoot().setStyle("-fx-base:white");
            textArea.setStyle("-fx-text-fill:black; -fx-font-size: " + fontSize);
            isDark = false;
        }
    }


    private void updateCaretPosition(){
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

    public void setAboutRoot(Parent aboutRoot) {
        this.aboutRoot = aboutRoot;
    }
}
