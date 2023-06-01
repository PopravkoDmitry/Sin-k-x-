package com.example.function_calculators;

import javafx.scene.chart.XYChart;
import java.util.function.Consumer;

public class DefaultThreadFunctionCalculator extends FunctionCalculationStrategy {


    public DefaultThreadFunctionCalculator(XYChart.Series<Number, Number> targetXYSeries,
                                           Consumer<Void> onCalculationEndMethod) {
        super(targetXYSeries, onCalculationEndMethod);
    }

    @Override
    public void calculateWithDelay() {

        Runnable functionCalculationRunnable = () -> {

            calculateFunction("Thread");

            if(onCalculationEnd != null) {

                onCalculationEnd.accept(null);
            }
        };

        Thread thread = new Thread(functionCalculationRunnable);
        thread.start();
    }
}

