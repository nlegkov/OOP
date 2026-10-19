package ru.nsu.legkov.graphs;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import ru.nsu.legkov.Edge;
import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.sort.DfsSort;
import ru.nsu.legkov.sort.KahnSort;
import ru.nsu.legkov.sort.TopologicalSort;

/** Интерфейс ориентированного графа с параметризованными данными вершин. */
public interface Graph<T> {
    TopologicalSort[] DEFAULT_SORTERS = new TopologicalSort[1];

    /**
     * Добавляет вершину в граф.
     *
     * @param vertex вершина графа
     *
     * @return true при успешном выполнении операции, иначе false
     */
    boolean addVertex(Vertex<T> vertex);

    /**
     * Удаляет вершину и связанные с ней рёбра.
     *
     * @param vertex вершина графа
     *
     * @return true при успешном выполнении операции, иначе false
     */
    boolean removeVertex(Vertex<T> vertex);

    /**
     * Добавляет ориентированное ребро.
     *
     * @param from начальная вершина ребра
     * @param to конечная вершина ребра
     *
     * @return true при успешном выполнении операции, иначе false
     */
    boolean addEdge(Vertex<T> from, Vertex<T> to);

    /**
     * Удаляет ориентированное ребро.
     *
     * @param from начальная вершина ребра
     * @param to конечная вершина ребра
     *
     * @return true при успешном выполнении операции, иначе false
     */
    boolean removeEdge(Vertex<T> from, Vertex<T> to);

    /**
     * Возвращает все вершины графа.
     *
     * @return список элементов
     */
    List<Vertex<T>> getVertices();

    /**
     * Возвращает все ориентированные рёбра графа.
     *
     * @return список элементов
     */
    List<Edge<T>> getEdge();

    /**
     * Возвращает вершины, в которые ведут исходящие рёбра.
     *
     * @param vertex вершина графа
     *
     * @return список элементов
     */
    List<Vertex<T>> getNeighbors(Vertex<T> vertex);

    /**
     * Загружает граф из файла установленного формата.
     *
     * @param path путь к файлу
     * @param parser преобразователь строки в данные вершины
     *
     * @throws IOException если произошла ошибка чтения или записи файла
     */
    void loadFromFile(Path path, Function<String, T> parser) throws IOException;

    /**
     * Сохраняет граф в файл установленного формата.
     *
     * @param path путь к файлу
     * @param formatter преобразователь данных вершины в строку
     *
     * @throws IOException если произошла ошибка чтения или записи файла
     */
    void saveToFile(Path path, Function<T, String> formatter) throws IOException;

    /**
     *
     */
    default void setSortTopologically(TopologicalSort<T> sorter) {
        DEFAULT_SORTERS[0] = sorter;
    }

    /**
     * Выполняет топологическую сортировку графа.
     *
     * @return список элементов
     */
    default List<Vertex<T>> sortTopologically() {
        if (DEFAULT_SORTERS[0] == null) {
            throw new IllegalStateException("Sorter is not set");
        }
        return DEFAULT_SORTERS[0].sort(this);
    }
}
