module com.example.comwanyoikesufeeds {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.desktop;
    requires java.sql;

    opens com.example.comwanyoikesufeeds to javafx.fxml;
    exports com.example.comwanyoikesufeeds;
}