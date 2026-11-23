package ru.nsu.legkov;

import java.io.IOException;
import java.util.List;

/** Точка входа для поиска в файлах UTF-8 из командной строки. */
public final class Main {
    private Main() {
    }

    /**
     * Выводит позиции по мере обнаружения вхождений без сохранения списка результатов.
     *
     * @param args путь к файлу и непустая искомая подстрока
     * @throws IOException при ошибке чтения или декодирования UTF-8
     * @throws IllegalArgumentException если переданы некорректные аргументы
     */
    public static void main(String[] args) throws IOException {
        String fileName = args[0];
        String substring = args[1];

        List<Long> positions = Find.find(fileName, substring);
        System.out.println(positions);
    }
}