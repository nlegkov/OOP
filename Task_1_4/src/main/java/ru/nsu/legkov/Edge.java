package ru.nsu.legkov;

import java.util.Objects;

public class Edge {
    Vertex<?> vertex_u;
    Vertex<?> vertex_v;

    public Edge(Vertex<?> vertex_u, Vertex<?> vertex_v) {
        this.vertex_v = vertex_v;
        this.vertex_u = vertex_u;
    }

    public Vertex<?> getVertex_u() {
        return vertex_u;
    }

    public Vertex<?> getVertex_v() {
        return vertex_v;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(vertex_u, edge.vertex_u) && Objects.equals(vertex_v, edge.vertex_v);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertex_u, vertex_v);
    }
}
