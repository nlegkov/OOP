package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class HashTableResizeTest {
    @Test
    void growsAndPreservesAllKeysIncludingHighHashBits() {
        HashTable<Integer, String> table = new HashTable<>();
        assertTrue(table.put(65536, "special"));
        for (int i = 0; i < 1000; i++) {
            assertTrue(table.put(i, "v" + i));
        }
        assertEquals("special", table.get(65536));
        for (int i = 0; i < 1000; i++) {
            assertEquals("v" + i, table.get(i));
        }
    }

    @Test
    void shrinksAndPreservesRemainingEntries() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int i = 0; i < 256; i++) {
            table.put(i, i * 2);
        }
        for (int i = 0; i < 250; i++) {
            assertTrue(table.remove(i));
        }
        for (int i = 250; i < 256; i++) {
            assertEquals(i * 2, table.get(i));
        }
        assertFalse(table.remove(12345));
    }

    @Test
    void repeatedGrowShrinkGrowKeepsData() {
        HashTable<Integer, Integer> table = new HashTable<>();
        for (int i = 0; i < 300; i++) {
            table.put(i, i);
        }
        for (int i = 0; i < 280; i++) {
            table.remove(i);
        }
        for (int i = 300; i < 600; i++) {
            table.put(i, i);
        }
        for (int i = 280; i < 600; i++) {
            assertEquals(i, table.get(i));
        }
        int count = 0;
        for (var ignored : table) {
            count++;
        }
        assertEquals(320, count);
    }
}
