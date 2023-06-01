module com.example.sinfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sinfx to javafx.fxml;
    exports com.example.sinfx;
    exports com.example.function_calculators;
    opens com.example.function_calculators to javafx.fxml;
}