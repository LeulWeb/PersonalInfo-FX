module com.example.personalinfoapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.personalinfoapp to javafx.fxml;
    exports com.example.personalinfoapp;
}