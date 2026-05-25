package com.mycompany.irr00_group_project;

import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EpisodeTest {

    private Episode episode;

    /** Sets up a fresh Episode before each test. */
    @BeforeEach
    void setUp() {
        episode = new Episode("Chapter 1", Paths.get("chapter1.mp3"));
    }

    @Test
    void constructorSetsTitle() {
        assertEquals("Chapter 1", episode.getTitle());
    }

    @Test
    void constructorSetsFilePath() {
        assertEquals(Paths.get("chapter1.mp3"), episode.getFilePath());
    }

    @Test
    void defaultProgressIsZero() {
        assertEquals(0, episode.getProgress());
    }

    @Test
    void defaultDurationIsZero() {
        assertEquals(0, episode.getDuration());
    }

    @Test
    void setProgressToZeroSucceeds() {
        episode.setDuration(3600);
        episode.setProgress(0);
        assertEquals(0, episode.getProgress());
    }

    @Test
    void setProgressToMaxSucceeds() {
        episode.setDuration(3600);
        episode.setProgress(3600);
        assertEquals(3600, episode.getProgress());
    }

    @Test
    void setProgressToMidValueSucceeds() {
        episode.setDuration(3600);
        episode.setProgress(1800);
        assertEquals(1800, episode.getProgress());
    }

    @Test
    void setProgressNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> episode.setProgress(-1));
    }

    @Test
    void setProgressExceedsDurationThrows() {
        episode.setDuration(100);
        assertThrows(IllegalArgumentException.class, () -> episode.setProgress(101));
    }

    @Test
    void setDurationNegativeThrows() {
        assertThrows(IllegalArgumentException.class, () -> episode.setDuration(-1));
    }

    @Test
    void setDurationSucceeds() {
        episode.setDuration(7200);
        assertEquals(7200, episode.getDuration());
    }

}