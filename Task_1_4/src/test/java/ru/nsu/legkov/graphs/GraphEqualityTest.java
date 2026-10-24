package ru.nsu.legkov.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.legkov.Vertex;

/** Tests for graph equality across implementations. */
class GraphEqualityTest {
    @Test
    void graphEqualityAcrossImplementations() {
        List<Graph<String>> all = GraphTestSupport.graphs();
        Vertex<String> a = GraphTestSupport.vertex(1);
        Vertex<String> b = GraphTestSupport.vertex(2);
        for (Graph<String> graph : all) {
            graph.addVertex(a);
            graph.addVertex(b);
            graph.addEdge(a, b);
        }
        for (Graph<String> first : all) {
            assertEquals(first, first);
            assertNotEquals(first, null);
            assertNotEquals(first, "graph");
            for (Graph<String> second : all) {
                assertEquals(first, second);
                assertEquals(first.hashCode(), second.hashCode());
            }
            assertNotNull(first.toString());
        }
        all.get(0).addEdge(b, a);
        assertNotEquals(all.get(0), all.get(1));
        all.get(1).removeVertex(b);
        assertNotEquals(all.get(1), all.get(2));
    }
}
