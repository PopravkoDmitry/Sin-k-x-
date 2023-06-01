package com.example.function_calculators;

import javafx.scene.chart.XYChart;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class CFFunctionCalculator extends FunctionCalculationStrategy {
    public CFFunctionCalculator(XYChart.Series<Number, Number> targetXYSeries, Consumer<Void> onCalculationEndMethod) {
        super(targetXYSeries, onCalculationEndMethod);
    }

    @Override
    public void calculateWithDelay() {
        CompletableFuture<Void> functionCalculationCompletableFuture =
                CompletableFuture.runAsync(() -> calculateFunction("CompletableFuture"));

        functionCalculationCompletableFuture.thenRun(() -> onCalculationEnd.accept(null));
    }
}