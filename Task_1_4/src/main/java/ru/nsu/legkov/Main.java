package ru.nsu.legkov;

import ru.nsu.legkov.graphs.AdjacencyMatrixGraph;
import ru.nsu.legkov.graphs.Graph;
import ru.nsu.legkov.sort.DfsSort;
import ru.nsu.legkov.sort.KahnSort;
import ru.nsu.legkov.sort.TopologicalSort;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Graph<String> graph = new AdjacencyMatrixGraph<>();

        Vertex<String> a = new Vertex<>(1, "A");
        Vertex<String> b = new Vertex<>(2, "B");
        Vertex<String> c = new Vertex<>(3, "C");

        graph.addVertex(a);
        graph.addVertex(b);
        graph.addVertex(c);

        graph.addEdge(a, c);
        graph.addEdge(b, c);

        TopologicalSort<String> sorter = new KahnSort<>();
        List<Vertex<String>> result = sorter.sort(graph);

        System.out.println(result);

        sorter = new DfsSort<>();
        System.out.println(sorter.sort(graph));
    }
}