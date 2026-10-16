package ru.nsu.legkov.sort;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.graphs.Graph;

/** Топологическая сортировка по алгоритму Кана. */
public class KahnSort<T> implements TopologicalSort<T> {

    /**
     * Выполняет топологическую сортировку по входящим степеням вершин.
     *
     * @param graph исходный граф
     *
     * @return список элементов
     */
    @Override
    public List<Vertex<T>> sort(Graph<T> graph) {
        List<Vertex<T>> vertices = graph.getVertices();
        Map<Vertex<T>, Integer> inDegree = new HashMap<>();

        for (Vertex<T> vertex : vertices) {
            inDegree.put(vertex, 0);
        }

        // увел число входящих ребер
        for (Vertex<T> vertex : vertices) {
            for (Vertex<T> neighbor : graph.getNeighbors(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        Queue<Vertex<T>> queue = new ArrayDeque<>();

        for (Vertex<T> vertex : vertices) {
            if (inDegree.get(vertex) == 0) {
                queue.add(vertex);
            }
        }

        List<Vertex<T>> result = new ArrayList<>();

        while (!queue.isEmpty()) {
            Vertex<T> vertex = queue.remove();
            result.add(vertex);

            // Мысленно удаляем исходящие рёбра vertex.
            for (Vertex<T> neighbor : graph.getNeighbors(vertex)) {
                int degree = inDegree.get(neighbor) - 1;
                inDegree.put(neighbor, degree);

                if (degree == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (result.size() != vertices.size()) {
            throw new IllegalStateException("В графе есть цикл");
        }

        return result;
    }
}
