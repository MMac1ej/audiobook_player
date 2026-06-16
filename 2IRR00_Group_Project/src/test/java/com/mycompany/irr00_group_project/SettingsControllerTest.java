package com.mycompany.irr00_group_project;

import com.mycompany.irr00_group_project.gui.SettingsController;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SettingsController business logic.
 * JavaFX toolkit is started once via Platform.startup().
 */
class SettingsControllerTest {

    private SettingsController controller;
    private Scene scene;

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

    /** Restores default Settings after each test to avoid polluting the singleton for other tests. */
    @AfterEach
    void tearDown() {
        Settings.getInstance().setDefaults();
    }

    /** Resets Settings to defaults and loads a fresh FXML scene before each test. */
    @BeforeEach
    void setUp() throws InterruptedException {
        Settings.getInstance().setDefaults();
        runOnFxThread(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/mycompany/irr00_group_project/settings.fxml"));
                loader.load();
                controller = loader.getController();
                scene = new Scene(loader.getRoot());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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

    /** Tests that the theme choice box is populated with all available themes after init. */
    @Test
    void initialize_themeChoiceBoxContainsAllThemes() {
        assertEquals(Theme.values().length,
            scene.lookup("#themeChoiceBox") instanceof javafx.scene.control.ChoiceBox<?> cb
                ? cb.getItems().size() : -1);
    }

    /** Tests that clicking Reset Defaults restores the default font size in Settings. */
    @Test
    void resetDefaults_restoresDefaultFontSize() throws InterruptedException {
        runOnFxThread(() -> {
            Slider slider = (Slider) scene.lookup("#fontSizeSlider");
            slider.setValue(28);
        });
        runOnFxThread(() -> {
            Button resetButton = (Button) scene.lookup("#resetDefaultsButton");
            resetButton.fire();
        });
        assertEquals(Constants.DEFAULT_FONT_SIZE, Settings.getInstance().getFontSize());
    }

    /** Tests that saving persists a new audiobook folder path to Settings. */
    @Test
    void saveSettings_updatesAudiobookFolderPath() throws IOException, InterruptedException {
        Path newFolder = Files.createTempDirectory("settingsTestFolder");
        try {
            runOnFxThread(() -> {
                TextField pathField = (TextField) scene.lookup("#filePathTextField");
                pathField.setText(newFolder.toString());
                Button saveButton = (Button) scene.lookup("#saveSettingsButton");
                saveButton.fire();
            });
            assertEquals(newFolder, Settings.getInstance().getAudiobookFolderPath());
        } finally {
            Files.delete(newFolder);
        }
    }

    /** Tests that clicking Exit Settings invokes the registered onBack callback. */
    @Test
    void exitSettings_invokesOnBackCallback() throws InterruptedException {
        AtomicBoolean called = new AtomicBoolean(false);
        controller.setOnBack(() -> called.set(true));
        runOnFxThread(() -> {
            Button exitButton = (Button) scene.lookup("#exitSettingsButton");
            exitButton.fire();
        });
        assertTrue(called.get());
    }
}
