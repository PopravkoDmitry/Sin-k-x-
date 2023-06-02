package com.example.sinfx;

import com.example.function_calculators.CFFunctionCalculator;
import com.example.function_calculators.DefaultThreadFunctionCalculator;
import com.example.function_calculators.FunctionCalculationStrategy;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FunctionCalculatorApplication extends Application {

    private DefaultThreadFunctionCalculator defaultFunctionCalculator;
    private CFFunctionCalculator cfFunctionCalculator;
    private FunctionCalculationStrategy activeFunctionCalculationStrategy;

    private final double bottomLineSpacing = 5;
    private TextField aTextField;
    private TextField stepXTextField;
    private TextField kTextField;
    private Button drawButton;
    private final Point2D applicationScale = new Point2D(600, 600);

    private FunctionDrawer functionDrawer;

    @Override
    public void start(Stage stage) {

        aTextField = new TextField("50"); // 10
        stepXTextField = new TextField("1"); // 1
        kTextField = new TextField("300"); // 0.1

        Text aText = new Text("A:");
        Text kText = new Text("k:");
        Text xText = new Text("Step:");

        Canvas canvas = new Canvas(applicationScale.getX(), applicationScale.getY());
        functionDrawer = new FunctionDrawer(canvas, new Point2D(0, canvas.getHeight() / 2));

        defaultFunctionCalculator = new DefaultThreadFunctionCalculator(functionDrawer,
                this::onCalculationEnd, applicationScale.getX());
        cfFunctionCalculator = new CFFunctionCalculator(functionDrawer,
                this::onCalculationEnd, applicationScale.getX());
        activeFunctionCalculationStrategy = defaultFunctionCalculator;



        drawButton = new Button("Calculate y(x) = A*sin(k*x)");
        Button stopButton = new Button("Stop");

        drawButton.setOnAction(this::setNewValuesAndRecalculateFunction);
        stopButton.setOnAction(event -> {
            activeFunctionCalculationStrategy.stopCalculating();
            drawButton.setDisable(false);
        });

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

        StackPane rootStackPain = new StackPane();
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();

        rootStackPain.getChildren().addAll(canvas, borderPane);
        borderPane.setTop(menuBar);
        borderPane.setBottom(hBox);
        hBox.setSpacing(bottomLineSpacing);
        hBox.getChildren().addAll(aText, aTextField, xText, stepXTextField, kText, kTextField, drawButton, stopButton);

        Scene scene = new Scene(rootStackPain, applicationScale.getX(), applicationScale.getY());
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
                Double.parseDouble(kTextField.getText()), Double.parseDouble(stepXTextField.getText()));

        functionDrawer.Clear();

        activeFunctionCalculationStrategy.calculateWithDelay();
    }

    private void onCalculationEnd(Void unused) {

        drawButton.setDisable(false);
    }
}