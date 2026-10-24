package ru.nsu.legkov;

import java.util.Objects;

final class CollisionKey {
    private final int id;

    CollisionKey(int id) { this.id = id; }

    @Override
    public int hashCode() { return 42; }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CollisionKey other && id == other.id;
    }
}
