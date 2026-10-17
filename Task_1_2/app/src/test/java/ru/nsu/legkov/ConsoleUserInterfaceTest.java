package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.nsu.legkov.entity.Dealer;
import ru.nsu.legkov.entity.Player;
import ru.nsu.legkov.user.ConsoleUserInterface;

/** Тесты консольного интерфейса. */
class ConsoleUserInterfaceTest {
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    @AfterEach
    void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private static ConsoleUserInterface consoleWithInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
        return new ConsoleUserInterface();
    }

    @Test
    void consoleReadsValidChoicesAndRetriesInvalidInput() {
        ConsoleUserInterface ui = consoleWithInput("abc\n5\n1\n-1\n0\n");
        assertEquals(1, ui.getPlayerChoice());
        assertEquals(0, ui.getPlayerChoice());
    }

    @Test
    void consoleReadsContinueAnswersAndRetriesInvalidInput() {
        ConsoleUserInterface ui = consoleWithInput("hello\n2\n1\nno\n0\n");
        assertTrue(ui.askToContinue());
        assertFalse(ui.askToContinue());
    }

    @Test
    void consoleRendersWelcomeRoundAndMessages() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
        ConsoleUserInterface ui = consoleWithInput("0\n");
        ui.showWelcomeMessage();
        ui.showRoundStart(2);
        ui.showMessage("Проверка сообщения");
        ui.showGameState(new Player(), new Dealer());
        String shown = output.toString(StandardCharsets.UTF_8);
        assertTrue(shown.contains("Добро пожаловать"));
        assertTrue(shown.contains("Раунд 2"));
        assertTrue(shown.contains("Проверка сообщения"));
        assertTrue(shown.contains("Рука игрока"));
        assertTrue(shown.contains("Рука дилера"));
    }
}
