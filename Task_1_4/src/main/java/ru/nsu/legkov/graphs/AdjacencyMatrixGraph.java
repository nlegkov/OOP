package ru.nsu.legkov.graphs;

import ru.nsu.legkov.Vertex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdjacencyMatrixGraph implements Graph{
    private final Map<Vertex<?>, Integer> vertexToMap = new HashMap<>();
    private final List<Vertex<?>> listVertex = new ArrayList<>();

    boolean[][] matrix;

    public AdjacencyMatrixGraph() {
        matrix = new boolean[16][16];
    }

    private void setSize(int  size) {
        boolean[][] newMatrix = new boolean[size][size];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
        }
        matrix = newMatrix;
    }

    @Override
    public boolean addVertex(Vertex<?> vertex) {
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
    public boolean removeVertex(Vertex<?> vertex) {
        if (vertex == null) {
            return false;
        }
        if (!vertexToMap.containsKey(vertex)) {
            return false;
        }

        int indexToRemove = vertexToMap.get(vertex);
        int lastIndex = listVertex.size() - 1;

        if (indexToRemove != lastIndex) {
            Vertex<?> lastVertex = listVertex.get(lastIndex);

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
    public boolean addEdge(Vertex<?> from, Vertex<?> to) {
        if (from == null || to == null) {
            return false;
        }
        if (!vertexToMap.containsKey(from) || !vertexToMap.containsKey(to)) {
            return false;
        }
        int fromIndex = vertexToMap.get(from);
        int toIndex = vertexToMap.get(to);
        // Match the other representations: true means the graph actually changed.
        if (matrix[fromIndex][toIndex]) {
            return false;
        }
        matrix[fromIndex][toIndex] = true;
        return true;
    }

    @Override
    public boolean removeEdge(Vertex<?> from, Vertex<?> to) {
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
    public List<Vertex<?>> getNeighbors(Vertex<?> vertex) {
        if (vertex == null || !vertexToMap.containsKey(vertex)) {
            return List.of();
        }

        int indexV = vertexToMap.get(vertex);
        List<Vertex<?>> neighbors = new ArrayList<>();

        for (int i = 0; i < listVertex.size(); i++) {
            if (matrix[indexV][i]) {
                neighbors.add(listVertex.get(i));
            }
        }

        return neighbors;
    }

    @Override
    public boolean saveToFile(String filename) {
        return false;
    }

    @Override
    public boolean loadFromFile(String filename) throws IOException {
        Path path = Path.of(filename);

        String[] content = Files.readString(path).split("\n");

        String[] str = content[0].split(";");

        try {
            int n, m;
            n = Integer.parseInt(str[0]);
            m = Integer.parseInt(str[1]);
        }
        catch (Exception e) {
            System.out.println("n или m не число");
            return false;
        }

        for (int i = 1; i < content.length; i++) {
            str = content[i].split(";");

            Vertex<?> vertex1;
            Vertex<?> vertex2;

            try {
                int vert = Integer.parseInt(str[0]);
                vertex1 = new Vertex<>(vert);
                this.addVertex(vertex1);
            } catch (Exception e) {
                vertex1 = new Vertex<>(str[0]);
                this.addVertex(vertex1);
            }

            try {
                int vert = Integer.parseInt(str[1]);
                vertex2 = new Vertex<>(vert);
                this.addVertex(vertex2);
            } catch (Exception e) {
                vertex2 = new Vertex<>(str[1]);
                this.addVertex(vertex2);
            }

            this.addEdge(vertex1, vertex2);
        }

        return true;
    }
}
