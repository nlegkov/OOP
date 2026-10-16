package ru.nsu.legkov;

import java.util.Objects;

/** Ориентированное ребро между двумя вершинами. */
public class Edge<T> {
    private final Vertex<T> vertexU;
    private final Vertex<T> vertexV;

    /**
     * Создаёт ориентированное ребро.
     *
     * @param vertexU начальная вершина
     * @param vertexV конечная вершина
     */
    public Edge(Vertex<T> vertexU, Vertex<T> vertexV) {
        this.vertexU = vertexU;
        this.vertexV = vertexV;
    }

    /**
     * Возвращает начальную вершину ребра.
     *
     * @return вершина графа
     */
    public Vertex<T> getVertex_u() {
        return vertexU;
    }

    /**
     * Возвращает конечную вершину ребра.
     *
     * @return вершина графа
     */
    public Vertex<T> getVertex_v() {
        return vertexV;
    }

    /**
     * Сравнивает ориентированные рёбра по их вершинам.
     *
     * @param o объект для сравнения
     *
     * @return true при успешном выполнении операции, иначе false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edge<?> edge = (Edge<?>) o;
        return Objects.equals(vertexU, edge.vertexU)
                && Objects.equals(vertexV, edge.vertexV);
    }

    /**
     * Вычисляет хеш-код ребра.
     *
     * @return целочисленное значение
     */
    @Override
    public int hashCode() {
        return Objects.hash(vertexU, vertexV);
    }

    /**
     * Возвращает идентификаторы концов ребра в строковом виде.
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return vertexU.getId() + ";" + vertexV.getId();
    }
}
