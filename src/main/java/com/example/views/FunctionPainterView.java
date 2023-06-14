package com.example.views;

import com.example.functions.Coefficients;
import com.example.functions.FunctionCalculator;
import com.example.functions.Sinusoid;
import com.example.functions.Tangential;
import com.example.functions.ViewModels.FunctionPainterViewModel;
import com.example.painter.*;
import com.example.runners.CompletableRunner;
import com.example.runners.ThreadRunner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FunctionPainterView extends Application {
    private final Point2D windowSize = new Point2D(900, 600);
    private FunctionPainterViewModel viewModel;
    private IRenderer functionDrawer;
    private AsyncDrawer asyncDrawer;
    private CoefficientsView coefficientsView;
    private StackPane rootPain;
    private Menu rendererMenu;

    @Override
    public void start(Stage primaryStage) {
        this.viewModel = new FunctionPainterViewModel();
        this.functionDrawer = new CanvasRenderer(this.windowSize);
        asyncDrawer = new AsyncDrawer(ThreadRunner.INSTANCE);

        Menu runnerMenu = new Menu("Active Calculation Type: Default Thread");
        MenuItem runnerMenuItemThread = new MenuItem("Thread");
        MenuItem runnerMenuItemCompFuture = new MenuItem("CompletableFuture");

        rendererMenu = new Menu("Active Renderer: Canvas");
        MenuItem rendererMenuItemCanvas = new MenuItem("Canvas");
        MenuItem rendererMenuItemLineChart = new MenuItem("LineChart");

        Menu functionMenu = new Menu("Active Function: Sinus");
        MenuItem functionMenuItemSinus = new MenuItem("Sinus");
        MenuItem functionMenuItemTangent = new MenuItem("Tangent");
        MenuBar menuBar = new MenuBar();

        runnerMenu.getItems().addAll(runnerMenuItemThread, runnerMenuItemCompFuture);
        rendererMenu.getItems().addAll(rendererMenuItemCanvas, rendererMenuItemLineChart);
        functionMenu.getItems().addAll(functionMenuItemSinus, functionMenuItemTangent);
        menuBar.getMenus().addAll(runnerMenu, rendererMenu, functionMenu);

        Button clearButton = new Button("Clear");
        Button drawButton = new Button("Draw Function");
        Button stopButton = new Button("Stop");
        this.coefficientsView = new CoefficientsView();
        this.coefficientsView.setSpacing(5);
        this.coefficientsView.getChildren().addAll(clearButton, drawButton, stopButton);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setBottom(this.coefficientsView);
        rootPain = new StackPane();
        rootPain.getChildren().addAll(this.functionDrawer.getSurface(), borderPane);
        Scene scene = new Scene(rootPain);
        primaryStage.setScene(scene);

        drawButton.setOnAction(event -> {
            drawButton.setDisable(true);
            clearButton.setDisable(true);
            stopButton.setDisable(false);

            viewModel.updateValues(coefficientsView.getA(), coefficientsView.getK(), coefficientsView.getStep(), windowSize.getX());
            viewModel.checkCalculationStatus();

            this.asyncDrawer.run(this.viewModel.getCurrentContentSupplier(), nextPoint -> Platform.runLater(() -> {
                this.functionDrawer.drawNextPoint(nextPoint);
                this.viewModel.addPoint(nextPoint);
            }), () -> {
                drawButton.setDisable(false);
                clearButton.setDisable(false);
            });
        });

        clearButton.setOnAction(event -> {
            this.viewModel.clearFunction();
            this.functionDrawer.clear();
        });

        stopButton.setOnAction(event -> {
            this.asyncDrawer.stopCurrentTask();
            drawButton.setDisable(false);
            stopButton.setDisable(true);
        });

        runnerMenuItemThread.setOnAction(event -> {
            this.asyncDrawer.setRunner(ThreadRunner.INSTANCE);
            runnerMenu.setText("Calculation runner: Thread");
        });

        runnerMenuItemCompFuture.setOnAction(event -> {
            this.asyncDrawer.setRunner(new CompletableRunner());
            runnerMenu.setText("Calculation runner: CompletableFuture");
        });

        rendererMenuItemCanvas.setOnAction(event -> {
            this.resetRenderer(new CanvasRenderer(this.windowSize), "Active Renderer: Canvas");
        });

        rendererMenuItemLineChart.setOnAction(event -> {
            resetRenderer(new LineChartRenderer(), "Active Renderer: LineChart");
        });

        functionMenuItemSinus.setOnAction(event -> {
            viewModel.setFunction(new Sinusoid());
            functionMenu.setText("Active Function: Sinus");
        });

        functionMenuItemTangent.setOnAction(event -> {
            viewModel.setFunction(new Tangential());
            functionMenu.setText("Active Function: Tangent");
        });

        primaryStage.show();
    }

    private void resetRenderer(IRenderer renderer, String message) {
        this.functionDrawer = renderer;
        rootPain.getChildren().remove(0);
        rootPain.getChildren().add(0, functionDrawer.getSurface());
        rendererMenu.setText(message);
        functionDrawer.reDraw(viewModel.getDrawnPoints());
    }
}
