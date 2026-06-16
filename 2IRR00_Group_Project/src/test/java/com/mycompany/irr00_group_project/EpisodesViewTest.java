package com.mycompany.irr00_group_project;

import com.mycompany.irr00_group_project.gui.EpisodesView;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for EpisodesView backend logic (episode loading, filtering).
 */
class EpisodesViewTest {

    /** Starts the JavaFX toolkit once for all tests in this class. */
    @BeforeAll
    static void startFxToolkit() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        try {
            Platform.startup(latch::countDown);
        } catch (IllegalStateException e) {
            latch.countDown();
        }
        latch.await();
    }

    private Audiobook buildBook(String title, int episodeCount) {
        Audiobook book = new Audiobook(title, Path.of("/tmp/" + title));
        for (int i = 0; i < episodeCount; i++) {
            book.addEpisode(new Episode("Episode " + i, Path.of("ep" + i + ".mp3")));
        }
        return book;
    }

    private void runOnFxThread(Runnable task) throws InterruptedException {
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

    /** Tests that constructing with an audiobook populates episodes correctly. */
    @Test
    void constructorWithAudiobook_episodeCountMatchesBook() throws InterruptedException {
        Audiobook book = buildBook("Dune", 4);
        EpisodesView[] view = new EpisodesView[1];
        runOnFxThread(() -> view[0] = new EpisodesView(book));
        assertEquals(4, view[0].getEpisodes().size());
    }

    /** Tests that the audiobook title is preserved after construction. */
    @Test
    void constructorWithAudiobook_audiobookTitleIsSet() throws InterruptedException {
        Audiobook book = buildBook("Foundation", 2);
        EpisodesView[] view = new EpisodesView[1];
        runOnFxThread(() -> view[0] = new EpisodesView(book));
        assertEquals("Foundation", view[0].getAudiobook().getTitle());
    }

    /** Tests that setAudiobook replaces the existing episode list. */
    @Test
    void setAudiobook_replacesEpisodeList() throws InterruptedException {
        Audiobook bookA = buildBook("Book A", 2);
        Audiobook bookB = buildBook("Book B", 5);
        EpisodesView[] view = new EpisodesView[1];
        runOnFxThread(() -> {
            view[0] = new EpisodesView(bookA);
            view[0].setAudiobook(bookB);
        });
        assertEquals(5, view[0].getEpisodes().size());
        assertEquals("Book B", view[0].getAudiobook().getTitle());
    }

    /** Tests that constructing with a folder path loads only audio files (.mp3, .wav, .m4a). */
    @Test
    void constructorWithPath_loadsOnlyAudioFiles() throws IOException, InterruptedException {
        Path tempDir = Files.createTempDirectory("episodesViewTest");
        try {
            Files.createTempFile(tempDir, "track1", ".mp3");
            Files.createTempFile(tempDir, "track2", ".wav");
            Files.createTempFile(tempDir, "track3", ".m4a");
            Files.createTempFile(tempDir, "readme", ".txt");
            Files.createTempFile(tempDir, "cover", ".jpg");

            EpisodesView[] view = new EpisodesView[1];
            runOnFxThread(() -> view[0] = new EpisodesView(tempDir));

            assertEquals(3, view[0].getEpisodes().size());
        } finally {
            Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException ignored) {
                        // best-effort cleanup
                    }
                });
        }
    }

    /** Tests that a folder with no audio files yields zero episodes. */
    @Test
    void constructorWithPath_emptyFolder_zeroEpisodes() throws IOException, InterruptedException {
        Path tempDir = Files.createTempDirectory("episodesViewEmpty");
        try {
            EpisodesView[] view = new EpisodesView[1];
            runOnFxThread(() -> view[0] = new EpisodesView(tempDir));
            assertEquals(0, view[0].getEpisodes().size());
        } finally {
            Files.delete(tempDir);
        }
    }

    /** Tests that setAudiobook(Path) updates the episode list from the folder contents. */
    @Test
    void setAudiobookPath_updatesEpisodeList() throws IOException, InterruptedException {
        Path tempDir = Files.createTempDirectory("episodesViewSetPath");
        try {
            Files.createTempFile(tempDir, "chapter1", ".mp3");
            Files.createTempFile(tempDir, "chapter2", ".mp3");

            Audiobook initialBook = buildBook("Initial", 1);
            EpisodesView[] view = new EpisodesView[1];
            runOnFxThread(() -> {
                view[0] = new EpisodesView(initialBook);
                view[0].setAudiobook(tempDir);
            });

            assertEquals(2, view[0].getEpisodes().size());
        } finally {
            Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException ignored) {
                        // best-effort cleanup
                    }
                });
        }
    }
}
