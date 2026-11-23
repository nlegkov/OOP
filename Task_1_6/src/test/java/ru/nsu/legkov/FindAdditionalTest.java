package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Дополнительные проверки границ, длинных шаблонов и откатов КМП. */
class FindAdditionalTest {
    @TempDir
    Path directory;

    private void check(String text, String pattern, List<Long> expected) throws IOException {
        Path file = directory.resolve("input.txt");
        Files.writeString(file, text);
        assertEquals(expected, Find.find(file.toString(), pattern));
        List<Long> actual = new ArrayList<>();
        Find.find(file.toString(), pattern, actual::add);
        assertEquals(expected, actual);
    }

    @Test
    void findsPatternLongerThanReaderBuffer() throws IOException {
        String pattern = "ab".repeat(5000) + "c";
        check("xx" + pattern + pattern, pattern, List.of(2L, 10003L));
    }

    @Test
    void doesNotReportUnfinishedMatchAtEnd() throws IOException {
        check("abcab", "abc", List.of(0L));
        check("abababab", "ababac", List.of());
    }

    @Test
    void fallsBackSeveralTimesOnOneCharacter() throws IOException {
        check("abababxababac", "ababac", List.of(7L));
    }

    @Test
    void treatsSpecialCharactersAsPlainText() throws IOException {
        check("a.*[b] a.*[b]", ".*[b]", List.of(1L, 8L));
        check("a\u0000b\u0000b", "\u0000b", List.of(1L, 3L));
        check("AaA", "a", List.of(1L));
    }

    @Test
    void findsMatchAcrossSeveralLines() throws IOException {
        check("ab\ncd\nef\nab\ncd\nef", "b\ncd\ne", List.of(1L, 10L));
    }

    @Test
    void agreesWithSimpleSearchOnRandomUnicodeTexts() throws IOException {
        Random random = new Random(42);
        int[] alphabet = {'a', 'b', 'в', 0x1f600, '\n'};
        for (int attempt = 0; attempt < 200; attempt++) {
            StringBuilder text = new StringBuilder();
            for (int i = 0; i < 80; i++) {
                text.appendCodePoint(alphabet[random.nextInt(alphabet.length)]);
            }
            StringBuilder pattern = new StringBuilder();
            int length = 1 + random.nextInt(6);
            for (int i = 0; i < length; i++) {
                pattern.appendCodePoint(alphabet[random.nextInt(alphabet.length)]);
            }
            List<Long> expected = new ArrayList<>();
            int from = 0;
            int index;
            while ((index = text.indexOf(pattern.toString(), from)) >= 0) {
                expected.add((long) text.codePointCount(0, index));
                from = index + 1;
            }
            check(text.toString(), pattern.toString(), expected);
        }
    }
}
