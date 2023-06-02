package com.example.function_calculators;

import com.example.sinfx.FunctionDrawer;
import com.example.sinfx.Point2D;
import javafx.application.Platform;
import java.util.function.Consumer;

public abstract class FunctionCalculationStrategy {
    protected double a = 1;
    protected double k = 1;
    protected double maxX;
    private double stepX;
    private final int sleepTime = 70;
    protected boolean isCalculationRunning;

    protected FunctionDrawer functionDrawer;
    protected Consumer<Void> onCalculationEnd;

    public FunctionCalculationStrategy(FunctionDrawer functionDrawer, Consumer<Void> onCalculationEndMethod, double maxXValue) {
        this.functionDrawer = functionDrawer;
        onCalculationEnd = onCalculationEndMethod;
        maxX = maxXValue;
    }

    public abstract void calculateWithDelay();

    public void setCalculationValues(double aValue, double kValue, double stepX) {

        a = aValue;
        k = kValue;
        this.stepX = stepX;
    }

    protected void calculateFunction(String message) {

        isCalculationRunning = true;
        double x = stepX;
        double y;

        while (x <= maxX && isCalculationRunning && functionDrawer != null) {

            y = a * Math.sin(k * x);

            double finalX = x;
            double finalY = y;

            Platform.runLater(() -> functionDrawer.drawNextPoint(new Point2D(finalX, finalY)));

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(message + " " + x + "   " + y);
            x += stepX;
        }

    }

    public void stopCalculating() {

        isCalculationRunning = false;
    }
}