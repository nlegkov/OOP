package ru.nsu.legkov.graphs;

import ru.nsu.legkov.Vertex;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public interface Graph<T> {
    boolean addVertex(Vertex<T> vertex);
    boolean removeVertex(Vertex<T> vertex);
    boolean addEdge(Vertex<T> from, Vertex<T> to);
    boolean removeEdge(Vertex<T> from, Vertex<T> to);
    List<Vertex<T>> getNeighbors(Vertex<T> vertex);

    boolean saveToFile(String filename);
    boolean loadFromFile(String filename) throws IOException;

    public static <T> T parseValue(
            String text,
            Function<String, T> parser
    ) {
        return parser.apply(text);
    }
}