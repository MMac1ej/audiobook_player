package com.mycompany.irr00_group_project;

import java.nio.file.Path;
import java.nio.file.Paths;

/** Class to hold constant values used across the application. 
 * @author Tymon Szczepański, 2330113
*/
public final class Constants {

    /**
     * Constants for the Settings view.
     */
    public static final int MIN_FONT_SIZE = 14; 
    public static final int MAX_FONT_SIZE = 28;
    public static final Theme DEFAULT_THEME = Theme.LIGHT;
    public static final int DEFAULT_FONT_SIZE = 16;
    public static final Path DEFAULT_AUDIOBOOK_FOLDER = Paths.get(
        System.getProperty("user.home"), "Audiobooks");
    public static final Path SETTINGS_FILE = Paths.get(
            System.getProperty("user.home"), ".audiobookplayer", "config.properties");
    public static final String SAVE_FAILED_MESSAGE = (
        "Error: Failed to save settings. Invalid folder path!");
    public static final String SAVE_SUCCESS_MESSAGE = "Settings saved successfully!";
    public static final String SAVE_FAIL_MESSAGE_STYLE = (
        "-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
    public static final String SAVE_SUCCESS_MESSAGE_STYLE = (
        "-fx-text-fill: #2e7d32; -fx-font-weight: bold;");
    public static final String RESET_SUCCESS_MESSAGE = (
        "Settings reset to defaults. Click 'Save Settings' to apply.");
    public static final int STATUS_MESSAGE_DURATION_SECONDS = 6;

    /** Keys used in the properties file. */
    public static final String KEY_FOLDER = "audiobookFolderPath";
    public static final String KEY_FONT_SIZE = "fontSize";
    public static final String KEY_THEME = "theme";

    /**
     * Constants for the application window.
     */
    public static final int MIN_WINDOW_WIDTH = 400;
    public static final int MIN_WINDOW_HEIGHT = 300;
    public static final int DEFAULT_WINDOW_WIDTH = 900;
    public static final int DEFAULT_WINDOW_HEIGHT = 650;

    private Constants() {
        // Private constructor to prevent instantiation
    }
}
