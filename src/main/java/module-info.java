module ge.tsu.text_editor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires com.h2database;
    requires lombok;
    requires org.apache.logging.log4j.slf4j;
    requires slf4j.api;

    opens ge.tsu.text_editor to javafx.fxml;
    exports ge.tsu.text_editor;
}