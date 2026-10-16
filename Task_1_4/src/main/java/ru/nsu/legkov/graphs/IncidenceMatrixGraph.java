package ru.nsu.legkov.graphs;

import ru.nsu.legkov.Edge;
import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.io.GraphFileIO;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

/**
 * Ориентированный граф, хранящий рёбра в матрице инцидентности.
 */
public class IncidenceMatrixGraph<T> implements Graph<T> {
    private int[][] matrix;

    private final Map<Vertex<T>, Integer> vertexToMap = new HashMap<>();
    private final Map<Edge<T>, Integer> edgeToMap = new HashMap<>();
    private final List<Vertex<T>> listVertex = new ArrayList<>();
    private final List<Edge<T>> listEdge = new ArrayList<>();

    /**
     * Увеличивает число строк матрицы инцидентности.
     *
     * @param size новый размер матрицы
     */
    private void setSizeVertex(int size) {
        int[][] newMatrix = new int[size][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
        }
        matrix = newMatrix;
    }

    /**
     * Увеличивает число столбцов матрицы инцидентности.
     *
     * @param size новый размер матрицы
     */
    private void setSizeEdge(int size) {
        int[][] newMatrix = new int[matrix.length][size];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
        }
        matrix = newMatrix;
    }

    /**
     * Создаёт пустую матрицу инцидентности.
     */
    public IncidenceMatrixGraph() {
        matrix = new int[16][16];
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
           * Увеличивает число строк матрицы инцидентности.
           *
           * @param length параметр метода
           */
            setSizeVertex(matrix.length * 2);
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
        if (vertex == null || !vertexToMap.containsKey(vertex)) {
            return false;
        }

        int lastIndex = listVertex.size() - 1;
        int indexVertex = vertexToMap.get(vertex);

        for (int i = listEdge.size() - 1; i >= 0; i--) {
            Edge<T> edge = listEdge.get(i);

            if (edge.getVertex_u().equals(vertex)
                    || edge.getVertex_v().equals(vertex)) {
              /**
               * Удаляет ребро по индексу столбца, сохраняя компактность хранения.
               *
               * @param i индекс ребра
               */
                removeEdgeByIndex(i);
            }
        }

        if (lastIndex != indexVertex) {
            Vertex<T> lastVertex = listVertex.get(lastIndex);

            for (int i = 0; i < matrix[0].length; i++) {
                matrix[indexVertex][i] = matrix[lastIndex][i];
                matrix[lastIndex][i] = 0;
            }

            listVertex.set(indexVertex, lastVertex);
            vertexToMap.put(lastVertex, indexVertex);
        } else {
            for (int i = 0; i < matrix[0].length; i++) {
                matrix[indexVertex][i] = 0;
            }
        }

        listVertex.remove(lastIndex);
        vertexToMap.remove(vertex);
        return true;
    }

    /**
     * Удаляет ребро по индексу столбца, сохраняя компактность хранения.
     *
     * @param i индекс ребра
     */
    private void removeEdgeByIndex(int i) {
        int lastIndex = listEdge.size() - 1;
        Edge<T> edge = listEdge.get(i);

        if (lastIndex != i) {
            Edge<T> lastEdge = listEdge.get(lastIndex);

            for (int j = 0; j < matrix.length; j++) {
                matrix[j][i] = matrix[j][lastIndex];
                matrix[j][lastIndex] = 0;
            }
            edgeToMap.put(lastEdge, i);
            listEdge.set(i, lastEdge);
        } else {
            for (int j = 0; j < matrix.length; j++) {
                matrix[j][i] = 0;
            }
        }

        listEdge.remove(lastIndex);
        edgeToMap.remove(edge);
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

        Edge<T> edge = new Edge<T>(from, to);
        if (edgeToMap.containsKey(edge)) {
            return false;
        }

        if (listEdge.size() == matrix[0].length) {
          /**
           * Увеличивает число столбцов матрицы инцидентности.
           *
           * @param length параметр метода
           */
            setSizeEdge(matrix[0].length * 2);
        }

        int edgeIndex = listEdge.size();
        edgeToMap.put(edge, edgeIndex);
        listEdge.add(edge);

        int fromIndex = vertexToMap.get(from);
        int toIndex = vertexToMap.get(to);

        matrix[fromIndex][edgeIndex] = -1;
        if (toIndex != fromIndex) {
            matrix[toIndex][edgeIndex] = 1;
        }

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
        Edge<T> edge = new Edge<T>(from, to);
        if (!edgeToMap.containsKey(edge)) {
            return false;
        }

        int edgeIndex = edgeToMap.get(edge);
      /**
       * Удаляет ребро по индексу столбца, сохраняя компактность хранения.
       *
       * @param edgeIndex параметр метода
       */
        removeEdgeByIndex(edgeIndex);
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
        return List.copyOf(listEdge);
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

        for (int j = 0; j < listEdge.size(); j++) {
            if (matrix[indexV][j] == -1) {
                Edge<T> edge = listEdge.get(j);
                neighbors.add(edge.getVertex_v());
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
