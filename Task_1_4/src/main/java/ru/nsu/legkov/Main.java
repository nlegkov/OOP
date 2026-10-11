package ru.nsu.legkov;

import ru.nsu.legkov.graphs.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== 1. ТЕСТ ADJACENCY LIST GRAPH ===");
        testGraph(new AdjacencyListGraph());

        System.out.println("\n=== 2. ТЕСТ ADJACENCY MATRIX GRAPH ===");
        testGraph(new AdjacencyMatrixGraph());

        System.out.println("\n=== 3. ТЕСТ INCIDENCE MATRIX GRAPH ===");
        testGraph(new IncidenceMatrixGraph());

        System.out.println("\n=== 4. ТЕСТ РАСШИРЕНИЯ МАТРИЦЫ (БОЛЕЕ 16 ВЕРШИН) ===");
        testResize(new AdjacencyMatrixGraph());
        testResize(new IncidenceMatrixGraph());

        System.out.println("\n Все тесты завершены успешно!");
    }

    private static void testGraph(Graph graph) {
        Vertex<String> v1 = new Vertex<>(1, "A");
        Vertex<String> v2 = new Vertex<>(2, "B");
        Vertex<String> v3 = new Vertex<>(3, "C");
        Vertex<String> v4 = new Vertex<>(4, "D");

        // Добавление вершин
        assert graph.addVertex(v1) : "Не удалось добавить v1";
        assert graph.addVertex(v2) : "Не удалось добавить v2";
        assert graph.addVertex(v3) : "Не удалось добавить v3";
        assert graph.addVertex(v4) : "Не удалось добавить v4";
        assert !graph.addVertex(v1) : "Дубликат вершины v1 не должен добавляться!";

        // Добавление рёбер: A -> B, A -> C, B -> D
        assert graph.addEdge(v1, v2) : "Не удалось добавить ребро v1 -> v2";
        assert graph.addEdge(v1, v3) : "Не удалось добавить ребро v1 -> v3";
        assert graph.addEdge(v2, v4) : "Не удалось добавить ребро v2 -> v4";
        assert !graph.addEdge(v1, v2) : "Повторное ребро v1 -> v2 не должно добавляться!";

        // Проверка соседей вершины A (v1)
        List<Vertex<?>> neighborsA = graph.getNeighbors(v1);
        System.out.println("Соседи вершины A (1): " + neighborsA);
        assert neighborsA.size() == 2 && neighborsA.contains(v2) && neighborsA.contains(v3);

        // Удаление ребра A -> C
        assert graph.removeEdge(v1, v3) : "Не удалось удалить ребро v1 -> v3";
        assert !graph.removeEdge(v1, v3) : "Повторное удаление ребра v1 -> v3 должно возвращать false!";
        System.out.println("Соседи A после удаления ребра A->C: " + graph.getNeighbors(v1));

        // Удаление вершины B (v2) вместе с её рёбрами
        assert graph.removeVertex(v2) : "Не удалось удалить вершину v2";
        assert !graph.removeVertex(v2) : "Повторное удаление вершины v2 должно возвращать false!";
        System.out.println("Соседи A после удаления вершины B: " + graph.getNeighbors(v1));
    }

    private static void testResize(Graph graph) {
        System.out.println("Тестируем автоматическое расширение для " + graph.getClass().getSimpleName() + "...");

        // Создаём 20 вершин (начальный размер массива — 16)
        Vertex<String>[] vertices = new Vertex[20];
        for (int i = 0; i < 20; i++) {
            vertices[i] = new Vertex<>(i, "Data_" + i);
            assert graph.addVertex(vertices[i]) : "Ошибка добавления вершины " + i;
        }

        // Соединяем вершину 0 со всеми остальными (19 рёбер)
        for (int i = 1; i < 20; i++) {
            assert graph.addEdge(vertices[0], vertices[i]) : "Ошибка добавления ребра 0 -> " + i;
        }

        assert graph.getNeighbors(vertices[0]).size() == 19 : "Неверное количество соседей у вершины 0";
        System.out.println("Успешно обработано 20 вершин и 19 рёбер!");
    }
}