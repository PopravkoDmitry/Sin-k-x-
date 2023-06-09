package com.example.functions;

import com.example.painter.Point2D;

import java.util.Iterator;
import java.util.List;

public class FunctionCalculator implements Iterator<Point2D>, Iterable<Point2D> {
    private final double maxX;
    private double stepX;
    private double currentX;
    private List<Point2D> drawnPoints;
    private IFunction function;

    public FunctionCalculator(double startX, double maxXValue, IFunction function) {
        this.maxX = maxXValue;
        this.currentX = startX;
        this.function = function;
    }

    public void setCalculationValues(double stepX) {
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
        return function.calculatePointAtX(x);
    }

    @Override
    public Iterator<Point2D> iterator() {
        return this;
    }
}