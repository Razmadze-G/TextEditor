module com.example.text_editor {
    requires javafx.controls;
    requires javafx.fxml;


    opens ge.tsu.text_editor to javafx.fxml;
    exports ge.tsu.text_editor;
}