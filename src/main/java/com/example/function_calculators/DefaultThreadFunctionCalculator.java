package com.example.function_calculators;

import com.example.sinfx.IFunctionDrawer;

public class DefaultThreadFunctionCalculator extends FunctionCalculationStrategy {


    public DefaultThreadFunctionCalculator(IFunctionDrawer functionDrawer, double maxXValue) {
        super(functionDrawer, maxXValue);
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

