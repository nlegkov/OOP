package ru.nsu.legkov.coregame;

import ru.nsu.legkov.entity.Dealer;
import ru.nsu.legkov.entity.Player;
import ru.nsu.legkov.user.ConsoleUserInterface;
import ru.nsu.legkov.user.UserInterface;


/**
 * Класс Game представляет игру "21" (Blackjack).
 * Он управляет игровым процессом, включая раздачу карт, обработку ходов игрока и дилера,
 * определение победителя раунда и вывод результатов.
 */
public class Game {
    private final Player player = new Player();
    private final Dealer dealer = new Dealer();
    private DeckOfCards deck;
    private final GameState gameState = new GameState();
    private final UserInterface ui;

    public Game() {
        this.ui = new ConsoleUserInterface(); // При необходимости можно передать через конструктор
    }

    /**
     * Метод startGame() выводит правила игры и запускает первый раунд.
     */
    public void startGame() {
        ui.showWelcomeMessage();

        int deckCount = ui.getDeckCount();
        deck = new DeckOfCards(deckCount);

        boolean continueGame = true;
        while (continueGame) {
            Round round = new Round(gameState, player, dealer, deck, ui);
            round.play();

            continueGame = ui.askToContinue();
        }

        ui.showMessage("\nИгра окончена. " + gameState.toStringScoreEndGame());
    }
}