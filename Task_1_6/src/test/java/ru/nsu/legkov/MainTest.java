package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Проверяет обработку аргументов командной строки и вывод результатов. */
class MainTest {
    @TempDir
    Path directory;

    @Test
    void rejectsInvalidArguments() {
        assertThrows(IllegalArgumentException.class, () -> Main.main(new String[]{}));
        assertThrows(IllegalArgumentException.class, () -> Main.main(new String[]{"file"}));
        assertThrows(IllegalArgumentException.class,
                () -> Main.main(new String[]{"file", "a", "extra"}));
        assertThrows(IllegalArgumentException.class,
                () -> Main.main(new String[]{"file", ""}));
    }

    @Test
    void printsMatchesAndEmptyResult() throws IOException {
        Path file = directory.resolve("file with spaces.txt");
        Files.writeString(file, "абракадабра");
        assertEquals("[1, 8]" + System.lineSeparator(), output(file, "бра"));
        assertEquals("[]" + System.lineSeparator(), output(file, "z"));
    }

    private String output(Path file, String pattern) throws IOException {
        PrintStream previous = System.out;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (PrintStream capture = new PrintStream(bytes, true, StandardCharsets.UTF_8)) {
            System.setOut(capture);
            Main.main(new String[]{file.toString(), pattern});
        } finally {
            System.setOut(previous);
        }
        return bytes.toString(StandardCharsets.UTF_8);
    }
}
