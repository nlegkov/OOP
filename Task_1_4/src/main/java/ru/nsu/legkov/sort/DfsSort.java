package ru.nsu.legkov.sort;

import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.graphs.Graph;

import java.util.*;

public class DfsSort<T> implements TopologicalSort<T> {

    @Override
    public List<Vertex<T>> sort(Graph<T> graph) {
        Map<Vertex<T>, Boolean> finished = new HashMap<>();
        List<Vertex<T>> result = new ArrayList<>();

        for (Vertex<T> vertex : graph.getVertices()) {
            visit(graph, vertex, finished, result);
        }

        Collections.reverse(result);
        return result;
    }

    private void visit(
            Graph<T> graph,
            Vertex<T> vertex,
            Map<Vertex<T>, Boolean> finished,
            List<Vertex<T>> result
    ) {
        Boolean state = finished.get(vertex);

        if (state != null) {
            if (!state) {
                throw new IllegalStateException("В графе есть цикл");
            }
            return;
        }

        finished.put(vertex, false);

        for (Vertex<T> neighbor : graph.getNeighbors(vertex)) {
            visit(graph, neighbor, finished, result);
        }

        finished.put(vertex, true);
        result.add(vertex);
    }
}