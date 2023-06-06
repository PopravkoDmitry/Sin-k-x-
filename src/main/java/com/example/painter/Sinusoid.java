package com.example.painter;

public class Sinusoid implements IFunction{
    @Override
    public double calculateYValue(double aValue, double kValue, double xValue) {

        double yValue;
        yValue = aValue * Math.sin(kValue * xValue);
        return yValue;
    }
}
