package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Проверяет позиции символов Unicode и чтение на границе буфера. */
class FindUnicodeTest {
    @TempDir
    Path directory;

    private void check(String text, String pattern, List<Long> expected) throws IOException {
        Path file = directory.resolve("unicode.txt");
        Files.writeString(file, text);
        assertEquals(expected, Find.find(file.toString(), pattern));
    }

    @Test
    void searchesDifferentAlphabets() throws IOException {
        check("汉字 Ελληνικά 汉字", "汉字", List.of(0L, 12L));
    }

    @Test
    void countsSupplementaryCharactersAsOnePosition() throws IOException {
        check("😀аб😀аб", "аб", List.of(1L, 4L));
        check("😀😀😀", "😀😀", List.of(0L, 1L));
        check("𐐀𐐁𐐀", "𐐀", List.of(0L, 2L));
    }

    @Test
    void searchesAcrossLineBreaks() throws IOException {
        check("a\r\nb\nc", "\nb", List.of(2L));
    }

    @Test
    void readsSurrogatePairAndMatchAcrossBufferBoundary() throws IOException {
        check("x".repeat(8191) + "😀абра", "😀аб", List.of(8191L));
    }
}
