package com.mycompany.irr00_group_project;

import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

class TimestampStoreTest {

    @TempDir
    Path tempDir;

    @Test
    void getRecentlyPlayedIsEmptyOnNewStore() {
        TimestampStore store = new TimestampStore(tempDir.resolve("timestamp.properties"));
        assertTrue(store.getRecentlyPlayedAudiobooks().isEmpty());
    }

    @Test
    void savedKeyAppearsInRecentlyPlayed() {
        TimestampStore store = new TimestampStore(tempDir.resolve("timestamp.properties"));
        store.saveTimestamp("/audiobooks/Hobbit");
        assertTrue(store.getRecentlyPlayedAudiobooks().contains("/audiobooks/Hobbit"));
    }

    @Test
    void recentlyPlayedSortedMostRecentFirst() {
        TimestampStore store = new TimestampStore(tempDir.resolve("timestamp.properties"));
        store.saveTimestamp("/audiobooks/Dune");
        store.saveTimestamp("/audiobooks/Hobbit");

        List<String> result = store.getRecentlyPlayedAudiobooks();
        assertEquals("/audiobooks/Hobbit", result.get(0));
        assertEquals("/audiobooks/Dune", result.get(1));
    }

    @Test
    void savingTimestampAgainUpdatesOrder() {
        TimestampStore store = new TimestampStore(tempDir.resolve("timestamp.properties"));
        store.saveTimestamp("/audiobooks/Hobbit");
        store.saveTimestamp("/audiobooks/Dune");
        store.saveTimestamp("/audiobooks/Hobbit");

        List<String> result = store.getRecentlyPlayedAudiobooks();
        assertEquals("/audiobooks/Hobbit", result.get(0));
        assertEquals("/audiobooks/Dune", result.get(1));
    }

    @Test
    void newInstanceOnSameFileReadsPersistedData() {
        Path file = tempDir.resolve("timestamp.properties");
        new TimestampStore(file).saveTimestamp("/audiobooks/Hobbit");
        List<String> episodes = new TimestampStore(file).getRecentlyPlayedAudiobooks();
        assertTrue(episodes.contains("/audiobooks/Hobbit"));
    }

}
