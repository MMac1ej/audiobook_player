package com.mycompany.irr00_group_project;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/** Persists episode playback progress to a properties file on disk.
 *  NEVER create more than one instance of ProgressStore!
*/
public class ProgressStore {

    private static final Path DEFAULT_FILE = Paths.get(
            System.getProperty("user.home"), ".audiobookplayer", "progress.properties");

    private final Path storeFile;
    private final Properties props;

    /** Creates a ProgressStore using the default file location. */
    public ProgressStore() {
        this(DEFAULT_FILE);
    }

    /**
     * Creates a ProgressStore that reads and writes to the given file.
     *
     * @param storeFile the path to the properties file
     */
    public ProgressStore(Path storeFile) {
        this.storeFile = storeFile;
        this.props = new Properties();
        loadFromDisk();
    }

    private void loadFromDisk() {
        if (!Files.exists(storeFile)) {
            return;
        }
        try (InputStream in = Files.newInputStream(storeFile)) {
            props.load(in);
        } catch (IOException e) {
            // file unreadable — start with empty store
        }
    }

    private void saveToDisk() {
        try {
            Files.createDirectories(storeFile.getParent());
            try (OutputStream out = Files.newOutputStream(storeFile)) {
                props.store(out, null);
            }
        } catch (IOException e) {
            // best-effort write approach
        }
    }

    /**
     * Saves the playback progress for the given key.
     *
     * @param key the episode identifier (typically its file path as a string)
     * @param seconds the progress in seconds
     */
    public void saveProgress(String key, long seconds) {
        props.setProperty(key, Long.toString(seconds));
        saveToDisk();
    }

    /**
     * Returns the saved playback progress for the given key, or 0 if not found.
     *
     * @param key the episode identifier
     * @return the saved progress in seconds, or 0 if not found
     */
    public long loadProgress(String key) {
        String value = props.getProperty(key);
        if (value == null) {
            return 0;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Removes the saved progress for the given key.
     *
     * @param key the episode identifier
     */
    public void clearProgress(String key) {
        props.remove(key);
        saveToDisk();
    }

}
