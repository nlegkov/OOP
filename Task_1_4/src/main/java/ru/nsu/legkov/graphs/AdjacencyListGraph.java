package ru.nsu.legkov.graphs;

import ru.nsu.legkov.Edge;
import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.io.GraphFileIO;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class AdjacencyListGraph<T> implements Graph<T> {
    private final Map<Vertex<T>, List<Vertex<T>>> adjList = new HashMap<>();

    @Override
    public boolean addVertex(Vertex<T> vertex) {
        if (vertex == null) {
            return false;
        }
        if (adjList.containsKey(vertex)) {
            return false;
        }
        adjList.put(vertex, new ArrayList<>());
        return true;
    }

    @Override
    public boolean removeVertex(Vertex<T> vertex) {
        if (vertex == null) {
            return false;
        }
        if (!adjList.containsKey(vertex)) {
            return false;
        }

        adjList.remove(vertex);

        for (List<Vertex<T>> neighbors : adjList.values()) {
            neighbors.removeIf(v -> v.equals(vertex));
        }

        return true;
    }

    @Override
    public boolean addEdge(Vertex<T> from, Vertex<T> to) {
        if (from == null || to == null) {
            return false;
        }
        if (!adjList.containsKey(from) || !adjList.containsKey(to)) {
            return false;
        }

        if (adjList.get(from).contains(to)) {
            return false;
        }

        adjList.get(from).add(to);
        return true;
    }

    @Override
    public boolean removeEdge(Vertex<T> from, Vertex<T> to) {
        if (from == null || to == null) {
            return false;
        }
        if (!adjList.containsKey(from) || !adjList.containsKey(to)) {
            return false;
        }
        return adjList.get(from).remove(to);
    }

    @Override
    public List<Vertex<T>> getVertices() {
        List<Vertex<T>> vertexList = new ArrayList<>();

        for (Map.Entry<Vertex<T>, List<Vertex<T>>> entry : adjList.entrySet()) {
            Vertex<T> value = entry.getKey();
            vertexList.add(value);
        }

        return vertexList;
    }

    @Override
    public List<Edge<T>> getEdge() {
        List<Edge<T>> edgeList = new ArrayList<>();

        for (Map.Entry<Vertex<T>, List<Vertex<T>>> entry : adjList.entrySet()) {
            Vertex<T> value = entry.getKey();
            List<Vertex<T>> vertexList = entry.getValue();

            for (int i = 0; i <= vertexList.size(); i++) {
                Edge<T> edge = new Edge<T>(value, vertexList.get(i));
                edgeList.add(edge);
            }
        }

        return edgeList;
    }

    @Override
    public List<Vertex<T>> getNeighbors(Vertex<T> vertex) {
        if (vertex == null) return List.of();

        List<Vertex<T>> neighbors = adjList.get(vertex);
        return neighbors == null ? List.of() : new ArrayList<>(neighbors);
    }

    @Override
    public void loadFromFile(Path path, Function<String, T> parser) throws IOException {
        GraphFileIO.load(this, path, parser);
    }

    @Override
    public void saveToFile(Path path, Function<T, String> formatter) throws IOException {
        GraphFileIO.save(this, path, formatter);
    }
}
