package ru.nsu.legkov;

import java.util.ArrayDeque;
import java.util.Queue;
import ru.nsu.legkov.entity.Dealer;
import ru.nsu.legkov.entity.Player;
import ru.nsu.legkov.user.UserInterface;

/** Консоль без ввода-вывода с фиксированными ответами игрока. */
final class TestUi implements UserInterface {
    private final Queue<Integer> choices = new ArrayDeque<>();

    TestUi(int... choices) {
        for (int choice : choices) {
            this.choices.add(choice);
        }
    }

    @Override
    public void showWelcomeMessage() { }

    @Override
    public void showRoundStart(int roundNumber) { }

    @Override
    public void showGameState(Player player, Dealer dealer) { }

    @Override
    public void showMessage(String message) { }

    @Override
    public int getPlayerChoice() {
        Integer choice = choices.poll();
        if (choice == null) {
            throw new AssertionError("Раунд запросил лишний выбор игрока");
        }
        return choice;
    }

    @Override
    public boolean askToContinue() {
        return false;
    }
}
