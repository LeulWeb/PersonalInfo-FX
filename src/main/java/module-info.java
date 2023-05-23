module com.example.personalinfoapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.personalinfoapp to javafx.fxml;
    exports com.example.personalinfoapp;
}