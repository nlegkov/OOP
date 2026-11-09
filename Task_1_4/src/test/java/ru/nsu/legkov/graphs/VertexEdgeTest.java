package ru.nsu.legkov.graphs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;
import ru.nsu.legkov.Edge;
import ru.nsu.legkov.Vertex;

/** Tests for vertex and edge value objects. */
class VertexEdgeTest {
    @Test
    void vertexAndEdgeValueObjects() {
        final Vertex<String> a = new Vertex<>(1, "A");
        final Vertex<String> sameId = new Vertex<>(1, "Other");
        final Vertex<String> b = new Vertex<>(2, "B");
        assertEquals(1, a.getId());
        assertEquals("A", a.getData());
        assertEquals("1;A", a.toString());
        assertEquals(a, sameId);
        assertEquals(a.hashCode(), sameId.hashCode());
        assertNotEquals(a, b);
        assertNotEquals(null, a);
        assertNotEquals(a, "A");
        assertEquals(a, a);

        final Edge<String> edge = new Edge<>(a, b);
        final Edge<String> same = new Edge<>(sameId, b);
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
}
