package com.example.painter;

import javafx.scene.Node;

public interface IRenderer {
    void drawNextPoint(Point2D newPoint);
    void clear();
    Node getSurface();
}