package com.mycompany.irr00_group_project;

import java.io.IOException;

/** Defines the playback operations for an audio episode. */
public interface PlaybackController {

    /** Starts or resumes playback of the current episode. */
    void play();

    /** Pauses playback, retaining the current position. */
    void pause();

    /** Stops playback. */
    void stop();

    /**
     * Seeks to the given position within the current episode.
     *
     * @param seconds the target position in seconds
     */
    void seek(long seconds);

    /**
     * Sets the playback volume.
     *
     * @param volume a value in [0.0, 1.0] where 0.0 is mute and 1.0 is full volume
     * @throws IllegalArgumentException if volume is outside [0.0, 1.0]
     */
    void setVolume(double volume);

    /**
     * Sets the playback speed.
     *
     * @param rate a value in [0.5, 2.0] where 1.0 is normal speed
     * @throws IllegalArgumentException if rate is outside [0.5, 2.0]
     */
    void setSpeed(double rate);

    /** Returns the current playback position in seconds. */
    long getCurrentPosition();

    /** Returns {@code true} if audio is currently playing. */
    boolean isPlaying();

    /**
     * Loads the given episode and prepares it for playback.
     *
     * @param episode the episode to load
     * @throws IOException if the audio file cannot be read
     */
    void loadEpisode(Episode episode) throws IOException;

}
