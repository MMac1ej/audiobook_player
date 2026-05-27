package com.mycompany.irr00_group_project;

import java.io.IOException;
import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AudioPlayerTest {

    private AudioPlayer player;

    /** Sets up a fresh AudioPlayer before each test. */
    @BeforeEach
    void setUp() {
        player = new AudioPlayer();
    }

    @Test
    void setVolumeNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> player.setVolume(-0.1));
    }

    @Test
    void setVolumeAboveOneThrows() {
        assertThrows(IllegalArgumentException.class, () -> player.setVolume(1.1));
    }

    @Test
    void setVolumeAtZeroSucceeds() {
        assertDoesNotThrow(() -> player.setVolume(0.0));
    }

    @Test
    void setVolumeAtOneSucceeds() {
        assertDoesNotThrow(() -> player.setVolume(1.0));
    }

    @Test
    void setSpeedBelowMinThrows() {
        assertThrows(IllegalArgumentException.class, () -> player.setSpeed(0.4));
    }

    @Test
    void setSpeedAboveMaxThrows() {
        assertThrows(IllegalArgumentException.class, () -> player.setSpeed(2.1));
    }

    @Test
    void setSpeedAtMinSucceeds() {
        assertDoesNotThrow(() -> player.setSpeed(0.5));
    }

    @Test
    void setSpeedAtMaxSucceeds() {
        assertDoesNotThrow(() -> player.setSpeed(2.0));
    }

    @Test
    void loadEpisodeMissingFileThrows() {
        Episode ep = new Episode("test", Paths.get("nonexistent_audio_file.mp3"));
        assertThrows(IOException.class, () -> player.loadEpisode(ep));
    }

}
