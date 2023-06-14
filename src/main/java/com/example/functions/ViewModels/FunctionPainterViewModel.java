package com.example.functions.ViewModels;

import com.example.functions.Coefficients;
import com.example.functions.FunctionCalculator;
import com.example.functions.IFunction;
import com.example.functions.Sinusoid;
import com.example.painter.AsyncDrawer;
import com.example.painter.CanvasRenderer;
import com.example.painter.IRenderer;
import com.example.painter.Point2D;
import com.example.runners.IRunner;
import com.example.runners.ThreadRunner;
import javafx.application.Platform;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class FunctionPainterViewModel {
    private Iterable<Point2D> currentContentSupplier;
    private IFunction<Coefficients> function = new Sinusoid();
    private List<Point2D> drawnPoints = new ArrayList<>();
    private double a;
    private double k;
    private double step;
    private double maxX;

    public List<Point2D> getDrawnPoints() {
        return new ArrayList<>(drawnPoints);
    }

    public Iterable<Point2D> getCurrentContentSupplier() {
        return currentContentSupplier;
    }

    public void updateValues(double a, double k, double step, double maxX) {
        this.a = a;
        this.k = k;
        this.step = step;
        this.maxX = maxX;
    }

    public void setFunction(IFunction function) {
        this.function = function;
    }

    public void addPoint(Point2D nextPoint) {
        this.drawnPoints.add(nextPoint);
    }
    public void clearFunction() {
        drawnPoints.clear();
        resetCalculator();
    }

    private void resetCalculator() {
        this.function.setCoefficients(new Coefficients(this.a, this.k));
        this.currentContentSupplier =  new FunctionCalculator(0, this.step, this.maxX, this.function);
    }

    public void checkCalculationStatus() {
        if(this.currentContentSupplier == null || !this.currentContentSupplier.iterator().hasNext()) {
            resetCalculator();
        }
    }
}