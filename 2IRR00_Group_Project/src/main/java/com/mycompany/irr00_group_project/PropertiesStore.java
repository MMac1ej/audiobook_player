package com.mycompany.irr00_group_project;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Set;

/** Abstract base class for property-file-backed stores. */
public abstract class PropertiesStore {

    private final Path storeFile;
    private final Properties props;

    /**
     * Creates a PropertiesStore that reads and writes to the given file.
     *
     * @param storeFile the path to the properties file
     */
    public PropertiesStore(Path storeFile) {
        this.storeFile = storeFile;
        this.props = new Properties();
        loadFromDisk();
    }

    /** Loads properties from disk, silently ignoring missing or unreadable files. */
    protected void loadFromDisk() {
        if (!Files.exists(storeFile)) {
            return;
        }
        try (InputStream in = Files.newInputStream(storeFile)) {
            props.load(in);
        } catch (IOException e) {
            // file unreadable — start with empty store
        }
    }

    /** Writes current properties to disk, creating parent directories as needed. */
    protected void saveToDisk() {
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
     * Saves a key-value pair and persists to disk.
     *
     * @param key the property key
     * @param value the property value
     */
    protected void setProperty(String key, String value) {
        props.setProperty(key, value);
        saveToDisk();
    }

    /**
     * Returns the value for the given key, or null if not found.
     *
     * @param key the property key
     * @return the value, or null
     */
    protected String getProperty(String key) {
        return props.getProperty(key);
    }

    /**
     * Removes the given key and persists to disk.
     *
     * @param key the property key to remove
     */
    protected void removeProperty(String key) {
        props.remove(key);
        saveToDisk();
    }

    /** Returns all property keys as a set of strings. */
    protected Set<String> keySet() {
        return props.stringPropertyNames();
    }
}
