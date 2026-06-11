package com.mycompany.irr00_group_project;

import java.nio.file.Path;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

class PropertiesStoreTest {

    @TempDir
    Path tempDir;

    private static class TestStore extends PropertiesStore {
        TestStore(Path storeFile) {
            super(storeFile);
        }

        public void set(String key, String value) {
            setProperty(key, value);
        }

        public String get(String key) {
            return getProperty(key, null);
        }

        public void remove(String key) {
            removeProperty(key);
        }

        public Set<String> keys() {
            return listKeysAsSet();
        }
    }

    @Test
    void setAndGetReturnsSameValue() {
        TestStore store = new TestStore(tempDir.resolve("test.properties"));
        store.set("key1", "value1");
        assertEquals("value1", store.get("key1"));
    }

    @Test
    void getUnknownKeyReturnsNull() {
        TestStore store = new TestStore(tempDir.resolve("test.properties"));
        assertNull(store.get("unknown"));
    }

    @Test
    void removeDeletesKey() {
        TestStore store = new TestStore(tempDir.resolve("test.properties"));
        store.set("key1", "value1");
        store.remove("key1");
        assertNull(store.get("key1"));
    }

    @Test
    void listKeysAsSetReturnsAllKeys() {
        TestStore store = new TestStore(tempDir.resolve("test.properties"));
        store.set("key1", "value1");
        store.set("key2", "value2");
        assertEquals(Set.of("key1", "key2"), store.keys());
    }

    @Test
    void listKeysAsSetIsEmptyOnNewStore() {
        TestStore store = new TestStore(tempDir.resolve("test.properties"));
        assertTrue(store.keys().isEmpty());
    }

    @Test
    void newInstanceOnSameFileReadsPersistedData() {
        Path file = tempDir.resolve("test.properties");
        new TestStore(file).set("key1", "value1");
        assertEquals("value1", new TestStore(file).get("key1"));
    }

    @Test
    void removeNonExistentKeyDoesNotThrow() {
        TestStore store = new TestStore(tempDir.resolve("test.properties"));
        assertDoesNotThrow(() -> store.remove("nonexistent"));
    }

}
