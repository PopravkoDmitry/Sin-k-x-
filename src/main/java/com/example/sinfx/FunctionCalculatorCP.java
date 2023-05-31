package com.example.sinfx;

import javafx.application.Application;
import javafx.application.Platform;
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

import java.util.concurrent.CompletableFuture;

public class FunctionCalculatorCP extends Application{
    private Thread thread;
    private double a = 1;
    private double k = 1;
    private double maxX = 10;
    private final double stepX = 0.1;

    private final int sleepTime = 70;
    private final double bottomLineSpacing = 5;
    private TextField aTextField;
    private TextField xTextField;
    private TextField kTextField;

    private Button drawButton;
    private XYChart.Series<Number, Number> dataSeries;
    private CompletableFuture functionAnimation;

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

        drawButton = new Button("Calculate y(x) = A*sin(k*x)");
        drawButton.setOnAction(this::setNewValuesAndRecalculateFunction);
        Button stopButton = new Button("Stop");
        stopButton.setOnAction(event -> {
            if(thread != null) {

                thread.interrupt();
                drawButton.setDisable(false);
            }
        });

        xNumberAxis.setLabel("x");
        yNumberAxis.setLabel("y");

        LineChart<Number, Number> lineChart = new LineChart<>(xNumberAxis, yNumberAxis);
        dataSeries = new XYChart.Series<>();

        lineChart.getData().add(dataSeries);

        borderPane.setCenter(lineChart);
        borderPane.setBottom(hBox);
        hBox.setSpacing(bottomLineSpacing);
        hBox.getChildren().addAll(aText, aTextField, xText, xTextField, kText, kTextField, drawButton, stopButton);

        Scene scene = new Scene(borderPane, 600, 600);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {

            if(thread != null) {

                thread.interrupt();
            }
        });
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }

    private void setNewValuesAndRecalculateFunction(ActionEvent event) {

        a = Double.parseDouble(aTextField.getText());
        k = Double.parseDouble(kTextField.getText());
        maxX = Double.parseDouble(xTextField.getText());

        functionAnimation = CompletableFuture.runAsync(() -> {

            thread = Thread.currentThread();

            drawButton.setDisable(true);
            dataSeries.getData().clear();

            for (double x = 0; x <= maxX; x += stepX) {

                var y = a * Math.sin(k * x);
                var finalX = x;

                CompletableFuture.runAsync(() -> {

                    dataSeries.getData().add(new XYChart.Data<>(finalX, y));
                }, Platform::runLater);



                System.out.println(x);

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                    return;
                }
            }

            functionAnimation.thenRun(() -> {

                System.out.println("Completed");
                drawButton.setDisable(false);
            });
        });
    }
}