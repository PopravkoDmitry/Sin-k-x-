package com.example.runners;

import java.util.concurrent.CompletableFuture;

public class CompletableRunner implements IRunner {
    @Override
    public void run(Runnable task) {
        CompletableFuture.runAsync(task);
    }
}