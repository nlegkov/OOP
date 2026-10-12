package ru.nsu.legkov.graphs;

import ru.nsu.legkov.Vertex;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

public interface Graph<T> {
    boolean addVertex(Vertex<T> vertex);
    boolean removeVertex(Vertex<T> vertex);

    boolean addEdge(Vertex<T> from, Vertex<T> to);
    boolean removeEdge(Vertex<T> from, Vertex<T> to);

    List<Vertex<T>> getVertices();
    List<Vertex<T>> getNeighbors(Vertex<T> vertex);

    void loadFromFile(
            Path path,
            Function<String, T> parser
    ) throws IOException;

    void saveToFile(
            Path path,
            Function<T, String> formatter
    ) throws IOException;
}