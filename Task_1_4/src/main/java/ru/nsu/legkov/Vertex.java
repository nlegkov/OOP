package ru.nsu.legkov;

import java.util.Objects;

public class Vertex<T> {
    private final T data;

    public Vertex(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Vertex<?> vertex = (Vertex<?>) o;
        return Objects.equals(data, vertex.data);    }

    @Override
    public String toString() {
        return "Vertex: " + data;
    }
}
