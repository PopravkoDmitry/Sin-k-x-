package com.example.painter;

import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

public class LineChartRenderer implements IRenderer{

    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> xyData;

    public LineChartRenderer() {
        NumberAxis xNumberAxis = new NumberAxis();
        NumberAxis yNumberAxis = new NumberAxis();
        this.xyData = new XYChart.Series<>();
        this.lineChart = new LineChart<>(xNumberAxis, yNumberAxis);
        this.lineChart.getData().add(this.xyData);
    }

    @Override
    public void drawNextPoint(Point2D newPoint) {
        xyData.getData().add(new XYChart.Data<>(newPoint.getX(), newPoint.getY()));
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
    public void clear() {
        lineChart.getData().remove(xyData);
        xyData = new XYChart.Series<>();
        lineChart.getData().add(xyData);
    }

    @Override
    public Node getSurface() {
        return this.lineChart;
    }
}