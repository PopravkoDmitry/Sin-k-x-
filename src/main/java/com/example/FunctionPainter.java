package com.example;

import com.example.functions.SinusCalculator;
import com.example.runners.CompletableRunner;
import com.example.runners.ThreadRunner;
import com.example.painter.AsyncDrawer;
import com.example.painter.CanvasRenderer;
import com.example.painter.IRenderer;
import com.example.painter.Point2D;
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

public class FunctionPainter extends Application {
    private final double BOTTOM_LINE_SPACING = 5;

    private Iterable<Point2D> currentContentSupplier;
    private AsyncDrawer painter;

    private TextField aTextField;
    private TextField stepXTextField;
    private TextField kTextField;
    private Button drawButton;
    private Button stopButton;
    private final Point2D windowSize = new Point2D(900, 600);

    private IRenderer functionDrawer;

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

        drawButton = new Button("Draw y(x) = A*sin(k*x)");
        stopButton = new Button("Stop");
        stopButton.setDisable(true);

        drawButton.setOnAction(this::setNewValuesAndRecalculateFunction);
        stopButton.setOnAction(event -> {
            this.painter.stopCurrentTask();
            drawButton.setDisable(false);
            stopButton.setDisable(true);
        });

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Active Calculation Type: Default Thread");

        MenuItem menuItem1 = new MenuItem("Thread");
        menuItem1.setOnAction(event -> {
            this.painter.setRunner(ThreadRunner.INSTANCE);
            menu.setText("Calculation runner: Thread");
        });

        MenuItem menuItem2 = new MenuItem("CompletableFuture");
        menuItem2.setOnAction(event -> {
            this.painter.setRunner(new CompletableRunner());
            menu.setText("Calculation runner: CompletableFuture");
        });

        menu.getItems().addAll(menuItem1, menuItem2);
        menuBar.getMenus().add(menu);

        StackPane rootPanel = new StackPane();
        BorderPane borderPane = new BorderPane();
        HBox hBox = new HBox();

        rootPanel.getChildren().addAll(this.functionDrawer.getSurface(), borderPane);
        borderPane.setTop(menuBar);
        borderPane.setBottom(hBox);
        hBox.setSpacing(BOTTOM_LINE_SPACING);

        Button clearButton = new Button("Clear canvas");
        clearButton.setOnAction(event -> this.functionDrawer.clear());

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
        stopButton.setDisable(false);

        if (this.currentContentSupplier == null || !this.currentContentSupplier.iterator().hasNext()) {
            SinusCalculator sinusCalculator = new SinusCalculator(0, this.windowSize.getY());
            sinusCalculator.setCalculationValues(Double.parseDouble(aTextField.getText()),
                    Double.parseDouble(kTextField.getText()), Double.parseDouble(stepXTextField.getText()));

            this.currentContentSupplier = sinusCalculator;
        }

        this.painter.run(this.currentContentSupplier, nextPoint -> {
            Platform.runLater(() -> this.functionDrawer.drawNextPoint(nextPoint));
        }, this::onCalculationEnd);

//        functionDrawer.clear();
    }

    private void onCalculationEnd() {
        this.drawButton.setDisable(false);
    }
}