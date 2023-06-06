package com.example.painter;

import com.example.runners.IRunner;

import java.util.function.Consumer;

public class AsyncDrawer {

    private IRunner runner;
    private boolean stopCurrentTask;

    public AsyncDrawer(IRunner runner) {
        this.runner = runner;
    }

    public void run(Iterable<Point2D> drawingContent, Consumer<Point2D> taskSupplier, Runnable endSignal) {
        if (this.runner == null) {
            return;
        }

        this.runner.run(() -> {
            for (Point2D currentPoint : drawingContent) {

                if (this.stopCurrentTask) {
                    this.stopCurrentTask = false;
                    break;
                }

                taskSupplier.accept(currentPoint);

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    break;
                }
            }

            endSignal.run();
        });
    }

    public void setRunner(IRunner runner) {
        this.runner = runner;
    }

    public void stopCurrentTask() {
        this.stopCurrentTask = true;
    }
}
