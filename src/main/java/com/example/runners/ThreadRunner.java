package com.example.runners;

public class ThreadRunner implements Runnable, IRunner {

    public static final ThreadRunner INSTANCE = new ThreadRunner();

    private final Thread taskThread;
    Runnable task;
    private boolean stopRequest;
    private boolean taskInQueue;

    private ThreadRunner() {
        this.taskThread = new Thread(this);
        this.taskThread.start();
    }

    @Override
    public synchronized void run() {
        while (true) {
            try {
                while (!this.taskInQueue) {
                    this.wait(500);
                    if (this.stopRequest) {
                        return;
                    }
                }

            } catch (InterruptedException e) {
                return;
            }

            this.task.run();
            this.taskInQueue = false;
        }
    }

    @Override
    public synchronized void run(Runnable task) {
        this.task = task;
        this.taskInQueue = true;
        this.notifyAll();
    }

    public void stop() {
        this.stopRequest = true;
        try {
            this.taskThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

