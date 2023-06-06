package com.example.functions;

import com.example.painter.Point2D;

import java.util.Iterator;

public class SinusCalculator implements Iterator<Point2D>, Iterable<Point2D> {
    private final double maxX;
    private double a = 1;
    private double k = 1;
    private double stepX;
    private double currentX;

    public SinusCalculator(double startX, double maxXValue) {
        this.maxX = maxXValue;
        this.currentX = startX;
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
        return new Point2D(x, this.a * Math.sin(this.k * x));
    }

    @Override
    public Iterator<Point2D> iterator() {
        return this;
    }
}