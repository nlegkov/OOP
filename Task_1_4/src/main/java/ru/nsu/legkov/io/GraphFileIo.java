package ru.nsu.legkov.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import ru.nsu.legkov.Edge;
import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.graphs.Graph;

/** Чтение и запись ориентированного графа в текстовом формате. */
public class GraphFileIo {

    /**
     * Считывает вершины и рёбра из текстового файла.
     *
     * @param <T> тип данных вершины
     * @param graph исходный граф
     * @param path путь к файлу
     * @param parser преобразователь строки в данные вершины
     * @throws IOException если произошла ошибка чтения или записи файла
     */
    public static <T> void load(Graph<T> graph, Path path, Function<String, T> parser)
            throws IOException {
        String[] fileStr = Files.readString(path).split("\n");

        int i = 0;
        int len = fileStr.length;
        String[] str;

        Map<Integer, Vertex<T>> mapVertex = new HashMap<Integer, Vertex<T>>();

        for (; i < len; i++) {
            if (fileStr[i].isEmpty()) {
                break;
            }

            str = fileStr[i].split(";", 2);

            if (str.length != 2) {
                throw new IOException("Не правильны формат");
            }

            int key = Integer.parseInt(str[0]);
            T data = parser.apply(str[1]);
            Vertex<T> vertex = new Vertex<>(key, data);

            if (!graph.addVertex(vertex)) {
                throw new IOException("Такой ключ уже существует: " + key);
            }

            mapVertex.put(key, vertex);
        }

        for (; i < len; i++) {
            if (!fileStr[i].isEmpty()) {
                break;
            }
        }

        for (; i < len; i++) {
            if (fileStr[i].isEmpty()) {
                break;
            }

            str = fileStr[i].split(";");

            if (str.length != 2) {
                throw new IOException("Не правильны формат");
            }

            Vertex<T> vertexFrom = mapVertex.get(Integer.parseInt(str[0]));
            Vertex<T> vertexTo = mapVertex.get(Integer.parseInt(str[1]));

            if (!graph.addEdge(vertexFrom, vertexTo)) {
                throw new IOException(
                        "Ошибка в добавлении ребра: " + vertexFrom + " -> " + vertexTo
                );
            }
        }
    }

    /**
     * Записывает вершины и рёбра в текстовый файл.
     *
     * @param <T> тип данных вершины
     * @param graph исходный граф
     * @param path путь к файлу
     * @param formatter преобразователь данных вершины в строку
     * @throws IOException если произошла ошибка чтения или записи файла
     */
    public static <T> void save(Graph<T> graph, Path path, Function<T, String> formatter)
            throws IOException {
        List<Vertex<T>> vertexList = graph.getVertices();
        List<Edge<T>> edgeList = graph.getEdge();

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Vertex<T> vertex : vertexList) {
                writer.write(vertex.getId() + ";" + formatter.apply(vertex.getData()));
                writer.newLine();
            }

            writer.newLine();

            for (Edge<T> edge : edgeList) {
                writer.write(edge.toString());
                writer.newLine();
            }
        }
    }
}
