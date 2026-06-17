package com.mycompany.irr00_group_project;

import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;

/** Utility for running code on the JavaFX application thread in unit tests. */
final class FxTestHelper {

    private FxTestHelper() { }

    /** Ensures the JavaFX toolkit is started before JavaFX tests run. */
    static void startToolkit() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            latch.countDown();
        }
        latch.await();
    }

    /** Runs the given task on the JavaFX thread and blocks until it completes. */
    static void runOnFxThread(Runnable task) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            try {
                task.run();
            } finally {
                latch.countDown();
            }
        });
        latch.await();
    }
}
