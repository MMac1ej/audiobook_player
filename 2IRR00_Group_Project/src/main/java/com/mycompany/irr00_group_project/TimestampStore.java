package com.mycompany.irr00_group_project;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Persists last-played timestamps for audiobooks to a properties file on disk. */
public class TimestampStore extends PropertiesStore {

    /** Creates a TimestampStore using the default file location. */
    public TimestampStore() {
        this(Constants.DEFAULT_TIMESTAMP_STORE_PATH);
    }

    /**
     * Creates a TimestampStore that reads and writes to the given file.
     *
     * @param storeFile the path to the properties file
     */
    public TimestampStore(Path storeFile) {
        super(storeFile);
    }

    /**
     * Saves the current time as the last-played timestamp for the given audiobook.
     *
     * @param key the audiobook identifier (typically its folder path as a string)
     */
    public void saveTimestamp(String key) {
        super.setProperty(key, Long.toString(System.currentTimeMillis()));
    }

    /**
     * Returns all audiobook keys sorted by last-played time, most recent first.
     *
     * @return sorted list of audiobook identifiers
     */
    public List<String> getRecentlyPlayed() {
        Set<String> audiobooks = super.listKeysAsSet();
        Map<String, Long> audiobooksWithTimestamps = new HashMap<>();

        for (String audiobook : audiobooks) {
            String timestamp = super.getProperty(audiobook, "0");
            audiobooksWithTimestamps.put(audiobook, Long.valueOf(timestamp));
        }

        return audiobooksWithTimestamps.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
