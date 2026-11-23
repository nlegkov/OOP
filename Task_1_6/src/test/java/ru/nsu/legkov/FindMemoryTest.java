package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Проверяет поиск в файле, превышающем лимит памяти отдельной JVM. */
class FindMemoryTest {
    @TempDir
    Path directory;

    @Test
    void searchesFileLargerThanHeap() throws Exception {
        Path file = directory.resolve("large.txt");
        byte[] block = new byte[8192];
        Arrays.fill(block, (byte) 'a');
        int blockCount = 8192;
        try (OutputStream output = Files.newOutputStream(file)) {
            for (int i = 0; i < blockCount; i++) {
                output.write(block);
            }
            output.write(new byte[]{'E', 'N', 'D'});
        }
        long letters = (long) block.length * blockCount;
        String classes = Path.of(Find.class.getProtectionDomain().getCodeSource()
                .getLocation().toURI()).toString();
        String testClasses = Path.of(MemoryProbe.class.getProtectionDomain().getCodeSource()
                .getLocation().toURI()).toString();
        String java = Path.of(System.getProperty("java.home"), "bin", "java").toString();
        Path log = directory.resolve("process.log");
        Process process = new ProcessBuilder(java, "-Xmx16m", "-cp",
                classes + File.pathSeparator + testClasses,
                MemoryProbe.class.getName(), file.toString(), Long.toString(letters))
                .redirectErrorStream(true).redirectOutput(log.toFile()).start();
        try {
            assertTrue(process.waitFor(90, TimeUnit.SECONDS), "Поиск не завершился за 90 секунд");
            assertEquals(0, process.exitValue(), Files.readString(log));
        } finally {
            if (process.isAlive()) {
                process.destroyForcibly();
                process.waitFor();
            }
        }
    }

    /** Запускается отдельно, без JUnit и без сохранения миллионов позиций. */
    public static class MemoryProbe {
        /** Проверяет плотные совпадения и единственное вхождение в конце файла. */
        public static void main(String[] args) throws Exception {
            Path file = Path.of(args[0]);
            long letters = Long.parseLong(args[1]);
            if (Files.size(file) <= Runtime.getRuntime().maxMemory()) {
                throw new AssertionError("Файл должен превышать доступную память JVM");
            }
            long[] count = {0};
            Find.find(file.toString(), "aaa", position -> {
                if (position != count[0]) {
                    throw new AssertionError("Неверная позиция: " + position);
                }
                count[0]++;
            });
            if (count[0] != letters - 2) {
                throw new AssertionError("Неверное число вхождений: " + count[0]);
            }
            if (!Find.find(file.toString(), "END").equals(List.of(letters))) {
                throw new AssertionError("Не найдено вхождение в конце файла");
            }
        }
    }
}
