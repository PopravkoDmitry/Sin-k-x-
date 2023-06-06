package com.example.painter;

public class Tangential implements IFunction{
    @Override
    public double calculateYValue(double aValue, double kValue, double xValue) {

        double yValue;
        yValue = aValue * Math.tan(kValue * xValue);
        return yValue;
    }
}
