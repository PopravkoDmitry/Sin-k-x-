package com.example.functions;

import com.example.painter.Point2D;

import java.util.Iterator;

public class FunctionCalculator implements Iterator<Point2D>, Iterable<Point2D> {
    private final double maxX;
    private double a = 1;
    private double k = 1;
    private double stepX;
    private double currentX;
    private IFunction function;

    public FunctionCalculator(double startX, double maxXValue, IFunction function) {
        this.maxX = maxXValue;
        this.currentX = startX;
        this.function = function;
    }

    public void setCalculationValues(double aValue, double kValue, double stepX) {
        this.a = aValue;
        this.k = kValue;
        this.stepX = stepX;
    }

    @Override
    public boolean hasNext() {
        return this.currentX <= this.maxX;
    }

    @Override
    public Point2D next() {
        double x = this.currentX;
        this.currentX += this.stepX;
        return function.calculatePointAtX(this.a, this.k, x);
    }

    @Override
    public Iterator<Point2D> iterator() {
        return this;
    }
}