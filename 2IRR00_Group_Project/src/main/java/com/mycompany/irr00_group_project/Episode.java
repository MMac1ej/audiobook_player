package com.mycompany.irr00_group_project;

import java.nio.file.Path;

/** Represents a single audio file episode within an audiobook. */
public class Episode {

    private final String title;
    private final Path filePath;
    private long durationSeconds;
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
        this.durationSeconds = 0;
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
        this.durationSeconds = seconds;
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
        if (seconds > durationSeconds) {
            throw new IllegalArgumentException(
                "Progress " + seconds + " exceeds duration " + durationSeconds
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
        
        String durationString = "Duration ";
        
        //note integer division always rounds down
        long seconds = durationSeconds % 60;
        long minutes = (durationSeconds / 60) % 60;
        long hours = (durationSeconds / 3600) % 24;
        long days = durationSeconds / 86400;
        
        if (durationSeconds >= 86400) {
            durationString += days + " days";
        }
        
        if (durationSeconds >= 3600) {
            durationString += hours + " hours";
        }
 
        if (durationSeconds >= 60) {
            durationString += minutes + " minutes";
        }
        
        if (durationSeconds >= 1) {
            durationString += seconds + " seconds";
        } else {
            durationString = "Duration 0 seconds";
        }
        
        return title + " " + durationString;
    }

}
