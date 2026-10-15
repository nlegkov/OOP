package ru.nsu.legkov.sort;

import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.graphs.Graph;

import java.util.*;

/**
 * Топологическая сортировка ориентированного графа обходом в глубину.
 */
public class DfsSort<T> implements TopologicalSort<T> {

    /**
     * Выполняет топологическую сортировку методом DFS.
     *
     * @param graph исходный граф
     * @return список элементов
     */
    @Override
    public List<Vertex<T>> sort(Graph<T> graph) {
        Map<Vertex<T>, Boolean> finished = new HashMap<>();
        List<Vertex<T>> result = new ArrayList<>();

        for (Vertex<T> vertex : graph.getVertices()) {
          /**
           * Обходит вершину и проверяет наличие циклов.
           *
           * @param graph исходный граф
           * @param vertex вершина графа
           * @param finished состояния посещения вершин
           * @param result список результата сортировки
           */
            visit(graph, vertex, finished, result);
        }

        Collections.reverse(result);
        return result;
    }

    /**
     * Обходит вершину и проверяет наличие циклов.
     *
     * @param graph исходный граф
     * @param vertex вершина графа
     * @param finished состояния посещения вершин
     * @param result список результата сортировки
     */
    private void visit(
            Graph<T> graph,
            Vertex<T> vertex,
            Map<Vertex<T>, Boolean> finished,
            List<Vertex<T>> result
    ) {
        Boolean state = finished.get(vertex);

        if (state != null) {
            if (!state) {
                throw new IllegalStateException("В графе есть цикл");
            }
            return;
        }

        finished.put(vertex, false);

        for (Vertex<T> neighbor : graph.getNeighbors(vertex)) {
          /**
           * Обходит вершину и проверяет наличие циклов.
           *
           * @param graph исходный граф
           * @param neighbor параметр метода
           * @param finished состояния посещения вершин
           * @param result список результата сортировки
           */
            visit(graph, neighbor, finished, result);
        }

        finished.put(vertex, true);
        result.add(vertex);
    }
}