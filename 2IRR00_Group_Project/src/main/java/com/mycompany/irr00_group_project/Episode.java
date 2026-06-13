package com.mycompany.irr00_group_project;

import java.nio.file.Path;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

/** Represents a single audio file episode within an audiobook. */
public class Episode {

    private final String title;
    private final Path filePath;
    private final LongProperty durationSeconds = new SimpleLongProperty(0);
    private long progressSeconds;

    /**
     * Creates an Episode with zero duration and zero progress.
     *
     * @param title the display name of this episode
     * @param filePath the path to the audio file on disk
     */
    public Episode(String title, Path filePath) {
        this.title = title;
        this.filePath = filePath;
        this.progressSeconds = 0;
    }

    /** Returns the display name of this episode. */
    public String getTitle() {
        return title;
    }

    /** Returns the path to the audio file on disk. */
    public Path getFilePath() {
        return filePath;
    }

    /** Returns the total duration of this episode in seconds. */
    public long getDuration() {
        return durationSeconds.get();
    }

    /** Returns the duration property (for UI binding). */
    public LongProperty durationProperty() {
        return durationSeconds;
    }

    /** Returns the current playback progress of this episode in seconds. */
    public long getProgress() {
        return progressSeconds;
    }

    /**
     * Sets the total duration of this episode.
     *
     * @param seconds the duration in seconds
     * @throws IllegalArgumentException if seconds is negative
     */
    public void setDuration(long seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Duration cannot be negative: " + seconds);
        }
        durationSeconds.set(seconds);
    }

    /**
     * Sets the playback progress for this episode.
     *
     * @param seconds the progress in seconds
     * @throws IllegalArgumentException if seconds is negative or exceeds the total duration
     */
    public void setProgress(long seconds) {
        if (seconds < 0) {
            throw new IllegalArgumentException("Progress cannot be negative: " + seconds);
        }
        if (durationSeconds.get() > 0 && seconds > durationSeconds.get()) {
            throw new IllegalArgumentException(
                "Progress " + seconds + " exceeds duration " + durationSeconds.get()
            );
        }
        this.progressSeconds = seconds;
    }
    
    /**
     * implements toString.
     * @return title of episode
     */
    @Override
    public String toString() {
        long total = durationSeconds.get();
        if (total < 1) {
            return title + " Duration 0 seconds";
        }

        long seconds = total % 60;
        long minutes = (total / 60) % 60;
        long hours = (total / 3600) % 24;
        long days = total / 86400;

        String duration;
        if (days > 0) {
            duration = String.format(
                "%d days %d hours %d minutes %d seconds", days, hours, minutes, seconds);
        } else if (hours > 0) {
            duration = String.format("%d hours %d minutes %d seconds", hours, minutes, seconds);
        } else if (minutes > 0) {
            duration = String.format("%d minutes %d seconds", minutes, seconds);
        } else {
            duration = String.format("%d seconds", seconds);
        }

        return title + " Duration " + duration;
    }

}
