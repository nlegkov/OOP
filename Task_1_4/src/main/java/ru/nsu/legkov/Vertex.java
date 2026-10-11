package ru.nsu.legkov;

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
        return data == vertex.data;
    }

    @Override
    public String toString() {
        return "Vertex: " + data;
    }
}
