module com.example.final_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires javafx.media;
    requires java.desktop;
    requires org.json;


    opens com.example.lethal_corporation to javafx.fxml;
    exports com.example.lethal_corporation;
}