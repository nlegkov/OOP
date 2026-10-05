package ru.nsu.legkov.User;

import ru.nsu.legkov.entity.Dealer;
import ru.nsu.legkov.entity.Player;

public interface UserInterface {
    void showWelcomeMessage();
    void showRoundStart(int roundNumber);
    void showGameState(Player player, Dealer dealer);
    void showMessage(String message);

    /**
     * Запрашивает у пользователя выбор: 1 — взять карту, 0 — остановиться.
     */
    int getPlayerChoice();

    /**
     * Запрашивает согласие на следующий раунд (true — продолжать, false — выйти).
     */
    boolean askToContinue();
}