package com.mycompany.irr00_group_project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

class LibraryTest {

    @TempDir
    Path tempDir;

    private Library library;

    /** Sets up a fresh Library before each test. */
    @BeforeEach
    void setUp() {
        library = new Library();
    }

    @Test
    void newLibraryHasZeroAudiobooks() {
        assertEquals(0, library.getAudiobookCount());
    }

    @Test
    void loadFromEmptyFolderAddsNothing() throws IOException {
        library.loadFromFolder(tempDir);
        assertEquals(0, library.getAudiobookCount());
    }

    @Test
    void loadFromFolderCreatesAudiobookAndEpisode() throws IOException {
        Path subDir = Files.createDirectory(tempDir.resolve("The Hobbit"));
        Files.createFile(subDir.resolve("chapter1.mp3"));

        library.loadFromFolder(tempDir);

        assertEquals(1, library.getAudiobookCount());
        assertEquals(1, library.getAudiobooks().get(0).getEpisodeCount());
    }

    @Test
    void loadFromFolderHandlesOneHundredFiles() throws IOException {
        for (int i = 0; i < 10; i++) {
            Path sub = Files.createDirectory(tempDir.resolve("Book" + i));
            for (int j = 0; j < 10; j++) {
                Files.createFile(sub.resolve("ep" + j + ".mp3"));
            }
        }

        library.loadFromFolder(tempDir);

        assertEquals(10, library.getAudiobookCount());
    }

    @Test
    void addNullAudiobookThrows() {
        assertThrows(IllegalArgumentException.class, () -> library.addAudiobook(null));
    }

    @Test
    void searchReturnsMatchingAudiobook() {
        library.addAudiobook(new Audiobook("The Hobbit"));
        library.addAudiobook(new Audiobook("Dune"));

        List<Audiobook> result = library.search("Hobbit");

        assertEquals(1, result.size());
        assertEquals("The Hobbit", result.get(0).getTitle());
    }

    @Test
    void searchNoMatchReturnsEmptyList() {
        library.addAudiobook(new Audiobook("The Hobbit"));

        List<Audiobook> result = library.search("xyz");

        assertEquals(0, result.size());
    }

    @Test
    void searchEmptyQueryReturnsAll() {
        library.addAudiobook(new Audiobook("The Hobbit"));
        library.addAudiobook(new Audiobook("Dune"));

        List<Audiobook> result = library.search("");

        assertEquals(2, result.size());
    }

}
