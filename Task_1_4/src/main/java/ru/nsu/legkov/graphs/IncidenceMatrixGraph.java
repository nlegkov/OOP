package ru.nsu.legkov.graphs;

import ru.nsu.legkov.Edge;
import ru.nsu.legkov.Vertex;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

public class IncidenceMatrixGraph<T> implements Graph<T> {
    private int[][] matrix;

    private final Map<Vertex<T>, Integer> vertexToMap = new HashMap<>();
    private final Map<Edge<T>, Integer> edgeToMap = new HashMap<>();
    private final List<Vertex<T>> listVertex = new ArrayList<>();
    private final List<Edge<T>> listEdge = new ArrayList<>();

    private void setSizeVertex(int size) {
        int[][] newMatrix = new int[size][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
        }
        matrix = newMatrix;
    }

    private void setSizeEdge(int size) {
        int[][] newMatrix = new int[matrix.length][size];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
        }
        matrix = newMatrix;
    }

    public IncidenceMatrixGraph() {
        matrix = new int[16][16];
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
            setSizeVertex(matrix.length * 2);
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

        int lastIndex = listVertex.size() - 1;
        int indexVertex = vertexToMap.get(vertex);

        Collections.swap(listVertex, indexVertex, listVertex.size() - 1);

        for (int i = listEdge.size() - 1; i >= 0; i--) {
            if (matrix[indexVertex][i] != 0) {
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
        removeEdgeByIndex(edgeIndex);
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

        for (int j = 0; j < listEdge.size(); j++) {
            if (matrix[indexV][j] == -1) {
                Edge<T> edge = listEdge.get(j);
                neighbors.add(edge.getVertex_v());
            }
        }

        return neighbors;
    }
}
