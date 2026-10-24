package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class HashTableBasicTest {
    @Test
    void emptyTable() {
        HashTable<String, Integer> table = new HashTable<>();
        assertNull(table.get("missing"));
        assertFalse(table.remove("missing"));
        assertFalse(table.update("missing", 1));
        assertEquals("{}", table.toString());
    }

    @Test
    void insertAndFind() {
        HashTable<String, Integer> table = new HashTable<>();
        assertTrue(table.put("one", 1));
        assertTrue(table.put("two", 2));
        assertEquals(1, table.get("one"));
        assertEquals(2, table.get("two"));
        assertNull(table.get("three"));
    }

    @Test
    void duplicateKeyDoesNotOverwrite() {
        HashTable<String, Integer> table = new HashTable<>();
        assertTrue(table.put(new String("same"), 1));
        assertFalse(table.put(new String("same"), 999));
        assertEquals(1, table.get("same"));
    }

    @Test
    void updateExistingAndMissing() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        assertTrue(table.update("one", 7));
        assertEquals(7, table.get("one"));
        assertFalse(table.update("unknown", 5));
    }

    @Test
    void removeExistingAndMissing() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        assertTrue(table.remove("one"));
        assertNull(table.get("one"));
        assertFalse(table.remove("one"));
        assertTrue(table.put("one", 2));
        assertEquals(2, table.get("one"));
    }

    @Test
    void permitsNullValues() {
        HashTable<String, Integer> table = new HashTable<>();
        assertTrue(table.put("one", null));
        assertNull(table.get("one"));
        assertFalse(table.put("one", 5));
        assertTrue(table.update("one", 5));
        assertEquals(5, table.get("one"));
    }

    @Test
    void nullKeysAreRejectedByCurrentImplementation() {
        HashTable<String, Integer> table = new HashTable<>();
        assertThrows(NullPointerException.class, () -> table.put(null, 1));
    }
}
