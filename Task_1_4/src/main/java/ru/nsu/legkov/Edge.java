package ru.nsu.legkov;

import java.util.Objects;

/**
 * Ориентированное ребро между двумя вершинами.
 */
public class Edge<T> {
    Vertex<T> vertex_u;
    Vertex<T> vertex_v;

    /**
     * Создаёт ориентированное ребро.
     *
     * @param vertex_u начальная вершина ребра
     * @param vertex_v конечная вершина ребра
     */
    public Edge(Vertex<T> vertex_u, Vertex<T> vertex_v) {
        this.vertex_v = vertex_v;
        this.vertex_u = vertex_u;
    }

    /**
     * Возвращает начальную вершину ребра.
     * @return вершина графа
     */
    public Vertex<T> getVertex_u() {
        return vertex_u;
    }

    /**
     * Возвращает конечную вершину ребра.
     * @return вершина графа
     */
    public Vertex<T> getVertex_v() {
        return vertex_v;
    }

    /**
     * Сравнивает ориентированные рёбра по их вершинам.
     *
     * @param o объект для сравнения
     * @return true при успешном выполнении операции, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge<T> edge = (Edge<T>) o;
        return Objects.equals(vertex_u, edge.vertex_u) && Objects.equals(vertex_v, edge.vertex_v);
    }

    /**
     * Вычисляет хеш-код ребра.
     * @return целочисленное значение
     */
    @Override
    public int hashCode() {
        return Objects.hash(vertex_u, vertex_v);
    }

    /**
     * Возвращает идентификаторы концов ребра в строковом виде.
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return vertex_u.getId() + ";" + vertex_v.getId();
    }
}
