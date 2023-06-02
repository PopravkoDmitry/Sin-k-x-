package com.example.function_calculators;

import com.example.sinfx.FunctionDrawer;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class CFFunctionCalculator extends FunctionCalculationStrategy {
    public CFFunctionCalculator(FunctionDrawer functionDrawer, Consumer<Void> onCalculationEndMethod,
                                double maxXValue) {
        super(functionDrawer, onCalculationEndMethod, maxXValue);
    }

    @Override
    public void calculateWithDelay() {
        CompletableFuture<Void> functionCalculationCompletableFuture =
                CompletableFuture.runAsync(() -> calculateFunction("CompletableFuture"));

        functionCalculationCompletableFuture.thenRun(() -> onCalculationEnd.accept(null));
    }
}