module com.example.sinfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.painter to javafx.fxml;
    exports com.example.painter;
    exports com.example.functions;
    opens com.example.functions to javafx.fxml;
    exports com.example.runners;
    opens com.example.runners to javafx.fxml;
    exports com.example;
    opens com.example to javafx.fxml;
    exports com.example.views;
    opens com.example.views to javafx.fxml;
}