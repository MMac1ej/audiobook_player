package com.mycompany.irr00_group_project;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

class ProgressStoreTest {

    @TempDir
    Path tempDir;

    @Test
    void saveAndLoadReturnsSameValue() {
        ProgressStore store = new ProgressStore(tempDir.resolve("progress.properties"));
        store.saveProgress("ep1", 1800);
        assertEquals(1800, store.loadProgress("ep1"));
    }

    @Test
    void loadUnknownKeyReturnsZero() {
        ProgressStore store = new ProgressStore(tempDir.resolve("progress.properties"));
        assertEquals(0, store.loadProgress("unknown"));
    }

    @Test
    void saveZeroAndLoadReturnsZero() {
        ProgressStore store = new ProgressStore(tempDir.resolve("progress.properties"));
        store.saveProgress("ep1", 0);
        assertEquals(0, store.loadProgress("ep1"));
    }

    @Test
    void clearProgressMakesLoadReturnZero() {
        ProgressStore store = new ProgressStore(tempDir.resolve("progress.properties"));
        store.saveProgress("ep1", 500);
        store.clearProgress("ep1");
        assertEquals(0, store.loadProgress("ep1"));
    }

    @Test
    void newInstanceOnSameFileReadsPersistedData() {
        Path file = tempDir.resolve("progress.properties");
        new ProgressStore(file).saveProgress("ep1", 3600);
        assertEquals(3600, new ProgressStore(file).loadProgress("ep1"));
    }

}
