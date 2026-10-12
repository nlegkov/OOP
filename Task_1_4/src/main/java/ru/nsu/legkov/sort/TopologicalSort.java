package ru.nsu.legkov.sort;

import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.graphs.Graph;

import java.util.List;

public interface TopologicalSort<T> {
    List<Vertex<T>> sort(Graph<T> graph);
}