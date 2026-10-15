package ru.nsu.legkov.graphs;

import ru.nsu.legkov.Edge;
import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.io.GraphFileIO;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

/**
 * Ориентированный граф, хранящий рёбра в списках смежности.
 */
public class AdjacencyListGraph<T> implements Graph<T> {
    private final Map<Vertex<T>, List<Vertex<T>>> adjList = new HashMap<>();

    /**
     * Добавляет вершину в граф.
     *
     * @param vertex вершина графа
     * @return true при успешном выполнении операции, иначе false
     */
    @Override
    public boolean addVertex(Vertex<T> vertex) {
        if (vertex == null) {
            return false;
        }
        if (adjList.containsKey(vertex)) {
            return false;
        }
        adjList.put(vertex, new ArrayList<>());
        return true;
    }

    /**
     * Удаляет вершину вместе с инцидентными рёбрами.
     *
     * @param vertex вершина графа
     * @return true при успешном выполнении операции, иначе false
     */
    @Override
    public boolean removeVertex(Vertex<T> vertex) {
        if (vertex == null) {
            return false;
        }
        if (!adjList.containsKey(vertex)) {
            return false;
        }

        adjList.remove(vertex);

        for (List<Vertex<T>> neighbors : adjList.values()) {
            neighbors.removeIf(v -> v.equals(vertex));
        }

        return true;
    }

    /**
     * Добавляет ориентированное ребро.
     *
     * @param from начальная вершина ребра
     * @param to конечная вершина ребра
     * @return true при успешном выполнении операции, иначе false
     */
    @Override
    public boolean addEdge(Vertex<T> from, Vertex<T> to) {
        if (from == null || to == null) {
            return false;
        }
        if (!adjList.containsKey(from) || !adjList.containsKey(to)) {
            return false;
        }

        if (adjList.get(from).contains(to)) {
            return false;
        }

        adjList.get(from).add(to);
        return true;
    }

    /**
     * Удаляет ориентированное ребро.
     *
     * @param from начальная вершина ребра
     * @param to конечная вершина ребра
     * @return true при успешном выполнении операции, иначе false
     */
    @Override
    public boolean removeEdge(Vertex<T> from, Vertex<T> to) {
        if (from == null || to == null) {
            return false;
        }
        if (!adjList.containsKey(from) || !adjList.containsKey(to)) {
            return false;
        }
        return adjList.get(from).remove(to);
    }

    /**
     * Возвращает список всех вершин графа.
     * @return список элементов
     */
    @Override
    public List<Vertex<T>> getVertices() {
        List<Vertex<T>> vertexList = new ArrayList<>();

        for (Map.Entry<Vertex<T>, List<Vertex<T>>> entry : adjList.entrySet()) {
            Vertex<T> value = entry.getKey();
            vertexList.add(value);
        }

        return vertexList;
    }

    /**
     * Возвращает список всех рёбер графа.
     * @return список элементов
     */
    @Override
    public List<Edge<T>> getEdge() {
        List<Edge<T>> edgeList = new ArrayList<>();

        for (Map.Entry<Vertex<T>, List<Vertex<T>>> entry : adjList.entrySet()) {
            Vertex<T> value = entry.getKey();
            List<Vertex<T>> vertexList = entry.getValue();

            for (int i = 0; i < vertexList.size(); i++) {
                Edge<T> edge = new Edge<T>(value, vertexList.get(i));
                edgeList.add(edge);
            }
        }

        return edgeList;
    }

    /**
     * Возвращает список исходящих соседей вершины.
     *
     * @param vertex вершина графа
     * @return список элементов
     */
    @Override
    public List<Vertex<T>> getNeighbors(Vertex<T> vertex) {
        if (vertex == null) return List.of();

        List<Vertex<T>> neighbors = adjList.get(vertex);
        return neighbors == null ? List.of() : new ArrayList<>(neighbors);
    }

    /**
     * Загружает вершины и рёбра графа из файла.
     *
     * @param path путь к файлу
     * @param parser преобразователь строки в данные вершины
     * @throws IOException если произошла ошибка чтения или записи файла
     */
    @Override
    public void loadFromFile(Path path, Function<String, T> parser) throws IOException {
        GraphFileIO.load(this, path, parser);
    }

    /**
     * Сохраняет вершины и рёбра графа в файл.
     *
     * @param path путь к файлу
     * @param formatter преобразователь данных вершины в строку
     * @throws IOException если произошла ошибка чтения или записи файла
     */
    @Override
    public void saveToFile(Path path, Function<T, String> formatter) throws IOException {
        GraphFileIO.save(this, path, formatter);
    }

    /**
     * Сравнивает графы по множествам вершин и рёбер
     *
     * @param obj объект для сравнения
     * @return true при успешном выполнении операции, иначе false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Graph<?> other)) {
            return false;
        }

        return new HashSet<>(getVertices()).equals(new HashSet<>(other.getVertices()))
                && new HashSet<>(getEdge()).equals(new HashSet<>(other.getEdge()));
    }

    /**
     * Вычисляет хеш-код по множествам вершин и рёбер.
     * @return целочисленное значение
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                new HashSet<>(getVertices()),
                new HashSet<>(getEdge())
        );
    }

    /**
     * Возвращает строковое представление графа.
     *
     * @return строковое представление графа
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Vertex<T> vertex : getVertices()) {
            sb.append(vertex).append(" -> ");
            sb.append(getNeighbors(vertex));
            sb.append(System.lineSeparator());
        }

        return sb.toString();
    }
}
