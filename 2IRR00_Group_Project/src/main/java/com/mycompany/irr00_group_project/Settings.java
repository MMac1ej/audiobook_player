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
    //temporary? constants
    private static final int MIN_FONT_SIZE = 14; 
    private static final int MAX_FONT_SIZE = 28;
    private static final Theme DEFAULT_THEME = Theme.LIGHT;
    private static final Path DEFAULT_AUDIOBOOK_FOLDER = Paths.get(
        System.getProperty("user.home"), "Audiobooks");
    private static final int DEFAULT_FONT_SIZE = 16;

    private static Settings instance;

    private static final Path SETTINGS_FILE = Paths.get(
            System.getProperty("user.home"), ".audiobookplayer", "config.properties");

    private final Properties props;

    // Keys used in the properties file
    private static final String KEY_FOLDER = "audiobookFolderPath";
    private static final String KEY_FONT_SIZE = "fontSize";
    private static final String KEY_THEME = "theme";

    private Path audiobookFolderPath;
    private int fontSize;

    private Settings() {
        this.props = new Properties();
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
        if (!Files.exists(SETTINGS_FILE)) {
            // Set default values if config file doesn't exist
            setDefaults();
            return;
        }
        try (var in = Files.newInputStream(SETTINGS_FILE)) {
            props.load(in);
            setAudiobookFolderPathInternal(Paths.get(props.getProperty(
                KEY_FOLDER, DEFAULT_AUDIOBOOK_FOLDER.toString())));
            setFontSizeInternal(Integer.parseInt(props.getProperty(
                KEY_FONT_SIZE, String.valueOf(DEFAULT_FONT_SIZE))));
        } catch (Exception e) {
            // If there's an error reading the config, use defaults
            setDefaults();
        }
    }

    private void saveToDisk() {
        try {
            Files.createDirectories(SETTINGS_FILE.getParent());
            try (var out = Files.newOutputStream(SETTINGS_FILE)) {
                props.store(out, null);
            }
        } catch (IOException e) {
            // best-effort write approach
        }
    }

    /**
     * Resets all settings to their default values.
     */
    protected void setDefaults() {
        setFontSize(DEFAULT_FONT_SIZE);
        setTheme(DEFAULT_THEME);
        setAudiobookFolderPath(DEFAULT_AUDIOBOOK_FOLDER);
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
        if (fontSize < MIN_FONT_SIZE || fontSize > MAX_FONT_SIZE) {
            throw new IllegalArgumentException(
                "Font size must be between " + MIN_FONT_SIZE + " and " + MAX_FONT_SIZE);
        }
        setFontSizeInternal(fontSize);
    }

    private void setFontSizeInternal(int fontSize) {
        this.fontSize = fontSize;
        props.setProperty(KEY_FONT_SIZE, Integer.toString(fontSize));
        saveToDisk();
    }

    /**
     * Returns the theme for the application.
     * @return the theme
     */
    public Theme getTheme() {
        String themeStr = props.getProperty(KEY_THEME, "LIGHT");
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
        props.setProperty(KEY_THEME, theme.name());
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
    public void setAudiobookFolderPath(Path audiobookFolderPath) {
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
        props.setProperty(KEY_FOLDER, audiobookFolderPath.toString());
        saveToDisk();
    }
}
