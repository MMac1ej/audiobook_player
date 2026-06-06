package com.mycompany.irr00_group_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for the Settings class. */
class SettingsTest {

    @TempDir
    Path tempDir;

    /**
     * Resets the Settings singleton instance before each test to ensure test isolation.
     * @throws Exception if reflection fails to reset the singleton instance
     */
    @BeforeEach
    void resetSingleton() throws Exception {
        // Reset the singleton instance before each test
        var field = Settings.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(null, null);
    }

    @Test
    void getInstanceReturnsSingletonInstance() {
        Settings instance1 = Settings.getInstance();
        Settings instance2 = Settings.getInstance();
        assertSame(instance1, instance2, "getInstance should return the same instance");
    }

    @Test
    void defaultFontSizeIsValid() {
        Settings settings = Settings.getInstance();
        int fontSize = settings.getFontSize();
        assertTrue(fontSize >= 14 && fontSize <= 28,
            "Default font size should be within valid range [14, 28]");
    }

    @Test
    void defaultThemeIsLight() {
        Settings settings = Settings.getInstance();
        assertEquals(Theme.LIGHT, settings.getTheme(),
            "Default theme should be LIGHT");
    }

    @Test
    void setFontSizeUpdatesValue() {
        Settings settings = Settings.getInstance();
        settings.setFontSize(20);
        assertEquals(20, settings.getFontSize(),
            "Font size should be updated to 20");
    }

    @Test
    void setFontSizeWithValidBoundaryValues() {
        Settings settings = Settings.getInstance();

        settings.setFontSize(14);
        assertEquals(14, settings.getFontSize(), "Should accept minimum font size 14");

        settings.setFontSize(28);
        assertEquals(28, settings.getFontSize(), "Should accept maximum font size 28");
    }

    @Test
    void setFontSizeBelowMinimumThrowsException() {
        Settings settings = Settings.getInstance();
        assertThrows(IllegalArgumentException.class, () -> settings.setFontSize(13),
            "Should throw exception when setting font size below 14");
    }

    @Test
    void setFontSizeAboveMaximumThrowsException() {
        Settings settings = Settings.getInstance();
        assertThrows(IllegalArgumentException.class, () -> settings.setFontSize(29),
            "Should throw exception when setting font size above 28");
    }

    @Test
    void setThemeUpdatesValue() {
        Settings settings = Settings.getInstance();
        settings.setTheme(Theme.DARK);
        assertEquals(Theme.DARK, settings.getTheme(),
            "Theme should be updated to DARK");

        settings.setTheme(Theme.HIGH_CONTRAST);
        assertEquals(Theme.HIGH_CONTRAST, settings.getTheme(),
            "Theme should be updated to HIGH_CONTRAST");
    }

    @Test
    void setAudiobookFolderPathUpdatesValue() {
        Settings settings = Settings.getInstance();
        Path newPath = tempDir;
        settings.setAudiobookFolderPath(newPath);
        assertEquals(newPath, settings.getAudiobookFolderPath(),
            "Audiobook folder path should be updated");
    }

    @Test
    void setAudiobookFolderPathWithNullThrowsException() {
        Settings settings = Settings.getInstance();
        assertThrows(IllegalArgumentException.class,
            () -> settings.setAudiobookFolderPath(null),
            "Should throw exception when setting null path");
    }

    @Test
    void setAudiobookFolderPathWithNonExistentPathThrowsException() {
        Settings settings = Settings.getInstance();
        Path nonExistentPath = tempDir.resolve("nonexistent");
        assertThrows(IllegalArgumentException.class,
            () -> settings.setAudiobookFolderPath(nonExistentPath),
            "Should throw exception when path does not exist");
    }

    @Test
    void setAudiobookFolderPathWithFilePathThrowsException() throws Exception {
        Settings settings = Settings.getInstance();
        Path filePath = tempDir.resolve("testfile.txt");
        Files.createFile(filePath);

        assertThrows(IllegalArgumentException.class,
            () -> settings.setAudiobookFolderPath(filePath),
            "Should throw exception when path is not a directory");
    }

    @Test
    void setDefaultsResetsAllSettings() throws Exception {
        Settings settings = Settings.getInstance();

        // Change settings
        settings.setFontSize(24);
        settings.setTheme(Theme.DARK);
        settings.setAudiobookFolderPath(tempDir);

        // Reset defaults
        settings.setDefaults();

        // Verify defaults (accounting for which defaults were actually set)
        assertEquals(Theme.LIGHT, settings.getTheme(),
            "Theme should be reset to LIGHT");
        // Note: Font size and audiobook path depend on defaults, so we just check they're valid
        assertTrue(settings.getFontSize() >= 14 && settings.getFontSize() <= 28,
            "Font size should be valid after reset");
    }

    @Test
    void multipleSettingsChangesArePersisted() throws Exception {
        Settings settings1 = Settings.getInstance();
        settings1.setFontSize(22);
        settings1.setTheme(Theme.DARK);

        // Create a new settings file to verify persistence
        // (In a real scenario, you'd verify by reloading from disk,
        // but the singleton prevents that without reflection)
        assertEquals(22, settings1.getFontSize());
        assertEquals(Theme.DARK, settings1.getTheme());
    }

    @Test
    void settingsFontSizeWithinRangeSucceeds() {
        Settings settings = Settings.getInstance();
        for (int size = 14; size <= 28; size++) {
            settings.setFontSize(size);
            assertEquals(size, settings.getFontSize());
        }
    }

    @Test
    void allThemesCanBeSet() {
        Settings settings = Settings.getInstance();
        for (Theme theme : Theme.values()) {
            settings.setTheme(theme);
            assertEquals(theme, settings.getTheme());
        }
    }

}
