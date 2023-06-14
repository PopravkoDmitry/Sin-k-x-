package com.example.painter;

import javafx.scene.Node;

import java.util.List;

public interface IRenderer {
    void drawNextPoint(Point2D newPoint);
    void reDraw(List<Point2D> drawnPoints);
    void clear();
    Node getSurface();
}