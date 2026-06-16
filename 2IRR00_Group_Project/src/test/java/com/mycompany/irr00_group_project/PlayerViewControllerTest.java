package com.mycompany.irr00_group_project;

import com.mycompany.irr00_group_project.gui.PlayerViewController;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/** Tests for PlayerViewController. */
class PlayerViewControllerTest {

    private AudioPlayer audioPlayer;
    private ProgressStore progressStore;
    private TimestampStore timestampStore;
    private Path tempFile;
    private PlayerViewController controller;

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

    /** Creates fresh collaborators before each test. */
    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("progress", ".properties");
        progressStore = new ProgressStore(tempFile);
        audioPlayer = new StubAudioPlayer();
        timestampStore = new TimestampStore();
        controller = new PlayerViewController(audioPlayer, progressStore, timestampStore);
    }

    /** Removes the temporary progress file after each test. */
    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void noBook_showsDefaultTitle() throws Exception {
        Scene scene = buildOnFxThread();
        Label label = (Label) scene.lookup("#episodeTitleLabel");
        assertEquals("No episode loaded", label.getText());
    }

    @Test
    void noBook_prevAndNextBothDisabled() throws Exception {
        Scene scene = buildOnFxThread();
        Button prev = (Button) scene.lookup("#prevButton");
        Button next = (Button) scene.lookup("#nextButton");
        assertTrue(prev.isDisable());
        assertTrue(next.isDisable());
    }

    @Test
    void singleEpisode_prevAndNextBothDisabled() throws Exception {
        controller.setBook(buildBook(1));
        Scene scene = buildOnFxThread();
        Button prev = (Button) scene.lookup("#prevButton");
        Button next = (Button) scene.lookup("#nextButton");
        assertTrue(prev.isDisable());
        assertTrue(next.isDisable());
    }

    @Test
    void firstOfMultiple_prevDisabledNextEnabled() throws Exception {
        controller.setBook(buildBook(3));
        Scene scene = buildOnFxThread();
        Button prev = (Button) scene.lookup("#prevButton");
        Button next = (Button) scene.lookup("#nextButton");
        assertTrue(prev.isDisable());
        assertFalse(next.isDisable());
    }

    @Test
    void lastOfMultiple_prevEnabledNextDisabled() throws Exception {
        controller.setBook(buildBook(3));
        Scene scene = buildOnFxThread();
        Button next = (Button) scene.lookup("#nextButton");
        runOnFxThread(() -> next.fire());
        runOnFxThread(() -> next.fire());
        Button prev = (Button) scene.lookup("#prevButton");
        assertFalse(prev.isDisable());
        assertTrue(next.isDisable());
    }

    @Test
    void progressIsRestoredOnLoad() throws Exception {
        Audiobook book = buildBook(1);
        String key = book.getEpisode(0).getFilePath().toString();
        progressStore.saveProgress(key, 1L);
        controller.setBook(book);
        Scene scene = buildOnFxThread();
        Slider slider = (Slider) scene.lookup("#seekSlider");
        assertEquals(1.0, slider.getValue(), 0.001);
    }

    private Scene buildOnFxThread() throws Exception {
        Scene[] result = new Scene[1];
        Exception[] err = new Exception[1];
        runOnFxThread(() -> {
            try {
                result[0] = controller.buildScene();
            } catch (IOException e) {
                err[0] = e;
            }
        });
        if (err[0] != null) {
            throw err[0];
        }
        return result[0];
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

    private Audiobook buildBook(int episodeCount) {
        Audiobook book = new Audiobook("Test Book", null);
        for (int i = 0; i < episodeCount; i++) {
            book.addEpisode(new Episode("Episode " + i, Path.of("ep" + i + ".mp3")));
        }
        return book;
    }

    private static class StubAudioPlayer extends AudioPlayer {

        @Override
        public void loadEpisode(Episode episode) {
            // intentional no-op — avoids IOException from missing files in tests
        }

    }

}
