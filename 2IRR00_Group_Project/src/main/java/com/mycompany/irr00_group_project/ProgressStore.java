package com.mycompany.irr00_group_project;

import java.nio.file.Path;

/** Persists episode playback progress to a properties file on disk.
 *  NEVER create more than one instance of ProgressStore!
*/
public class ProgressStore extends PropertiesStore {

    private static final Path DEFAULT_FILE = Constants.DEFAULT_PROGRESS_STORE_PATH;

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
        super(storeFile);
    }

    /**
     * Saves the playback progress for the given key.
     *
     * @param key the episode identifier (typically its file path as a string)
     * @param seconds the progress in seconds
     */
    public void saveProgress(String key, long seconds) {
        super.setProperty(key, Long.toString(seconds));
    }

    /**
     * Returns the saved playback progress for the given key, or 0 if not found.
     *
     * @param key the episode identifier
     * @return the saved progress in seconds, or 0 if not found
     */
    public long loadProgress(String key) {
        String value = super.getProperty(key, null);
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
        super.removeProperty(key);
    }

}
