package com.example.sinfx;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FunctionDrawer {

    private Point2D previousPoint;
    private Point2D startPoint;
    private final GraphicsContext graphicsContext;
    private Point2D canvasSize;

    public FunctionDrawer(Canvas canvasToDraw, Point2D startPoint) {
        graphicsContext = canvasToDraw.getGraphicsContext2D();
        this.startPoint = startPoint;
        previousPoint = startPoint;
        graphicsContext.setStroke(Color.RED);
        graphicsContext.setFill(Color.WHITE);
        canvasSize = new Point2D(canvasToDraw.getWidth(), canvasToDraw.getWidth());
    }

    public void drawNextPoint(Point2D newPoint) {

        var nextPoint = new Point2D(newPoint.getX(), previousPoint.getY() - newPoint.getY());

        graphicsContext.strokeLine(previousPoint.getX(), previousPoint.getY(),
                nextPoint.getX(), nextPoint.getY());

        previousPoint = nextPoint;
    }

    public void Clear()
    {
        graphicsContext.fillRect(0, 0, canvasSize.getX(), canvasSize.getY());
        previousPoint = startPoint;
    }
}

