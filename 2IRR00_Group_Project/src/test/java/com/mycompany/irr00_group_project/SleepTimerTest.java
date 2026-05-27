package com.mycompany.irr00_group_project;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class SleepTimerTest {

    @Test
    void callbackFiredAfterExpiry() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        SleepTimer timer = new SleepTimer(latch::countDown);
        timer.start(1);
        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }

    @Test
    void cancelPreventsCallback() throws InterruptedException {
        AtomicBoolean wasCalled = new AtomicBoolean(false);
        SleepTimer timer = new SleepTimer(() -> wasCalled.set(true));
        timer.start(1);
        timer.cancel();
        Thread.sleep(1500);
        assertFalse(wasCalled.get());
    }

    @Test
    void isRunningTrueAfterStart() {
        SleepTimer timer = new SleepTimer(() -> { });
        timer.start(60);
        assertTrue(timer.isRunning());
        timer.cancel();
    }

    @Test
    void isRunningFalseAfterCancel() {
        SleepTimer timer = new SleepTimer(() -> { });
        timer.start(60);
        timer.cancel();
        assertFalse(timer.isRunning());
    }

    @Test
    void startNonPositiveThrows() {
        SleepTimer timer = new SleepTimer(() -> { });
        assertThrows(IllegalArgumentException.class, () -> timer.start(0));
        assertThrows(IllegalArgumentException.class, () -> timer.start(-1));
    }

    @Test
    void doubleStartRestarts() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        SleepTimer timer = new SleepTimer(latch::countDown);
        timer.start(60);
        timer.start(1);
        assertTrue(latch.await(2, TimeUnit.SECONDS));
    }

}
