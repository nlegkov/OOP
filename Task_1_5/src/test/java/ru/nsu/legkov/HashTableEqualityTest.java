package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class HashTableEqualityTest {
    @Test
    void sameEntriesInDifferentOrderAreEqual() {
        HashTable<String, Integer> first = new HashTable<>();
        HashTable<String, Integer> second = new HashTable<>();
        first.put("one", 1);
        first.put("two", 2);
        second.put("two", 2);
        second.put("one", 1);
        assertEquals(first, first);
        assertEquals(first, second);
        assertEquals(second, first);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void emptyTablesAreEqual() {
        assertEquals(new HashTable<>(), new HashTable<>());
    }

    @Test
    void differentValuesAreNotEqual() {
        HashTable<String, Integer> first = new HashTable<>();
        HashTable<String, Integer> second = new HashTable<>();
        first.put("one", 1);
        second.put("one", 2);
        assertNotEquals(first, second);
    }

    @Test
    void differentKeysAreNotEqual() {
        HashTable<String, Integer> first = new HashTable<>();
        HashTable<String, Integer> second = new HashTable<>();
        first.put("one", 1);
        second.put("two", 1);
        assertNotEquals(first, second);
    }

    @Test
    void differentSizesAreNotEqual() {
        HashTable<String, Integer> first = new HashTable<>();
        HashTable<String, Integer> second = new HashTable<>();
        first.put("one", 1);
        second.put("one", 1);
        second.put("two", 2);
        assertNotEquals(first, second);
    }

    @Test
    void nullOrUnrelatedObjectsAreNotEqual() {
        HashTable<String, Integer> table = new HashTable<>();
        assertNotEquals(null, table);
        assertNotEquals("not a table", table);
        assertFalse(table.equals(null));
        assertFalse(table.equals("not a table"));
    }

    @Test
    void nullValuesAreComparedCorrectly() {
        HashTable<String, Integer> first = new HashTable<>();
        HashTable<String, Integer> second = new HashTable<>();
        first.put("one", null);
        second.put("one", null);
        assertEquals(first, second);
        second.update("one", 100);
        assertNotEquals(first, second);
    }

    @Test
    void stringRepresentationContainsAllEntries() {
        HashTable<String, Integer> table = new HashTable<>();
        table.put("one", 1);
        table.put("two", 2);
        String printed = table.toString();
        assertTrue(printed.startsWith("{"));
        assertTrue(printed.endsWith("}"));
        assertTrue(printed.contains("one=1"));
        assertTrue(printed.contains("two=2"));
        assertTrue(printed.contains(", "));
    }
}
