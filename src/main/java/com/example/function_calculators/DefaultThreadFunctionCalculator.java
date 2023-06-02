package com.example.function_calculators;

import com.example.sinfx.FunctionDrawer;
import java.util.function.Consumer;

public class DefaultThreadFunctionCalculator extends FunctionCalculationStrategy {


    public DefaultThreadFunctionCalculator(FunctionDrawer functionDrawer, Consumer<Void> onCalculationEndMethod,
                                           double maxXValue) {
        super(functionDrawer, onCalculationEndMethod, maxXValue);
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

