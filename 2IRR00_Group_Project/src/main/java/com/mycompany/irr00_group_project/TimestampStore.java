package com.mycompany.irr00_group_project;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/** Persists play-order for episodes to a properties file on disk. */
public class TimestampStore extends PropertiesStore {

    private static final String COUNTER_KEY = "__play_counter";

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
     * Records that the given audiobook was just played by assigning it the next
     * sequential play-order counter value.
     *
     * @param key the audiobook identifier (typically its folder path as a string)
     */
    public void saveTimestamp(String key) {
        long counter = Long.parseLong(super.getProperty(COUNTER_KEY, "0")) + 1;
        super.setProperty(COUNTER_KEY, Long.toString(counter));
        super.setProperty(key, Long.toString(counter));
    }

    /**
     * Returns all audiobook keys sorted by play order, most recent first.
     *
     * @return sorted list of audiobook identifiers
     */
    public List<String> getRecentlyPlayedAudiobooks() {
        Set<String> audiobooks = super.listKeysAsSet();
        Map<String, Long> audiobooksWithOrder = new HashMap<>();

        for (String audiobook : audiobooks) {
            if (COUNTER_KEY.equals(audiobook)) {
                continue;
            }
            String order = super.getProperty(audiobook, "0");
            audiobooksWithOrder.put(audiobook, Long.valueOf(order));
        }

        return audiobooksWithOrder.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

}
