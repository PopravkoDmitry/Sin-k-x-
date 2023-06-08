package com.example.functions;

import com.example.painter.Point2D;

public class Sinusoid implements IFunction {


    @Override
    public Point2D calculatePointAtX(double aValue, double kValue, double xValue) {
        return new Point2D(xValue, aValue * Math.sin(kValue * xValue));
    }
}
