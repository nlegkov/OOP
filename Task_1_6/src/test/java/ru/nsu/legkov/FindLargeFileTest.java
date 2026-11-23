package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Генерирует большой файл во время теста и обрабатывает вхождения по мере обнаружения. */
class FindLargeFileTest {
    @TempDir
    Path directory;

    @Test
    void streamsGeneratedFile() throws IOException {
        Path file = directory.resolve("large.txt");
        int repetitions = 100_000;
        String block = "x".repeat(255) + "😀аб";
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (int i = 0; i < repetitions; i++) {
                writer.write(block);
            }
        }
        long[] count = {0};
        Find.find(file.toString(), "😀аб", position -> {
            assertEquals(count[0] * 258 + 255, position);
            count[0]++;
        });
        assertEquals(repetitions, count[0]);
    }
}
