package com.example.function_calculators;

import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import java.util.function.Consumer;

public abstract class FunctionCalculationStrategy {
    protected double a = 1;
    protected double k = 1;
    protected double maxX = 10;
    protected final double stepX = 0.1;
    protected final int sleepTime = 70;
    protected boolean isCalculationRunning;
    XYChart.Series<Number, Number> XYSeries;
    protected Consumer<Void> onCalculationEnd;

    public FunctionCalculationStrategy(XYChart.Series<Number, Number> targetXYSeries, Consumer<Void> onCalculationEndMethod) {
        XYSeries = targetXYSeries;
        onCalculationEnd = onCalculationEndMethod;
    }

    public abstract void calculateWithDelay();

    public void setCalculationValues(double aValue, double kValue, double maxXValue) {

        a = aValue;
        k = kValue;
        maxX = maxXValue;
    }

    protected void calculateFunction(String message) {

        isCalculationRunning = true;
        double x = 0;
        double y;

        while (x <= maxX && isCalculationRunning && XYSeries != null) {

            y = a * Math.sin(k * x);

            double finalX = x;
            double finalY = y;

            Platform.runLater(() -> XYSeries.getData().add(new XYChart.Data<>(finalX, finalY)));

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(message + " " + x);
            x += stepX;
        }

    }

    public void stopCalculating() {

        isCalculationRunning = false;
    }
}