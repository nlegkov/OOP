package ru.nsu.legkov.graphs;

import ru.nsu.legkov.Vertex;

import java.io.IOException;
import java.util.List;

public interface Graph {
    boolean addVertex(Vertex<?> vertex);
    boolean removeVertex(Vertex<?> vertex);
    boolean addEdge(Vertex<?> from, Vertex<?> to);
    boolean removeEdge(Vertex<?> from, Vertex<?> to);
    List<Vertex<?>> getNeighbors(Vertex<?> vertex);

    boolean saveToFile(String filename);
    boolean loadFromFile(String filename) throws IOException;


}