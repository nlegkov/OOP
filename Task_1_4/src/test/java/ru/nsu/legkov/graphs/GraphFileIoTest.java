package ru.nsu.legkov.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.nsu.legkov.Vertex;

/** Tests for graph serialization and parsing. */
class GraphFileIoTest {
    @TempDir Path tempDir;

    @Test
    void saveLoadRoundTrip() throws IOException {
        for (int i = 0; i < GraphTestSupport.graphs().size(); i++) {
            Graph<String> original = GraphTestSupport.graphs().get(i);
            Vertex<String> a = new Vertex<>(1, "alpha");
            Vertex<String> b = new Vertex<>(2, "beta");
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
            Graph<String> loaded = GraphTestSupport.graphs().get(i);
            loaded.loadFromFile(path, value -> value);
            assertEquals(
                    new HashSet<>(original.getVertices()), new HashSet<>(loaded.getVertices()));
            assertEquals(new HashSet<>(original.getEdge()), new HashSet<>(loaded.getEdge()));
            assertEquals(
                    "alpha",
                    loaded.getVertices().stream()
                            .filter(v -> v.getId() == 1)
                            .findFirst()
                            .orElseThrow()
                            .getData());
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
        assertEquals(
                20,
                graph.getVertices().stream()
                        .filter(v -> v.getId() == 2)
                        .findFirst()
                        .orElseThrow()
                        .getData());
        Path saved = tempDir.resolve("numbers-out.txt");
        graph.saveToFile(saved, Object::toString);
        assertTrue(Files.readString(saved).contains("2;20"));
    }

    @Test
    void loadInvalidFiles() throws IOException {
        String[] invalidContents = {
            "bad_line\n", // нет разделителя
            "1;A\n1;B\n", // повтор вершины
            "1;A\n\n1;2\n", // нет целевой вершины
            "1;A\n2;B\n\n1;2\n1;2\n", // повтор ребра
            "1;A\n2;B\n\ninvalid\n" // некорректное ребро
        };
        for (int i = 0; i < invalidContents.length; i++) {
            Path file = tempDir.resolve("invalid" + i + ".txt");
            Files.writeString(file, invalidContents[i]);
            for (Graph<String> graph : GraphTestSupport.graphs()) {
                assertThrows(Exception.class, () -> graph.loadFromFile(file, value -> value));
            }
        }
    }

    @Test
    void emptyGraphAndFile() throws IOException {
        Path file = tempDir.resolve("empty.txt");
        for (Graph<String> graph : GraphTestSupport.graphs()) {
            assertTrue(graph.getVertices().isEmpty());
            assertTrue(graph.getEdge().isEmpty());
            graph.saveToFile(file, value -> value);
            Graph<String> loaded = new AdjacencyListGraph<>();
            loaded.loadFromFile(file, value -> value);
            assertTrue(loaded.getVertices().isEmpty());
        }
    }
}
