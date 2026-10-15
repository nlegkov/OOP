package ru.nsu.legkov.graphs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.legkov.Edge;
import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.sort.DfsSort;
import ru.nsu.legkov.sort.KahnSort;
import ru.nsu.legkov.sort.TopologicalSort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/** Тесты представлений графа, файлового формата и топологической сортировки. */
class MainTest {
    @TempDir
    Path tempDir;

    private List<Graph<String>> graphs() {
        return List.of(new AdjacencyListGraph<>(),
                new AdjacencyMatrixGraph<>(), new IncidenceMatrixGraph<>());
    }

    private Vertex<String> vertex(int id) {
        return new Vertex<>(id, "V" + id);
    }

    @Test
    void vertexAndEdgeValueObjects() {
        Vertex<String> a = new Vertex<>(1, "A");
        Vertex<String> sameId = new Vertex<>(1, "Other");
        Vertex<String> b = new Vertex<>(2, "B");
        assertEquals(1, a.getId());
        assertEquals("A", a.getData());
        assertEquals("1;A", a.toString());
        assertEquals(a, sameId);
        assertEquals(a.hashCode(), sameId.hashCode());
        assertNotEquals(a, b);
        assertNotEquals(null, a);
        assertNotEquals(a, "A");
        assertEquals(a, a);

        Edge<String> edge = new Edge<>(a, b);
        Edge<String> same = new Edge<>(sameId, b);
        assertEquals(a, edge.getVertex_u());
        assertEquals(b, edge.getVertex_v());
        assertEquals("1;2", edge.toString());
        assertEquals(edge, same);
        assertEquals(edge.hashCode(), same.hashCode());
        assertEquals(edge, edge);
        assertNotEquals(edge, new Edge<>(b, a));
        assertNotEquals(edge, new Edge<>(a, a));
        assertNotEquals(edge, null);
        assertNotEquals(edge, "1;2");
    }

    @Test
    void addVerticesAndEdges() {
        for (Graph<String> graph : graphs()) {
            Vertex<String> a = vertex(1);
            Vertex<String> b = vertex(2);
            Vertex<String> c = vertex(3);
            assertFalse(graph.addVertex(null));
            assertTrue(graph.addVertex(a));
            assertTrue(graph.addVertex(b));
            assertTrue(graph.addVertex(c));
            assertFalse(graph.addVertex(vertex(1)));
            assertFalse(graph.addEdge(null, b));
            assertFalse(graph.addEdge(a, null));
            assertFalse(graph.addEdge(a, vertex(99)));
            assertFalse(graph.addEdge(vertex(99), a));
            assertTrue(graph.addEdge(a, b));
            assertTrue(graph.addEdge(a, c));
            assertTrue(graph.addEdge(b, a));
            assertFalse(graph.addEdge(a, b));
            assertEquals(Set.of(a, b, c), new HashSet<>(graph.getVertices()));
            assertEquals(Set.of(b, c), new HashSet<>(graph.getNeighbors(a)));
            assertEquals(List.of(a), graph.getNeighbors(b));
            assertEquals(List.of(), graph.getNeighbors(null));
            assertEquals(List.of(), graph.getNeighbors(vertex(99)));
            assertEquals(Set.of(new Edge<>(a, b), new Edge<>(a, c), new Edge<>(b, a)),
                    new HashSet<>(graph.getEdge()));
        }
    }

