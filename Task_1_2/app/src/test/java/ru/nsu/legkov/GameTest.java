package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.nsu.legkov.coregame.Game;

/** Тесты запуска и завершения игры. */
class GameTest {
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    @AfterEach
    void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    @Test
    void gameCanRunOneCompleteRoundFromConsole() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
        System.setIn(new ByteArrayInputStream("0\n0\n".getBytes(StandardCharsets.UTF_8)));
        new Game().startGame();
        String shown = output.toString(StandardCharsets.UTF_8);
        assertTrue(shown.contains("Раунд 1"));
        assertTrue(shown.contains("Раунд окончен"));
        assertTrue(shown.contains("Игра окончена"));
    }
}
