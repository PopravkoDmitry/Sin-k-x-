package com.example.functions;

import com.example.painter.Point2D;

public class Sinusoid implements IFunction<Coefficients> {
    private Coefficients coefficients;

    @Override
    public void setCoefficients(Coefficients coefficients) {
        this.coefficients = coefficients;
    }

    @Override
    public Point2D calculatePointAtX(double xValue) {
        return new Point2D(xValue,
                coefficients.getA() * Math.sin(coefficients.getK() * xValue));
    }
}
