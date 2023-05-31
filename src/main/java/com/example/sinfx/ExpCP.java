package com.example.sinfx;

import java.util.concurrent.CompletableFuture;

public class ExpCP {

    public static void main(String[] args) {

/*        CompletableFuture completableFuture1 = CompletableFuture.runAsync(() -> {

            for (int i = 0; i <= 1; i++) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                sendMessage("1");
            }
        });


        CompletableFuture completableFuture2 = CompletableFuture.runAsync(() -> {


            for (int i = 0; i <= 1; i++) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                sendMessage("2");
            }
        });*/

        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sendMessage("returning niceNice");
            return "niceNice";
        });

        CompletableFuture<Integer> future = completableFuture.thenApplyAsync(result -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sendMessage("returning length");
            return result.length();
        });


        future.whenCompleteAsync((result, exception) -> {

            if (exception == null) {
                try {
                    Thread.sleep(1000);
                    System.out.println(result);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
            }
        });

        sendMessage("started");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void sendMessage(String message) {
        System.out.println(message);
    }
}
