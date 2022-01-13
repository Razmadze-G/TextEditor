module ge.tsu.text_editor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens ge.tsu.text_editor to javafx.fxml;
    exports ge.tsu.text_editor;
}