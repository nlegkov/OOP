package ru.nsu.legkov;

import java.util.Scanner;

/**
 * Класс Game представляет игру "21" (Blackjack).
 * Он управляет игровым процессом, включая раздачу карт, обработку ходов игрока и дилера,
 * определение победителя раунда и вывод результатов.
 */
public class Game {
    private int rounds = 1;
    private final Player player = new Player();
    private final Dealer dealer = new Dealer();
    private final DeckOfCards deck = new DeckOfCards();
    private final Scanner scanner = new Scanner(System.in);
    private final GameState gameState = new GameState();

    /**
     * Метод startGame() выводит правила игры и запускает первый раунд.
     */
    public void startGame() {
        System.out.println("Добро пожаловать в игру 21!");
        System.out.println("Правила игры: цель игры - набрать сумму очков, "
                + "как можно ближе к 21, но не превышая его.");
        System.out.println("Карты с 2 по 10 имеют номинальную стоимость, валет, дама"
                + " и король стоят 10 очков, туз "
                + "может стоить 1 или 11 очков.");
        System.out.println("Если сумма очков игрока превышает 21, он"
                + " проигрывает. Если сумма очков дилера "
                + "превышает 21, он проигрывает.");
        System.out.println("Если игрок и дилер набрали одинаковое"
                + " количество очков, объявляется ничья.");
        System.out.println("Игра продолжается до тех пор,"
                + " пока игрок не решит выйти из игры.");
        System.out.println("Удачи!\n-------");

        startRound();
    }

    /**
     * Метод startRound() запускает новый раунд игры, раздает карты игроку и дилеру,
     * обрабатывает ходы игрока и дилера, определяет победителя раунда и выводит результаты.
     */
    public void startRound() {
        gameState.incrementRounds();
        System.out.println("Раунд " + gameState.getNumberOfRounds() + " начался.");

        player.resetHand();
        dealer.resetHand();

        player.takeCard(deck);
        player.takeCard(deck);

        dealer.takeCard(deck);
        dealer.takeCard(deck);

        System.out.println("Дилер раздал карты.");
        System.out.println(player);
        System.out.println(dealer);

        System.out.println("Ваш ход\n-------");

        boolean flag = true;

        while (true) {
            System.out.print("\nВведите 1, чтобы взять карту, 0, чтобы остановиться: ");
            int choice = scanner.nextInt();
            if (choice == 1) {
                player.takeCard(deck);
                System.out.println("Вы открыли карту " + player.lastCards());
                System.out.println(player);
                System.out.println(dealer);

                if (player.getScore() > 21) {
                    dealer.addWin();
                    System.out.println("Вы проиграли. Ваш счет: " + player.getScore()
                            + ", счет дилера: "
                            + dealer.getScore());
                    flag = false;
                    break;
                }
            } else if (choice == 0) {
                System.out.println("Вы остановились.");
                break;
            } else {
                System.out.println("Некорректный ввод. Попробуйте снова.");
            }
        }

        if (flag) {
            dealer.openCard();
            System.out.println(player);
            System.out.println(dealer);
            System.out.println();

            System.out.println("Ход дилера\n-------");

            while (dealer.getScore() < 17) {
                dealer.takeCard(deck);
                System.out.println("Дилер взял карту " + dealer.lastCards());
                System.out.println(player);
                System.out.println(dealer);
            }

            if (dealer.getScore() > 21) {
                player.addWin();
                System.out.println("Дилер проиграл. Ваш счет: " + gameState.getScorePlayer()
                        + ", счет дилера: "
                        + gameState.getScoreDealer());
            } else if (dealer.getScore() > player.getScore()) {
                dealer.addWin();
                System.out.println("Вы проиграли. Ваш счет: " + gameState.getScorePlayer()
                        + ", счет дилера: "
                        + gameState.getScoreDealer());
            } else if (dealer.getScore() < player.getScore()) {
                player.addWin();
                System.out.println("Вы выиграли! Ваш счет: " + gameState.getScorePlayer()
                        + ", счет дилера: "
                        + gameState.getScoreDealer());
            } else {
                System.out.println("Ничья! Ваш счет: " + gameState.getScorePlayer()
                        + ", счет дилера: " + gameState.getScoreDealer());
            }
        }

        System.out.print("Раунд окончен." + gameState.toStringScoreRound());

        System.out.print("Хотите продолжить игру? (1 - да, 0 - нет): ");
        int continueChoice = scanner.nextInt();
        if (continueChoice == 1) {
            System.out.println();
            startRound();
        } else {
            System.out.println("\nИгра окончена." + gameState.toStringScoreEndGame());
        }
    }
}