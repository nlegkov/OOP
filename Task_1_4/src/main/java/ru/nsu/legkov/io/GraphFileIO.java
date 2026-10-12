package ru.nsu.legkov.io;

import ru.nsu.legkov.Edge;
import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.graphs.Graph;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GraphFileIO {

    public static <T> void load(
            Graph<T> graph,
            Path path,
            Function<String, T> parser
    ) throws IOException {
        String[] fileStr = Files.readString(path).split("\n");

        int i = 0;
        int len = fileStr.length;
        String[] str;

        Map<Integer, Vertex<T>> mapVertex = new HashMap<Integer, Vertex<T>>();

        for (; i < len; i++) {
            if (fileStr[i].isEmpty()) {
                break;
            }

            str = fileStr[i].split(";");

            if (str.length != 2) {
                throw new IOException("Не правильны формат");
            }

            int key = Integer.parseInt(str[0]);
            T data = parser.apply(str[1]);
            Vertex<T> vertex = new Vertex<>(key, data);

            if (!graph.addVertex(vertex)) {
                throw new IOException("Такой ключ уже существует: " + key);
            }
        }

        for (; i <= len; i++) {
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

            Vertex<T> vartex_1 = mapVertex.get(Integer.parseInt(str[0]));
            Vertex<T> vartex_2 = mapVertex.get(Integer.parseInt(str[0]));

            if (!graph.addEdge(vartex_1, vartex_2)) {
                throw new IOException("Ошибка в добавлении ребра: " + vartex_1 + " -> " + vartex_2);
            }
        }
    }

    public static <T> void save(
            Graph<T> graph,
            Path path,
            Function<T, String> formatter
    ) throws IOException {
        List<Vertex<T>> vertexList = graph.getVertices();
        List<Edge<T>> edgeList = graph.getEdge();

        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Vertex<T> vertex : vertexList) {
                writer.write(vertex.toString());
                writer.newLine();
            }

            for (Edge<T> edge : edgeList) {
                writer.newLine();
                writer.write(edge.toString());
            }
        }
    }
}