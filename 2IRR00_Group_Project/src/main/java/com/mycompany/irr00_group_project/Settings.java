package com.mycompany.irr00_group_project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/** Singleton class to hold user settings for the audiobook player
 * and store them on disk. 
 * @author Tymon Szczepański, 2330113
 */
public class Settings {

    private static Settings instance;

    private final Properties properties;
    private Path audiobookFolderPath;
    private int fontSize;

    private Settings() {
        this.properties = new Properties();
        loadFromDisk();
    }

    /** Returns the singleton instance of Settings. */
    public static synchronized Settings getInstance() {
        if (instance == null) {
            instance = new Settings();
        }
        return instance;
    }

    private void loadFromDisk() {
        // Set default values if config file doesn't exist
        if (!Files.exists(Constants.SETTINGS_FILE)) {
            setDefaults();
            return;
        }
        // Load settings from config file
        try (var in = Files.newInputStream(Constants.SETTINGS_FILE)) {
            properties.load(in);
            setAudiobookFolderPathInternal(Paths.get(properties.getProperty(
                Constants.KEY_FOLDER, Constants.DEFAULT_AUDIOBOOK_FOLDER.toString())));
            setFontSizeInternal(Integer.parseInt(properties.getProperty(
                Constants.KEY_FONT_SIZE, String.valueOf(Constants.DEFAULT_FONT_SIZE))));
        } catch (Exception e) {
            // If there's an error reading the config, use defaults
            setDefaults();
        }
    }

    private void saveToDisk() {
        try {
            Files.createDirectories(Constants.SETTINGS_FILE.getParent());
            try (var out = Files.newOutputStream(Constants.SETTINGS_FILE)) {
                properties.store(out, null);
            }
        } catch (IOException e) {
            // best-effort write approach
        }
    }

    /**
     * Resets all settings to their default values.
     */
    public void setDefaults() {
        setFontSizeInternal(Constants.DEFAULT_FONT_SIZE);
        setTheme(Constants.DEFAULT_THEME);
        setAudiobookFolderPathInternal(Constants.DEFAULT_AUDIOBOOK_FOLDER);
    }
    
    // -----Getter and setter methods for each setting-----

    /**
     * Returns the font size for the application.
     * @return the font size
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Sets the font size for the application.
     * @param fontSize the font size to set
     * @throws IllegalArgumentException if fontSize is out of range
     */
    public void setFontSize(int fontSize) throws IllegalArgumentException {
        if (fontSize < Constants.MIN_FONT_SIZE || fontSize > Constants.MAX_FONT_SIZE) {
            throw new IllegalArgumentException(
                "Font size must be between " + Constants.MIN_FONT_SIZE 
                + " and " + Constants.MAX_FONT_SIZE);
        }
        setFontSizeInternal(fontSize);
    }

    private void setFontSizeInternal(int fontSize) {
        this.fontSize = fontSize;
        properties.setProperty(Constants.KEY_FONT_SIZE, Integer.toString(fontSize));
        saveToDisk();
    }

    /**
     * Returns the theme for the application.
     * @return the theme
     */
    public Theme getTheme() {
        String themeStr = properties.getProperty(
            Constants.KEY_THEME, Constants.DEFAULT_THEME.name());
        try {
            return Theme.valueOf(themeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid theme in config: " + themeStr + ", defaulting to LIGHT");
            return Theme.LIGHT;
        }
    }

    /**
     * Sets the theme for the application.
     * @param theme the theme to set
     */
    public void setTheme(Theme theme) {
        properties.setProperty(Constants.KEY_THEME, theme.name());
        saveToDisk();
    }

    /**
     * Returns the path to the folder containing audiobooks.
     * @return the path to the audiobook folder
     */
    public Path getAudiobookFolderPath() {
        return audiobookFolderPath;
    }

    /**
     * Sets the path to the folder containing audiobooks.
     * @param audiobookFolderPath the path to the audiobook folder
     * @throws IllegalArgumentException if the path is null or invalid
     */
    public void setAudiobookFolderPath(Path audiobookFolderPath) throws IllegalArgumentException {
        //check valid path? (exists, is directory, etc.)
        if (audiobookFolderPath == null) {
            throw new IllegalArgumentException("Audiobook folder path cannot be null");
        }
        if (!Files.exists(audiobookFolderPath)) {
            throw new IllegalArgumentException(
                "Specified path does not exist: " + audiobookFolderPath);
        }
        if (!Files.isDirectory(audiobookFolderPath)) {
            throw new IllegalArgumentException(
                "Specified path is not a directory: " + audiobookFolderPath);
        }
        setAudiobookFolderPathInternal(audiobookFolderPath);
    }

    private void setAudiobookFolderPathInternal(Path audiobookFolderPath) {
        this.audiobookFolderPath = audiobookFolderPath;
        properties.setProperty(Constants.KEY_FOLDER, audiobookFolderPath.toString());
        saveToDisk();
    }
}
