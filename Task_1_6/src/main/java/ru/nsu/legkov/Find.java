package ru.nsu.legkov;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.LongConsumer;

/**
 * Ищет подстроку в файлах UTF-8 алгоритмом Кнута — Морриса — Пратта.
 */
public final class Find {
    private Find() {
    }

    private static int[] prefixFunction(int[] pattern) {
        int[] pi = new int[pattern.length];

        for (int i = 1; i < pattern.length; i++) {
            int j = pi[i - 1];

            while (j > 0 && pattern[i] != pattern[j]) {
                j = pi[j - 1];
            }

            if (pattern[i] == pattern[j]) {
                j++;
            }

            pi[i] = j;
        }

        return pi;
    }

    private static int readCodePoint(BufferedReader reader)
            throws IOException {
        int first = reader.read();

        if (first == -1) {
            return -1;
        }

        if (Character.isHighSurrogate((char) first)) {
            int second = reader.read();

            if (second == -1
                    || !Character.isLowSurrogate((char) second)) {
                throw new IOException("Некорректная суррогатная пара");
            }

            return Character.toCodePoint((char) first, (char) second);
        }

        return first;
    }

    /**
     * Передаёт обработчику позиции всех вхождений в порядке появления, включая перекрывающиеся.
     * Файл читается последовательно; объём дополнительной памяти пропорционален
     * длине подстроки без учёта памяти, используемой переданным обработчиком.
     *
     * @param fileName путь к файлу в кодировке UTF-8
     * @param substring непустая искомая подстрока
     * @param onMatch обработчик позиций вхождений, отсчитываемых с нуля по кодовым точкам Unicode
     * @throws IOException при ошибке чтения или декодирования UTF-8
     * @throws IllegalArgumentException если искомая подстрока пуста
     * @throws NullPointerException если один из аргументов равен {@code null}
     */
    public static void find(
            String fileName,
            String substring,
            LongConsumer onMatch
    ) throws IOException {

        int[] pattern = substring.codePoints().toArray();

        if (pattern.length == 0) {
            throw new IllegalArgumentException(
                    "Искомая строка не должна быть пустой"
            );
        }

        int[] pi = prefixFunction(pattern);

        int j = 0;
        long pos = 0;

        try (BufferedReader reader = Files.newBufferedReader(
                Path.of(fileName), StandardCharsets.UTF_8)) {

            int symbol;

            while ((symbol = readCodePoint(reader)) != -1) {

                while (j > 0 && symbol != pattern[j]) {
                    j = pi[j - 1];
                }

                if (symbol == pattern[j]) {
                    j++;
                }

                if (j == pattern.length) {
                    long start = pos - pattern.length + 1;
                    onMatch.accept(start);

                    j = pi[j - 1];
                }

                pos++;
            }
        }
    }

    /**
     * Собирает позиции всех вхождений, включая перекрывающиеся.
     * В памяти хранятся искомая подстрока и полный список позиций. При большом
     * числе вхождений следует использовать перегрузку с обработчиком.
     *
     * @param fileName путь к файлу в кодировке UTF-8
     * @param substring непустая искомая подстрока
     * @return позиции в порядке возрастания, отсчитываемые с нуля по кодовым точкам Unicode
     * @throws IOException при ошибке чтения или декодирования UTF-8
     * @throws IllegalArgumentException если искомая подстрока пуста
     * @throws NullPointerException если один из аргументов равен {@code null}
     */
    public static List<Long> find(
            String fileName,
            String substring
    ) throws IOException {

        List<Long> positions = new ArrayList<>();
        find(fileName, substring, position -> positions.add(position));
        return positions;
    }

}
