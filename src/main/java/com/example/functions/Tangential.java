package com.example.functions;

import com.example.painter.Point2D;

public class Tangential implements IFunction<Coefficients> {
    private Coefficients coefficients;

    @Override
    public Point2D calculatePointAtX(double xValue) {
        return new Point2D(xValue, coefficients.getA() * Math.tan(coefficients.getK() * xValue));
    }

    @Override
    public void setCoefficients(Coefficients coefficients) {
        this.coefficients = coefficients;
    }
}