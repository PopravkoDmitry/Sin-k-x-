package com.example.sinfx;

import com.example.function_calculators.CFFunctionCalculator;
import com.example.function_calculators.DefaultThreadFunctionCalculator;
import com.example.function_calculators.FunctionCalculationStrategy;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FunctionCalculatorApplication extends Application {

    private DefaultThreadFunctionCalculator defaultFunctionCalculator;
    private CFFunctionCalculator cfFunctionCalculator;
    private FunctionCalculationStrategy activeFunctionCalculationStrategy;

    private final double bottomLineSpacing = 5;
    private TextField aTextField;
    private TextField xTextField;
    private TextField kTextField;

    private Button drawButton;
    private XYChart.Series<Number, Number> dataSeries;

    private final Runnable functionAnimation = new Runnable() {
        @Override
        public void run() {

            Platform.runLater(() -> {
                drawButton.setDisable(true);
            });

            dataSeries.getData().clear();

                System.out.println("Completed");
                Platform.runLater(() -> {
                    drawButton.setDisable(false);
                });
        }
    };

    @Override
    public void start(Stage stage) {

        NumberAxis xNumberAxis = new NumberAxis();
        NumberAxis yNumberAxis = new NumberAxis();
        LineChart<Number, Number> lineChart = new LineChart<>(xNumberAxis, yNumberAxis);
        dataSeries = new XYChart.Series<>();

        defaultFunctionCalculator = new DefaultThreadFunctionCalculator(dataSeries, this::onCalculationEnd);
        cfFunctionCalculator = new CFFunctionCalculator(dataSeries, this::onCalculationEnd);
        activeFunctionCalculationStrategy = defaultFunctionCalculator;

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Active Calculation Type: Default Thread");
        MenuItem menuItem1 = new MenuItem("Default Thread");
        menuItem1.setOnAction(event -> {
            activeFunctionCalculationStrategy = defaultFunctionCalculator;
            menu.setText("Active Calculation Type: Default Thread");
        });
        MenuItem menuItem2 = new MenuItem("CompletableFuture");
        menuItem2.setOnAction(event -> {
            activeFunctionCalculationStrategy = cfFunctionCalculator;
            menu.setText("Active Calculation Type: CompletableFuture");
        });

        menu.getItems().addAll(menuItem1, menuItem2);
        menuBar.getMenus().add(menu);

        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();

        aTextField = new TextField("1");
        xTextField = new TextField("10");
        kTextField = new TextField("1");

        Text aText = new Text("A:");
        Text kText = new Text("k:");
        Text xText = new Text("Max x:");

        drawButton = new Button("Calculate y(x) = A*sin(k*x)");
        drawButton.setOnAction(this::setNewValuesAndRecalculateFunction);
        Button stopButton = new Button("Stop");
        stopButton.setOnAction(event -> {
            activeFunctionCalculationStrategy.stopCalculating();
            drawButton.setDisable(false);
        });

        xNumberAxis.setLabel("x");
        yNumberAxis.setLabel("y");

        lineChart.getData().add(dataSeries);

        borderPane.setTop(menuBar);
        borderPane.setCenter(lineChart);
        borderPane.setBottom(hBox);
        hBox.setSpacing(bottomLineSpacing);
        hBox.getChildren().addAll(aText, aTextField, xText, xTextField, kText, kTextField, drawButton, stopButton);

        Scene scene = new Scene(borderPane, 600, 600);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            activeFunctionCalculationStrategy.stopCalculating();
        });
        stage.show();
    }

    public static void main(String[] args) {

        launch();
    }

    private void setNewValuesAndRecalculateFunction(ActionEvent event) {

        drawButton.setDisable(true);

        activeFunctionCalculationStrategy.setCalculationValues(Double.parseDouble(aTextField.getText()),
                Double.parseDouble(kTextField.getText()), Double.parseDouble(xTextField.getText()));
        dataSeries.getData().clear();

        activeFunctionCalculationStrategy.calculateWithDelay();
    }

    private void onCalculationEnd(Void unused) {

        drawButton.setDisable(false);
    }
}