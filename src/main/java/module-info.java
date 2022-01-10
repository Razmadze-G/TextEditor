module com.example.text_editor {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.text_editor to javafx.fxml;
    exports com.example.text_editor;
}