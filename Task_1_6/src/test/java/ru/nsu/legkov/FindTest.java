package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Проверяет обычные вхождения и переходы по префиксной функции алгоритма КМП. */
class FindTest {
    @TempDir
    Path directory;

    private void check(String text, String pattern, List<Long> expected) throws IOException {
        Path file = directory.resolve("input.txt");
        Files.writeString(file, text);
        assertEquals(expected, Find.find(file.toString(), pattern));
        List<Long> streamed = new ArrayList<>();
        Find.find(file.toString(), pattern, streamed::add);
        assertEquals(expected, streamed);
    }

    @Test
    void findsAssignmentExample() throws IOException {
        check("абракадабра", "бра", List.of(1L, 8L));
    }

    @Test
    void findsOverlappingMatches() throws IOException {
        check("aaaaa", "aaa", List.of(0L, 1L, 2L));
    }

    @Test
    void fallsBackAfterPartialMatch() throws IOException {
        check("ababababac", "ababac", List.of(4L));
    }

    @Test
    void handlesBoundariesAndSingleCharacters() throws IOException {
        check("abcabc", "abc", List.of(0L, 3L));
        check("abc", "abc", List.of(0L));
        check("aba", "a", List.of(0L, 2L));
    }

    @Test
    void returnsEmptyWhenNoMatchIsPossible() throws IOException {
        check("", "a", List.of());
        check("ab", "abc", List.of());
        check("abc", "z", List.of());
    }
}
