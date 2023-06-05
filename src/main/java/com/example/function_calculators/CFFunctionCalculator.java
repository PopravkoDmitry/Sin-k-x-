package com.example.function_calculators;

import com.example.sinfx.IFunctionDrawer;

import java.util.concurrent.CompletableFuture;

public class CFFunctionCalculator extends FunctionCalculationStrategy {
    public CFFunctionCalculator(IFunctionDrawer functionDrawer,
                                double maxXValue) {
        super(functionDrawer, maxXValue);
    }

    @Override
    public void calculateWithDelay() {
        CompletableFuture<Void> functionCalculationCompletableFuture =
                CompletableFuture.runAsync(() -> calculateFunction("CompletableFuture"));

        functionCalculationCompletableFuture.thenRun(() -> onCalculationEnd.accept(null));
    }
}