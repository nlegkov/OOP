package ru.nsu.legkov.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import ru.nsu.legkov.Edge;
import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.sort.DfsSort;
import ru.nsu.legkov.sort.KahnSort;
import ru.nsu.legkov.sort.TopologicalSort;

/** Tests for the DFS and Kahn topological sorting algorithms. */
class TopologicalSortTest {
    @Test
    void dfsAndKahnProduceValidTopologicalOrder() {
        for (Graph<String> graph : GraphTestSupport.graphs()) {
            Vertex<String> a = GraphTestSupport.vertex(1);
            Vertex<String> b = GraphTestSupport.vertex(2);
            Vertex<String> c = GraphTestSupport.vertex(3);
            Vertex<String> d = GraphTestSupport.vertex(4);
            Vertex<String> isolated = GraphTestSupport.vertex(5);
            for (Vertex<String> v : List.of(a, b, c, d, isolated)) {
                graph.addVertex(v);
            }
            graph.addEdge(a, c);
            graph.addEdge(b, c);
            graph.addEdge(c, d);
            graph.addEdge(a, d);
            for (TopologicalSort<String> sorter :
                    List.of(new DfsSort<String>(), new KahnSort<String>())) {
                List<Vertex<String>> sorted = sorter.sort(graph);
                assertEquals(5, sorted.size());
                assertEquals(new HashSet<>(graph.getVertices()), new HashSet<>(sorted));
                Map<Vertex<String>, Integer> index = new HashMap<>();
                for (int i = 0; i < sorted.size(); i++) {
                    index.put(sorted.get(i), i);
                }
                for (Edge<String> edge : graph.getEdge()) {
                    assertTrue(index.get(edge.getVertex_u()) < index.get(edge.getVertex_v()));
                }
            }
        }
    }

    @Test
    void sortsHandleCyclesAndEmptyGraphs() {
        for (Graph<String> graph : GraphTestSupport.graphs()) {
            for (TopologicalSort<String> sorter :
                    List.of(new DfsSort<String>(), new KahnSort<String>())) {
                assertEquals(List.of(), sorter.sort(graph));
            }
            Vertex<String> a = GraphTestSupport.vertex(1);
            Vertex<String> b = GraphTestSupport.vertex(2);
            graph.addVertex(a);
            graph.addVertex(b);
            graph.addEdge(a, b);
            graph.addEdge(b, a);
            assertThrows(IllegalStateException.class, () -> new DfsSort<String>().sort(graph));
            assertThrows(IllegalStateException.class, () -> new KahnSort<String>().sort(graph));
            graph.removeEdge(b, a);
            graph.addEdge(a, a);
            assertThrows(IllegalStateException.class, () -> new DfsSort<String>().sort(graph));
            assertThrows(IllegalStateException.class, () -> new KahnSort<String>().sort(graph));
        }
    }
}
