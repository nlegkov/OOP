package ru.nsu.legkov.io;

import ru.nsu.legkov.Vertex;
import ru.nsu.legkov.graphs.Graph;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

        for (; i < len; i++) {
            if (fileStr[i].isEmpty()) {
                break;
            }

            str = fileStr[i].split(";");
            T data = parser.apply(str[0]);
            graph.addVertex(new Vertex<>(data));
            data = parser.apply(str[1]);
            graph.addVertex(new Vertex<>(data));
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


        }
    }

    public static <T> void save(
            Graph<T> graph,
            Path path,
            Function<T, String> formatter
    ) throws IOException {
        // Получение вершин/рёбер и запись в файл
    }
}