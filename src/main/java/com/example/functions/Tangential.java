package com.example.functions;

import com.example.functions.IFunction;
import com.example.painter.Point2D;

public class Tangential implements IFunction {
    @Override
    public Point2D calculatePointAtX(double aValue, double kValue, double xValue) {
        return new Point2D(xValue, aValue * Math.tan(kValue * xValue));
    }
}
