package com.example.painter;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.List;

public class CanvasRenderer implements IRenderer {

    private final Canvas canvas;
    private Point2D previousPoint;
    private Point2D startPoint;

    public CanvasRenderer(Point2D canvasSize) {
        this.canvas = new Canvas(canvasSize.getX(), canvasSize.getY());
        this.startPoint = new Point2D(0, canvasSize.getY()/2);
        this.previousPoint = startPoint;
        this.canvas.getGraphicsContext2D().setStroke(Color.RED);
        this.canvas.getGraphicsContext2D().setFill(Color.WHITE);
    }

    @Override
    public void drawNextPoint(Point2D newPoint) {
        Point2D nextPoint = new Point2D(newPoint.getX(), this.previousPoint.getY() - newPoint.getY());

        this.canvas.getGraphicsContext2D()
                .strokeLine(this.previousPoint.getX(), this.previousPoint.getY(), nextPoint.getX(), nextPoint.getY());

        this.previousPoint = nextPoint;
    }

    @Override
    public void drawNextPoint(Point2D newPoint, List<Point2D> container) {
        this.drawNextPoint(newPoint);
        container.add(newPoint);
    }

    @Override
    public void reDraw(List<Point2D> drawnPoints) {
        if(drawnPoints == null) {
            return;
        }

        for (Point2D point : drawnPoints) {
            this.drawNextPoint(point);
        }
    }

    @Override
    public Node getSurface() {
        return this.canvas;
    }

    public void clear() {
        this.canvas.getGraphicsContext2D().fillRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
        resetStartPoint();
    }

    public void resetStartPoint() {
        this.previousPoint = this.startPoint;
    }
}