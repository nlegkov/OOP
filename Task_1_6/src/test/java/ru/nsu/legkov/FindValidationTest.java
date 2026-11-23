package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Проверяет некорректные аргументы и ошибки чтения входных данных. */
class FindValidationTest {
    @TempDir
    Path directory;

    @Test
    void rejectsEmptyPattern() {
        assertThrows(IllegalArgumentException.class,
                () -> Find.find(directory.resolve("unused").toString(), ""));
    }

    @Test
    void rejectsNullArguments() {
        assertThrows(NullPointerException.class, () -> Find.find(null, "a"));
        assertThrows(NullPointerException.class, () -> Find.find("unused", null));
        assertThrows(NullPointerException.class, () -> Find.find("unused", "a", null));
    }

    @Test
    void reportsMissingFile() {
        assertThrows(IOException.class,
                () -> Find.find(directory.resolve("missing").toString(), "a"));
    }

    @Test
    void reportsMalformedUtf8() throws IOException {
        Path file = directory.resolve("invalid.txt");
        Files.write(file, new byte[]{(byte) 0xc3, 0x28});
        assertThrows(IOException.class, () -> Find.find(file.toString(), "a"));
    }
}
