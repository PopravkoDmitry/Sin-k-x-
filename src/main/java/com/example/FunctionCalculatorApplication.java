package com.example;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FunctionCalculatorApplication extends Application {

    private double a = 1;
    private double k = 1;
    private double maxX = 10;
    private final double stepX = 0.1;
    private final double bottomLineSpacing = 5;
    private TextField aTextField;
    private TextField xTextField;
    private TextField kTextField;
    private XYChart.Series<Number, Number> dataSeries;

    @Override
    public void start(Stage stage) {
        NumberAxis xNumberAxis = new NumberAxis();
        NumberAxis yNumberAxis = new NumberAxis();
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();

        aTextField = new TextField(String.valueOf(a));
        xTextField = new TextField(String.valueOf(maxX));
        kTextField = new TextField(String.valueOf(k));

        Text aText = new Text("A:");
        Text kText = new Text("k:");
        Text xText = new Text("Max x:");

        Button drawButton = new Button("Calculate y(x) = A*sin(k*x)");
        drawButton.setOnAction(this::SetNewValuesAndRecalculateFunction);

        xNumberAxis.setLabel("x");
        yNumberAxis.setLabel("y");

        LineChart<Number, Number> _lineChart = new LineChart<>(xNumberAxis, yNumberAxis);
        dataSeries = new XYChart.Series<>();

        _lineChart.getData().add(dataSeries);

        borderPane.setCenter(_lineChart);
        borderPane.setBottom(hBox);
        hBox.setSpacing(bottomLineSpacing);
        hBox.getChildren().addAll(aText, aTextField, xText, xTextField, kText, kTextField, drawButton);

        Scene scene = new Scene(borderPane, 800, 800);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }

    private void calculateFunction() {

        dataSeries.getData().clear();
        for (double x = 0; x <= maxX; x += stepX) {
            var y = a * Math.sin(k * x);
            dataSeries.getData().add(new XYChart.Data<>(x, y));
        }
    }

    private void SetNewValuesAndRecalculateFunction(ActionEvent event) {

        a = Double.parseDouble(aTextField.getText());
        k = Double.parseDouble(kTextField.getText());
        maxX = Double.parseDouble(xTextField.getText());
        calculateFunction();
    }
}