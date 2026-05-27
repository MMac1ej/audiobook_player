package com.mycompany.irr00_group_project;

import java.util.Timer;
import java.util.TimerTask;

/** Calls a callback after a specified delay, with support for cancellation and restart. */
public class SleepTimer {

    private final Runnable onExpire;
    private Timer timer;
    private volatile boolean running;

    /**
     * Creates a SleepTimer that invokes the given callback on expiry.
     *
     * @param onExpire the callback to run when the timer expires
     */
    public SleepTimer(Runnable onExpire) {
        this.onExpire = onExpire;
    }

    /**
     * Starts the timer for the given number of seconds.
     * If the timer is already running, it is cancelled and restarted.
     *
     * @param seconds the delay in seconds before the callback is invoked
     * @throws IllegalArgumentException if seconds is zero or negative
     */
    public void start(long seconds) {
        if (seconds <= 0) {
            throw new IllegalArgumentException("Duration must be positive: " + seconds);
        }
        if (timer != null) {
            timer.cancel();
        }
        running = true;
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                running = false;
                onExpire.run();
            }
        }, seconds * 1000L);
    }

    /** Cancels the timer. The callback will not be invoked. */
    public void cancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        running = false;
    }

    /** Returns {@code true} if the timer is currently counting down. */
    public boolean isRunning() {
        return running;
    }

}
