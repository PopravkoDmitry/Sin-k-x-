package com.example.functions;

import com.example.painter.Point2D;

public interface IFunction<T> {
    Point2D calculatePointAtX(double xValue);
    void setCoefficients(T coefficients);
}
