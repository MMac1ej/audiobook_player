package com.mycompany.irr00_group_project;

import java.nio.file.Paths;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AudiobookTest {

    private Audiobook audiobook;
    private Episode episode;

    /** Sets up a fresh Audiobook and Episode before each test. */
    @BeforeEach
    void setUp() {
        audiobook = new Audiobook("The Hobbit");
        episode = new Episode("Chapter 1", Paths.get("chapter1.mp3"));
    }

    @Test
    void constructorSetsTitle() {
        assertEquals("The Hobbit", audiobook.getTitle());
    }

    @Test
    void newAudiobookHasZeroEpisodes() {
        assertEquals(0, audiobook.getEpisodeCount());
    }

    @Test
    void addEpisodeIncreasesCount() {
        audiobook.addEpisode(episode);
        assertEquals(1, audiobook.getEpisodeCount());
    }

    @Test
    void getEpisodeReturnsAddedEpisode() {
        audiobook.addEpisode(episode);
        assertSame(episode, audiobook.getEpisode(0));
    }

    @Test
    void addNullEpisodeThrows() {
        assertThrows(IllegalArgumentException.class, () -> audiobook.addEpisode(null));
    }

    @Test
    void getEpisodeNegativeIndexThrows() {
        assertThrows(IndexOutOfBoundsException.class, () -> audiobook.getEpisode(-1));
    }

    @Test
    void getEpisodeOutOfBoundsThrows() {
        assertThrows(IndexOutOfBoundsException.class, () -> audiobook.getEpisode(0));
    }

    @Test
    void getEpisodesIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class,
                () -> audiobook.getEpisodes().add(episode));
    }

}