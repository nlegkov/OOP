package ru.nsu.legkov.graphs;

import ru.nsu.legkov.Vertex;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class AdjacencyMatrixGraph<T> implements Graph<T> {
    private final Map<Vertex<T>, Integer> vertexToMap = new HashMap<>();
    private final List<Vertex<T>> listVertex = new ArrayList<>();

    boolean[][] matrix;

    public AdjacencyMatrixGraph() {
        matrix = new boolean[16][16];
    }

    private void setSize(int size) {
        boolean[][] newMatrix = new boolean[size][size];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
        }
        matrix = newMatrix;
    }

    @Override
    public boolean addVertex(Vertex<T> vertex) {
        if (vertex == null) {
            return false;
        }
        if (vertexToMap.containsKey(vertex)) {
            return false;
        }
        if (listVertex.size() == matrix.length) {
            setSize(matrix.length * 2);
        }
        vertexToMap.put(vertex, listVertex.size());
        listVertex.add(vertex);
        return true;
    }

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

    @Override
    public List<Vertex<T>> getVertices() {
        return List.of();
    }

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

    @Override
    public void loadFromFile(Path path, Function<String, T> parser) throws IOException {

    }

    @Override
    public void saveToFile(Path path, Function<T, String> formatter) throws IOException {

    }
}
