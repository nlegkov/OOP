package ru.nsu.legkov.coreGame;

import ru.nsu.legkov.entity.Dealer;
import ru.nsu.legkov.entity.Player;
import ru.nsu.legkov.User.UserInterface;

import java.util.Scanner;

public class Round {

    private final GameState gameState;
    private final Player player;
    private final Dealer dealer;
    private final DeckOfCards deck;
    private final UserInterface ui;


    public Round(GameState gameState, Player player, Dealer dealer, DeckOfCards deck, UserInterface ui) {
        this.gameState = gameState;
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
        this.ui = ui;
    }

    public void play() {
        gameState.incrementRounds();
        ui.showRoundStart(gameState.getNumberOfRounds());

        player.resetHand();
        dealer.resetHand();

        // Начальная раздача
        player.takeCard(deck);
        player.takeCard(deck);
        dealer.takeCard(deck);
        dealer.takeCard(deck, true);

        ui.showMessage("Дилер раздал карты.");
        ui.showGameState(player, dealer);

        // Ход игрока
        ui.showMessage("Ваш ход\n-------");
        boolean playerBusted = false;

        while (true) {
            int choice = ui.getPlayerChoice();
            if (choice == 1) {
                player.takeCard(deck);
                ui.showMessage("Вы открыли карту " + player.lastCards());
                ui.showGameState(player, dealer);

                if (player.getScore() > 21) {
                    gameState.incrementScoreDealer();
                    ui.showMessage("Вы проиграли (перебор). Ваш счет: " + player.getScore()
                            + ", счет дилера: " + dealer.getScore());
                    playerBusted = true;
                    break;
                }
            } else {
                ui.showMessage("Вы остановились.");
                break;
            }
        }

        // Ход дилера (если игрок не сгорел)
        if (!playerBusted) {
            dealer.openCard();
            ui.showGameState(player, dealer);
            ui.showMessage("\nХод дилера\n-------");

            while (dealer.getScore() < 17) {
                dealer.takeCard(deck);
                ui.showMessage("Дилер взял карту " + dealer.lastCards());
                ui.showGameState(player, dealer);
            }

            determineWinner();
        }

        ui.showMessage("Раунд окончен. " + gameState.toStringScoreRound());
    }

    private void determineWinner() {
        int playerScore = player.getScore();
        int dealerScore = dealer.getScore();

        if (dealerScore > 21) {
            gameState.incrementScorePlayer();
            ui.showMessage("Дилер проиграл (перебор)! Ваш счет: " + gameState.getScorePlayer()
                    + ", счет дилера: " + gameState.getScoreDealer());
        } else if (dealerScore > playerScore) {
            gameState.incrementScoreDealer();
            ui.showMessage("Вы проиграли. Ваш счет: " + gameState.getScorePlayer()
                    + ", счет дилера: " + gameState.getScoreDealer());
        } else if (dealerScore < playerScore) {
            gameState.incrementScorePlayer();
            ui.showMessage("Вы выиграли! Ваш счет: " + gameState.getScorePlayer()
                    + ", счет дилера: " + gameState.getScoreDealer());
        } else {
            ui.showMessage("Ничья! Ваш счет: " + gameState.getScorePlayer()
                    + ", счет дилера: " + gameState.getScoreDealer());
        }
    }
}