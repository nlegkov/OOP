package ru.nsu.legkov.sort;

import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.graphs.Graph;

import java.util.List;

/**
 * Интерфейс алгоритма топологической сортировки графа.
 */
public interface TopologicalSort<T> {
    /**
     * Возвращает вершины графа в топологическом порядке.
     *
     * @param graph исходный граф
     * @return список элементов
     */
    List<Vertex<T>> sort(Graph<T> graph);
}