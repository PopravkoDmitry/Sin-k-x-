package com.example.test;

import javafx.application.Application;
import javafx.beans.Observable;
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


public class HelloApplication extends Application {
    private double _a = 1;
    private double _k = 1;
    private double _minX = 0;
    private double _maxX = 10;
    private double _stepX = 0.1;
    private NumberAxis _xNumberAxis;
    private NumberAxis _yNumberAxis;
    private Button _drawButton;
    private TextField _aTextField;
    private Text _aText;
    private Text _kText;
    private Text _xText;
    private TextField _xTextField;
    private TextField _kTextField;
    private LineChart<Number, Number> _lineChart;
    private XYChart.Series<Number, Number> dataSeries;
    private BorderPane _borderPane;
    private HBox _hBox;
    private Scene scene;

    @Override
    public void start(Stage stage){

        _xNumberAxis = new NumberAxis();
        _yNumberAxis = new NumberAxis();
        _borderPane = new BorderPane();
        _hBox = new HBox();

        _aTextField = new TextField(String.valueOf(_a));
        _aTextField.textProperty().addListener(this::CheckTextFieldContext);
        _xTextField = new TextField(String.valueOf(_maxX));
        _kTextField = new TextField(String.valueOf(_k));

        _aText = new Text("A:");
        _kText = new Text("k:");
        _xText = new Text("Max x:");

        _drawButton = new Button("Calculate y(x) = A*sin(k*x)");
        _drawButton.setOnAction(this::SetNewValuesAndRecalculateFunction);

        _xNumberAxis.setLabel("x");
        _yNumberAxis.setLabel("y");

        _lineChart = new LineChart<>(_xNumberAxis, _yNumberAxis);
        dataSeries = new XYChart.Series<>();

        _lineChart.getData().add(dataSeries);

        _borderPane.setCenter(_lineChart);
        _borderPane.setBottom(_hBox);
        _hBox.setSpacing(5);
        _hBox.getChildren().add(_aText);
        _hBox.getChildren().add(_aTextField);
        _hBox.getChildren().add(_xText);
        _hBox.getChildren().add(_xTextField);
        _hBox.getChildren().add(_kText);
        _hBox.getChildren().add(_kTextField);
        _hBox.getChildren().add(_drawButton);

        scene = new Scene(_borderPane, 800, 800);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    private void CalculateFunction(){
        for (double x = _minX; x <= _maxX; x += _stepX){
            var y = _a * Math.sin(_k * x);
            dataSeries.getData().add(new XYChart.Data<>(x, y));
        }
    }

    private void SetNewValuesAndRecalculateFunction(ActionEvent event){
        _a = Double.parseDouble(_aTextField.getText());
        _k = Double.parseDouble(_kTextField.getText());
        _maxX = Double.parseDouble(_xTextField.getText());

        dataSeries.getData().clear();
        CalculateFunction();
    }

    private void CheckTextFieldContext(Observable observable)
    {
    }

}