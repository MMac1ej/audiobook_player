package com.mycompany.irr00_group_project;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Persists last-played timestamps for episodes to a properties file on disk. */
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
     * Saves the current time as the last-played timestamp for the given episode.
     *
     * @param key the episode identifier (typically its file path as a string)
     */
    public void saveTimestamp(String key) {
        super.setProperty(key, Long.toString(System.currentTimeMillis()));
    }

    /**
     * Returns all episode keys sorted by last-played time, most recent first.
     *
     * @return sorted list of episode identifiers
     */
    public List<String> getRecentlyPlayedEpisodes() {
        Set<String> episodes = super.listKeysAsSet();
        Map<String, Long> episodesWithTimestamps = new HashMap<>();

        for (String episode : episodes) {
            String timestamp = super.getProperty(episode, "0");
            episodesWithTimestamps.put(episode, Long.valueOf(timestamp));
        }

        return episodesWithTimestamps.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
