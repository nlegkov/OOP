package ru.nsu.legkov.graphs;

import ru.nsu.legkov.Edge;
import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.io.GraphFileIO;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

/**
 * Ориентированный граф, хранящий рёбра в матрице смежности.
 */
public class AdjacencyMatrixGraph<T> implements Graph<T> {
    private final Map<Vertex<T>, Integer> vertexToMap = new HashMap<>();
    private final List<Vertex<T>> listVertex = new ArrayList<>();

    private boolean[][] matrix;

    /**
     * Создаёт пустую матрицу смежности.
     * @return результат операции
     */
    public AdjacencyMatrixGraph() {
        matrix = new boolean[16][16];
    }

    /**
     * Увеличивает размер матрицы с сохранением рёбер.
     *
     * @param size новый размер матрицы
     */
    private void setSize(int size) {
        boolean[][] newMatrix = new boolean[size][size];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
        }
        matrix = newMatrix;
    }

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
        if (vertexToMap.containsKey(vertex)) {
            return false;
        }
        if (listVertex.size() == matrix.length) {
          /**
           * Увеличивает размер матрицы с сохранением рёбер.
           *
           * @param length параметр метода
           */
            setSize(matrix.length * 2);
        }
        vertexToMap.put(vertex, listVertex.size());
        listVertex.add(vertex);
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
        if (!vertexToMap.containsKey(vertex)) {
            return false;
        }

        int indexToRemove = vertexToMap.get(vertex);
        int lastIndex = listVertex.size() - 1;

        if (indexToRemove != lastIndex) {
            Vertex<T> lastVertex = listVertex.get(lastIndex);

            for (int i = 0; i < matrix.length; i++) {
                matrix[i][indexToRemove] = matrix[i][lastIndex];
                matrix[indexToRemove][i] = matrix[lastIndex][i];
            }

            matrix[indexToRemove][indexToRemove] = matrix[lastIndex][lastIndex];

            listVertex.set(indexToRemove, lastVertex);
            vertexToMap.put(lastVertex, indexToRemove);
        }

        for (int i = 0; i < matrix.length; i++) {
            matrix[lastIndex][i] = false;
            matrix[i][lastIndex] = false;
        }

        listVertex.remove(lastIndex);
        vertexToMap.remove(vertex);

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
        if (!vertexToMap.containsKey(from) || !vertexToMap.containsKey(to)) {
            return false;
        }
        int fromIndex = vertexToMap.get(from);
        int toIndex = vertexToMap.get(to);
        if (matrix[fromIndex][toIndex]) {
            return false;
        }
        matrix[fromIndex][toIndex] = true;
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
        if (!vertexToMap.containsKey(from) || !vertexToMap.containsKey(to)) {
            return false;
        }
        int fromIndex = vertexToMap.get(from);
        int toIndex = vertexToMap.get(to);
        if (!matrix[fromIndex][toIndex]) {
            return false;
        }
        matrix[fromIndex][toIndex] = false;
        return true;
    }

    /**
     * Возвращает список всех вершин графа.
     * @return список элементов
     */
    @Override
    public List<Vertex<T>> getVertices() {
        return List.copyOf(listVertex);
    }

    /**
     * Возвращает список всех рёбер графа.
     * @return список элементов
     */
    @Override
    public List<Edge<T>> getEdge() {
        List<Edge<T>> edgeList = new ArrayList<>();

        for (int i = 0; i < listVertex.size(); i++) {
            for (int j = 0; j < listVertex.size(); j++) {
                if (matrix[i][j]) {
                    Edge<T> edge = new Edge<>(listVertex.get(i), listVertex.get(j));

                    edgeList.add(edge);
                }
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
        if (vertex == null || !vertexToMap.containsKey(vertex)) {
            return List.of();
        }

        int indexV = vertexToMap.get(vertex);
        List<Vertex<T>> neighbors = new ArrayList<>();

        for (int i = 0; i < listVertex.size(); i++) {
            if (matrix[indexV][i]) {
                neighbors.add(listVertex.get(i));
            }
        }

        return neighbors;
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
     * Сравнивает графы по множествам вершин и рёбер.
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