    @Test
    void removeEdgesAndVertices() {
        for (Graph<String> graph : graphs()) {
            Vertex<String> a = vertex(1);
            Vertex<String> b = vertex(2);
            Vertex<String> c = vertex(3);
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
            assertFalse(graph.removeVertex(vertex(99)));
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
        for (Graph<String> graph : graphs()) {
            Vertex<String> a = vertex(1), b = vertex(2), c = vertex(3), d = vertex(4);
            for (Vertex<String> v : List.of(a, b, c, d)) graph.addVertex(v);
            graph.addEdge(a, d);
            graph.addEdge(d, c);
            graph.addEdge(b, c);
            graph.addEdge(c, b);
            graph.addEdge(b, b);
            graph.addEdge(d, b);
            assertTrue(graph.removeVertex(b));
            assertEquals(Set.of(new Edge<>(a, d), new Edge<>(d, c)),
                    new HashSet<>(graph.getEdge()));
            assertEquals(List.of(d), graph.getNeighbors(a));
            assertEquals(List.of(c), graph.getNeighbors(d));
            assertTrue(graph.addVertex(b));
            assertTrue(graph.addEdge(b, a));
        }
    }

    @Test
    void loopsAndReverseEdgesAreDistinct() {
        for (Graph<String> graph : graphs()) {
            Vertex<String> a = vertex(1), b = vertex(2);
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
        for (Graph<String> graph : graphs()) {
            List<Vertex<String>> vertices = new ArrayList<>();
            for (int i = 0; i < 35; i++) {
                Vertex<String> vertex = vertex(i);
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
        for (Graph<String> graph : graphs()) {
            Vertex<String> a = vertex(1), b = vertex(2), c = vertex(3);
            graph.addVertex(a);
            graph.addVertex(b);
            graph.addVertex(c);
            // Достаточно различных рёбер для матрицы с начальной ёмкостью 16.
            List<Vertex<String>> all = new ArrayList<>();
            for (int i = 4; i <= 8; i++) {
                Vertex<String> v = vertex(i);
                graph.addVertex(v);
                all.add(v);
            }
            all.add(a);
            all.add(b);
            all.add(c);
            for (int i = 0; i < all.size(); i++) {
                for (int j = 0; j < all.size(); j++) {
                    if (i != j) assertTrue(graph.addEdge(all.get(i), all.get(j)));
                }
            }
            assertEquals(56, graph.getEdge().size());
            assertTrue(graph.removeEdge(all.get(1), all.get(6)));
            assertEquals(55, graph.getEdge().size());
        }
    }

    @Test
    void graphEqualityAcrossImplementations() {
        List<Graph<String>> all = graphs();
        Vertex<String> a = vertex(1), b = vertex(2);
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

    @Test
    void saveLoadRoundTrip() throws IOException {
        for (int i = 0; i < graphs().size(); i++) {
            Graph<String> original = graphs().get(i);
            Vertex<String> a = new Vertex<>(1, "alpha"), b = new Vertex<>(2, "beta");
            Vertex<String> isolated = new Vertex<>(3, "alone");
            original.addVertex(a);
            original.addVertex(b);
            original.addVertex(isolated);
            original.addEdge(a, b);
            original.addEdge(b, a);
            Path path = tempDir.resolve("graph" + i + ".txt");
            original.saveToFile(path, value -> value);
            String text = Files.readString(path);
            assertTrue(text.contains("1;alpha"));
            assertTrue(text.contains("1;2"));
            Graph<String> loaded = graphs().get(i);
            loaded.loadFromFile(path, value -> value);
            assertEquals(new HashSet<>(original.getVertices()), new HashSet<>(loaded.getVertices()));
            assertEquals(new HashSet<>(original.getEdge()), new HashSet<>(loaded.getEdge()));
            assertEquals("alpha", loaded.getVertices().stream()
                    .filter(v -> v.getId() == 1).findFirst().orElseThrow().getData());
        }
    }

    @Test
    void loadWithCustomParserAndFormatter() throws IOException {
        Path file = tempDir.resolve("numbers.txt");
        Files.writeString(file, "1;10\n2;20\n3;30\n\n1;2\n2;3\n");
        Graph<Integer> graph = new AdjacencyListGraph<>();
        graph.loadFromFile(file, Integer::parseInt);
        assertEquals(3, graph.getVertices().size());
        assertEquals(2, graph.getEdge().size());
        assertEquals(20, graph.getVertices().stream().filter(v -> v.getId() == 2)
                .findFirst().orElseThrow().getData());
        Path saved = tempDir.resolve("numbers-out.txt");
        graph.saveToFile(saved, Object::toString);
        assertTrue(Files.readString(saved).contains("2;20"));
    }

    @Test
    void loadInvalidFiles() throws IOException {
        String[] invalidContents = {
                "bad_line\n",                 // нет разделителя
                "1;A\n1;B\n",                // повтор вершины
                "1;A\n\n1;2\n",            // нет целевой вершины
                "1;A\n2;B\n\n1;2\n1;2\n", // повтор ребра
                "1;A\n2;B\n\ninvalid\n"   // некорректное ребро
        };
        for (int i = 0; i < invalidContents.length; i++) {
            Path file = tempDir.resolve("invalid" + i + ".txt");
            Files.writeString(file, invalidContents[i]);
            for (Graph<String> graph : graphs()) {
                assertThrows(Exception.class, () -> graph.loadFromFile(file, value -> value));
            }
        }
    }

    @Test
    void emptyGraphAndFile() throws IOException {
        Path file = tempDir.resolve("empty.txt");
        for (Graph<String> graph : graphs()) {
            assertTrue(graph.getVertices().isEmpty());
            assertTrue(graph.getEdge().isEmpty());
            graph.saveToFile(file, value -> value);
            Graph<String> loaded = new AdjacencyListGraph<>();
            loaded.loadFromFile(file, value -> value);
            assertTrue(loaded.getVertices().isEmpty());
        }
    }

    @Test
    void dfsAndKahnProduceValidTopologicalOrder() {
        for (Graph<String> graph : graphs()) {
            Vertex<String> a = vertex(1), b = vertex(2), c = vertex(3);
            Vertex<String> d = vertex(4), isolated = vertex(5);
            for (Vertex<String> v : List.of(a, b, c, d, isolated)) graph.addVertex(v);
            graph.addEdge(a, c);
            graph.addEdge(b, c);
            graph.addEdge(c, d);
            graph.addEdge(a, d);
            for (TopologicalSort<String> sorter : List.of(new DfsSort<String>(),
                    new KahnSort<String>())) {
                List<Vertex<String>> sorted = sorter.sort(graph);
                assertEquals(5, sorted.size());
                assertEquals(new HashSet<>(graph.getVertices()), new HashSet<>(sorted));
                Map<Vertex<String>, Integer> index = new HashMap<>();
                for (int i = 0; i < sorted.size(); i++) index.put(sorted.get(i), i);
                for (Edge<String> edge : graph.getEdge()) {
                    assertTrue(index.get(edge.getVertex_u()) < index.get(edge.getVertex_v()));
                }
            }
        }
    }

    @Test
    void sortsHandleCyclesAndEmptyGraphs() {
        for (Graph<String> graph : graphs()) {
            for (TopologicalSort<String> sorter : List.of(new DfsSort<String>(),
                    new KahnSort<String>())) {
                assertEquals(List.of(), sorter.sort(graph));
            }
            Vertex<String> a = vertex(1), b = vertex(2);
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
