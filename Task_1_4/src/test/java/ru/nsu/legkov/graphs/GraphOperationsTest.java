package ru.nsu.legkov.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.nsu.legkov.Edge;
import ru.nsu.legkov.Vertex;

/** Tests for all three graph implementations. */
class GraphOperationsTest {
    @Test
    void addVerticesAndEdges() {
        for (Graph<String> graph : GraphTestSupport.graphs()) {
            final Vertex<String> a = GraphTestSupport.vertex(1);
            final Vertex<String> b = GraphTestSupport.vertex(2);
            final Vertex<String> c = GraphTestSupport.vertex(3);
            assertFalse(graph.addVertex(null));
            assertTrue(graph.addVertex(a));
            assertTrue(graph.addVertex(b));
            assertTrue(graph.addVertex(c));
            assertFalse(graph.addVertex(GraphTestSupport.vertex(1)));
            assertFalse(graph.addEdge(null, b));
            assertFalse(graph.addEdge(a, null));
            assertFalse(graph.addEdge(a, GraphTestSupport.vertex(99)));
            assertFalse(graph.addEdge(GraphTestSupport.vertex(99), a));
            assertTrue(graph.addEdge(a, b));
            assertTrue(graph.addEdge(a, c));
            assertTrue(graph.addEdge(b, a));
            assertFalse(graph.addEdge(a, b));
            assertEquals(Set.of(a, b, c), new HashSet<>(graph.getVertices()));
            assertEquals(Set.of(b, c), new HashSet<>(graph.getNeighbors(a)));
            assertEquals(List.of(a), graph.getNeighbors(b));
            assertEquals(List.of(), graph.getNeighbors(null));
            assertEquals(List.of(), graph.getNeighbors(GraphTestSupport.vertex(99)));
            assertEquals(
                    Set.of(new Edge<>(a, b), new Edge<>(a, c), new Edge<>(b, a)),
                    new HashSet<>(graph.getEdge()));
        }
    }

    @Test
    void removeEdgesAndVertices() {
        for (Graph<String> graph : GraphTestSupport.graphs()) {
            Vertex<String> a = GraphTestSupport.vertex(1);
            Vertex<String> b = GraphTestSupport.vertex(2);
            Vertex<String> c = GraphTestSupport.vertex(3);
            graph.addVertex(a);
            graph.addVertex(b);
            graph.addVertex(c);
            assertFalse(graph.removeEdge(null, b));
            assertFalse(graph.removeEdge(a, null));
            assertFalse(graph.removeEdge(a, b));
            assertTrue(graph.addEdge(a, b));
            assertTrue(graph.addEdge(b, a));
            assertTrue(graph.addEdge(c, b));
            assertTrue(graph.removeEdge(a, b));
            assertFalse(graph.removeEdge(a, b));
            assertTrue(graph.addEdge(a, b));
            assertFalse(graph.removeVertex(null));
            assertFalse(graph.removeVertex(GraphTestSupport.vertex(99)));
            assertTrue(graph.removeVertex(b));
            assertFalse(graph.removeVertex(b));
            assertEquals(Set.of(a, c), new HashSet<>(graph.getVertices()));
            assertTrue(graph.getEdge().isEmpty());
            assertTrue(graph.getNeighbors(a).isEmpty());
            assertTrue(graph.addEdge(c, a));
            assertEquals(List.of(a), graph.getNeighbors(c));
            assertTrue(graph.removeVertex(c));
            assertTrue(graph.removeVertex(a));
            assertTrue(graph.getVertices().isEmpty());
            assertTrue(graph.getEdge().isEmpty());
        }
    }

    @Test
    void removingMiddleVertexKeepsOtherEdges() {
        for (Graph<String> graph : GraphTestSupport.graphs()) {
            final Vertex<String> a = GraphTestSupport.vertex(1);
            final Vertex<String> b = GraphTestSupport.vertex(2);
            final Vertex<String> c = GraphTestSupport.vertex(3);
            final Vertex<String> d = GraphTestSupport.vertex(4);
            for (Vertex<String> v : List.of(a, b, c, d)) {
                graph.addVertex(v);
            }
            graph.addEdge(a, d);
            graph.addEdge(d, c);
            graph.addEdge(b, c);
            graph.addEdge(c, b);
            graph.addEdge(b, b);
            graph.addEdge(d, b);
            assertTrue(graph.removeVertex(b));
            assertEquals(
                    Set.of(new Edge<>(a, d), new Edge<>(d, c)), new HashSet<>(graph.getEdge()));
            assertEquals(List.of(d), graph.getNeighbors(a));
            assertEquals(List.of(c), graph.getNeighbors(d));
            assertTrue(graph.addVertex(b));
            assertTrue(graph.addEdge(b, a));
        }
    }

    @Test
    void loopsAndReverseEdgesAreDistinct() {
        for (Graph<String> graph : GraphTestSupport.graphs()) {
            Vertex<String> a = GraphTestSupport.vertex(1);
            Vertex<String> b = GraphTestSupport.vertex(2);
            graph.addVertex(a);
            graph.addVertex(b);
            assertTrue(graph.addEdge(a, a));
            assertTrue(graph.addEdge(a, b));
            assertTrue(graph.addEdge(b, a));
            assertEquals(Set.of(a, b), new HashSet<>(graph.getNeighbors(a)));
            assertEquals(3, graph.getEdge().size());
            assertTrue(graph.removeEdge(a, a));
            assertEquals(List.of(b), graph.getNeighbors(a));
            assertEquals(2, graph.getEdge().size());
        }
    }

    @Test
    void growMatricesBeyondInitialCapacity() {
        for (Graph<String> graph : GraphTestSupport.graphs()) {
            List<Vertex<String>> vertices = new ArrayList<>();
            for (int i = 0; i < 35; i++) {
                Vertex<String> vertex = GraphTestSupport.vertex(i);
                vertices.add(vertex);
                assertTrue(graph.addVertex(vertex));
            }
            for (int i = 0; i < 34; i++) {
                assertTrue(graph.addEdge(vertices.get(i), vertices.get(i + 1)));
            }
            assertEquals(35, graph.getVertices().size());
            assertEquals(34, graph.getEdge().size());
            assertEquals(List.of(vertices.get(34)), graph.getNeighbors(vertices.get(33)));
            assertTrue(graph.removeVertex(vertices.get(10)));
            assertEquals(34, graph.getVertices().size());
            assertEquals(32, graph.getEdge().size());
        }
    }

    @Test
    void growIncidenceEdgeCapacity() {
        for (Graph<String> graph : GraphTestSupport.graphs()) {
            Vertex<String> a = GraphTestSupport.vertex(1);
            Vertex<String> b = GraphTestSupport.vertex(2);
            Vertex<String> c = GraphTestSupport.vertex(3);
            graph.addVertex(a);
            graph.addVertex(b);
            graph.addVertex(c);
            // Достаточно различных рёбер для матрицы с начальной ёмкостью 16.
            List<Vertex<String>> all = new ArrayList<>();
            for (int i = 4; i <= 8; i++) {
                Vertex<String> v = GraphTestSupport.vertex(i);
                graph.addVertex(v);
                all.add(v);
            }
            all.add(a);
            all.add(b);
            all.add(c);
            for (int i = 0; i < all.size(); i++) {
                for (int j = 0; j < all.size(); j++) {
                    if (i != j) {
                        assertTrue(graph.addEdge(all.get(i), all.get(j)));
                    }
                }
            }
            assertEquals(56, graph.getEdge().size());
            assertTrue(graph.removeEdge(all.get(1), all.get(6)));
            assertEquals(55, graph.getEdge().size());
        }
    }
}
