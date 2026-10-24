package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.jupiter.api.Test;

class HashTableIteratorTest {
    @Test
    void iteratesEmptyTable() {
        HashTable<String, Integer> table = new HashTable<>();
        Iterator<Map.Entry<String, Integer>> it = table.iterator();
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iteratesEntriesAcrossBuckets() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);
        Set<String> seen = new HashSet<>();
        for (Map.Entry<String, Integer> entry : table) {
            seen.add(entry.getKey() + "=" + entry.getValue());
        }
        assertEquals(Set.of("one=1", "two=2", "three=3"), seen);
    }

    @Test
    void iteratesThroughCollisionChains() {
        HashTable<CollisionKey, Integer> table = new HashTable<>();
        for (int i = 0; i < 8; i++) {
            table.put(new CollisionKey(i), i);
        }
        Set<Integer> values = new HashSet<>();
        for (var entry : table) {
            values.add(entry.getValue());
        }
        assertEquals(Set.of(0, 1, 2, 3, 4, 5, 6, 7), values);
    }

    @Test
    void throwsAfterStructuralInsertion() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        Iterator<Map.Entry<String, Integer>> it = table.iterator();
        table.put("two", 2);
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void throwsAfterStructuralRemoval() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        Iterator<Map.Entry<String, Integer>> it = table.iterator();
        table.remove("one");
        assertThrows(ConcurrentModificationException.class, it::next);
    }

    @Test
    void updatingValueDoesNotInvalidateIterator() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        Iterator<Map.Entry<String, Integer>> it = table.iterator();
        assertTrue(table.update("one", 2));
        assertDoesNotThrow(it::next);
    }

    @Test
    void unsuccessfulInsertDoesNotInvalidateIterator() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        Iterator<Map.Entry<String, Integer>> it = table.iterator();
        assertFalse(table.put("one", 2));
        assertDoesNotThrow(it::next);
    }

    @Test
    void nextThrowsWhenIterationExhausted() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        Iterator<Map.Entry<String, Integer>> it = table.iterator();
        assertTrue(it.hasNext());
        assertEquals("one", it.next().getKey());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, it::next);
    }
}
