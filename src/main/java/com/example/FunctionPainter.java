package com.example;

import com.example.functions.*;
import com.example.painter.*;
import com.example.runners.CompletableRunner;
import com.example.runners.ThreadRunner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FunctionPainter extends Application {
    private final double BOTTOM_LINE_SPACING = 5;

    private Iterable<Point2D> currentContentSupplier;
    private FunctionCalculator functionCalculator;
    private AsyncDrawer painter;
    private TextField aTextField;
    private TextField stepXTextField;
    private TextField kTextField;
    private Button drawButton;
    private Button stopButton;
    private Button clearButton;
    private final Point2D windowSize = new Point2D(900, 600);

    private IFunction<Coefficients> function = new Sinusoid();
    private IRenderer functionDrawer;
    private final List<Point2D> drawnPoints = new ArrayList<>();

    @Override
    public void start(Stage stage) {

        aTextField = new TextField("50"); // 10
        stepXTextField = new TextField("1"); // 1
        kTextField = new TextField("300"); // 0.1

        Text aText = new Text("A:");
        Text kText = new Text("k:");
        Text xText = new Text("Step:");

        this.functionDrawer = new CanvasRenderer(this.windowSize);
        this.painter = new AsyncDrawer(ThreadRunner.INSTANCE);

        this.drawButton = new Button("Draw y(x) = A*sin(k*x)");
        this.stopButton = new Button("Stop");
        this.stopButton.setDisable(true);
        this.clearButton = new Button("Clear canvas");

        drawButton.setOnAction(this::setNewValuesAndRecalculateFunction);
        stopButton.setOnAction(event -> {
            this.painter.stopCurrentTask();
            drawButton.setDisable(false);
            stopButton.setDisable(true);
        });
        clearButton.setOnAction(event -> {
            this.drawnPoints.clear();
            this.functionDrawer.clear();
            resetCalculator();
        });

        MenuBar menuBar = new MenuBar();
        Menu runnerMenu = new Menu("Active Calculation Type: Default Thread");
        Menu rendererMenu = new Menu("Active Renderer: Canvas");
        Menu functionMenu = new Menu("Active Function: Sinus");



        MenuItem runnerMenuItem1 = new MenuItem("Thread");
        runnerMenuItem1.setOnAction(event -> {
            this.painter.setRunner(ThreadRunner.INSTANCE);
            runnerMenu.setText("Calculation runner: Thread");
        });

        MenuItem runnerMenuItem2 = new MenuItem("CompletableFuture");
        runnerMenuItem2.setOnAction(event -> {
            this.painter.setRunner(new CompletableRunner());
            runnerMenu.setText("Calculation runner: CompletableFuture");
        });

        MenuItem rendererMenuItem1 = new MenuItem("Canvas");
        MenuItem rendererMenuItem2 = new MenuItem("LineChart");
        MenuItem functionMenuItem1 = new MenuItem("Sinus");
        functionMenuItem1.setOnAction(event -> {
            function = new Sinusoid();
            functionMenu.setText("Active Function: Sinus");
        });
        MenuItem functionMenuItem2 = new MenuItem("Tangent");
        functionMenuItem2.setOnAction(event -> {
            function = new Tangential();
            functionMenu.setText("Active Function: Tangent");
        });

        runnerMenu.getItems().addAll(runnerMenuItem1, runnerMenuItem2);
        rendererMenu.getItems().addAll(rendererMenuItem1, rendererMenuItem2);
        functionMenu.getItems().addAll(functionMenuItem1, functionMenuItem2);
        menuBar.getMenus().addAll(runnerMenu, rendererMenu, functionMenu);

        StackPane rootPanel = new StackPane();
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();

        rootPanel.getChildren().addAll(this.functionDrawer.getSurface(), borderPane);
        borderPane.setTop(menuBar);
        borderPane.setBottom(hBox);
        hBox.setSpacing(BOTTOM_LINE_SPACING);

        rendererMenuItem2.setOnAction(event -> {
            this.functionDrawer = new LineChartRenderer();
            rootPanel.getChildren().remove(0);
            rootPanel.getChildren().add(0, functionDrawer.getSurface());
            rendererMenu.setText("Active Renderer: LineChart");
            functionDrawer.reDraw(drawnPoints);
        });

        rendererMenuItem1.setOnAction(event -> {
            this.functionDrawer = new CanvasRenderer(this.windowSize);
            rootPanel.getChildren().remove(0);
            rootPanel.getChildren().add(0, functionDrawer.getSurface());
            rendererMenu.setText("Active Renderer: Canvas");
            functionDrawer.reDraw(drawnPoints);
        });

        hBox.getChildren().addAll(aText, aTextField, xText, stepXTextField, kText, kTextField, clearButton,
                drawButton, stopButton);

        Scene scene = new Scene(rootPanel, windowSize.getX(), windowSize.getY());
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            this.painter.stopCurrentTask();
            ThreadRunner.INSTANCE.stop();
        });
        stage.show();
    }

    private void setNewValuesAndRecalculateFunction(ActionEvent event) {
        drawButton.setDisable(true);
        clearButton.setDisable(true);
        stopButton.setDisable(false);

        if (this.currentContentSupplier == null || !this.currentContentSupplier.iterator().hasNext()) {
            resetCalculator();
        }

        this.painter.run(this.currentContentSupplier, nextPoint -> {
            Platform.runLater(() -> {
                this.functionDrawer.drawNextPoint(nextPoint);
                drawnPoints.add(nextPoint);
            });
        }, this::onCalculationEnd);
    }

    private void onCalculationEnd() {
        this.drawButton.setDisable(false);
        this.clearButton.setDisable(false);
    }

    private void resetCalculator() {
        function.setCoefficients(new Coefficients(Double.parseDouble(aTextField.getText()),
                Double.parseDouble(kTextField.getText())));
        functionCalculator = new FunctionCalculator(0, Double.parseDouble(stepXTextField.getText()),
                this.windowSize.getY(), function);
        functionCalculator.setCalculationValues(Double.parseDouble(stepXTextField.getText()));

        this.currentContentSupplier = functionCalculator;
    }
}