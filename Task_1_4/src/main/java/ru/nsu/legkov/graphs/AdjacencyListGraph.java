package ru.nsu.legkov.graphs;

import ru.nsu.legkov.Vertex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjacencyListGraph implements Graph{
    private final Map<Vertex<?>, List<Vertex<?>>> adjList = new HashMap<>();

    @Override
    public boolean addVertex(Vertex<?> vertex) {
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
    public boolean removeVertex(Vertex<?> vertex) {
        if (vertex == null) {
            return false;
        }
        if (!adjList.containsKey(vertex)) {
            return false;
        }

        adjList.remove(vertex);

        for (List<Vertex<?>> neighbors : adjList.values()) {
            neighbors.removeIf(v -> v.equals(vertex));
        }

        return true;
    }

    @Override
    public boolean addEdge(Vertex<?> from, Vertex<?> to) {
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
    public boolean removeEdge(Vertex<?> from, Vertex<?> to) {
        if (from == null || to == null) {
            return false;
        }
        if (!adjList.containsKey(from) || !adjList.containsKey(to)) {
            return false;
        }
        return adjList.get(from).remove(to);
    }

    @Override
    public List<Vertex<?>> getNeighbors(Vertex<?> vertex) {
        if (vertex == null) return List.of();

        List<Vertex<?>> neighbors = adjList.get(vertex);
        return neighbors == null ? List.of() : new ArrayList<>(neighbors);
    }
}
