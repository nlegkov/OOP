package ru.nsu.legkov.graphs;

import java.util.List;
import ru.nsu.legkov.Vertex;

/** Shared factories for graph tests. */
final class GraphTestSupport {
    private GraphTestSupport() {
    }

    static List<Graph<String>> graphs() {
        return List.of(
                new AdjacencyListGraph<>(),
                new AdjacencyMatrixGraph<>(),
                new IncidenceMatrixGraph<>());
    }

    static Vertex<String> vertex(int id) {
        return new Vertex<>(id, "V" + id);
    }
}
