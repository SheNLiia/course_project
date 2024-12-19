module com.example.smarthome {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.smarthome to javafx.fxml;
    exports com.example.smarthome;
}
