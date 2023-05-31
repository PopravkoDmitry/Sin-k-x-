module com.example.sinfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sinfx to javafx.fxml;
    exports com.example.sinfx;
}