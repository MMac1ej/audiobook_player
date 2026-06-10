package com.mycompany.irr00_group_project;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.Set;

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

    protected void setProperty(String key, String value) {
        props.setProperty(key, value);
        saveToDisk();
    }

    protected String getProperty(String key) {
        return props.getProperty(key);
    }

    protected void removeProperty(String key) {
        props.remove(key);
        saveToDisk();
    }

    protected Set<String> keySet(){
        return props.stringPropertyNames();
    }
}
