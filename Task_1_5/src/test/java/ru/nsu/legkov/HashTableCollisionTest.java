package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class HashTableCollisionTest {
    private HashTable<CollisionKey, Integer> populated() {
        HashTable<CollisionKey, Integer> table = new HashTable<>();
        table.put(new CollisionKey(1), 10);
        table.put(new CollisionKey(2), 20);
        table.put(new CollisionKey(3), 30);
        return table;
    }

    @Test
    void collidingKeysRemainDistinct() {
        HashTable<CollisionKey, Integer> table = populated();
        assertEquals(10, table.get(new CollisionKey(1)));
        assertEquals(20, table.get(new CollisionKey(2)));
        assertEquals(30, table.get(new CollisionKey(3)));
        assertFalse(table.put(new CollisionKey(2), 200));
        assertEquals(20, table.get(new CollisionKey(2)));
        assertNull(table.get(new CollisionKey(99)));
    }

    @Test
    void removeHeadOfChain() {
        HashTable<CollisionKey, Integer> table = populated();
        assertTrue(table.remove(new CollisionKey(1)));
        assertNull(table.get(new CollisionKey(1)));
        assertEquals(20, table.get(new CollisionKey(2)));
        assertEquals(30, table.get(new CollisionKey(3)));
    }

    @Test
    void removeMiddleOfChain() {
        HashTable<CollisionKey, Integer> table = populated();
        assertTrue(table.remove(new CollisionKey(2)));
        assertEquals(10, table.get(new CollisionKey(1)));
        assertNull(table.get(new CollisionKey(2)));
        assertEquals(30, table.get(new CollisionKey(3)));
    }

    @Test
    void removeTailOfChain() {
        HashTable<CollisionKey, Integer> table = populated();
        assertTrue(table.remove(new CollisionKey(3)));
        assertEquals(10, table.get(new CollisionKey(1)));
        assertEquals(20, table.get(new CollisionKey(2)));
        assertFalse(table.remove(new CollisionKey(3)));
    }

    @Test
    void updateWithinChain() {
        HashTable<CollisionKey, Integer> table = populated();
        assertTrue(table.update(new CollisionKey(3), 300));
        assertEquals(300, table.get(new CollisionKey(3)));
        assertEquals(10, table.get(new CollisionKey(1)));
        assertFalse(table.update(new CollisionKey(55), 55));
    }
}
