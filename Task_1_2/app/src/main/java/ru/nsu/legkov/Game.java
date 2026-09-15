package ru.nsu.legkov;

import java.util.Scanner;

public class Game {
    int rounds = 1;
    Player player = new Player();
    Dealer dealer = new Dealer();
    DeckOfCards deck = new DeckOfCards();
    Scanner scanner = new Scanner(System.in);

    public void startGame() {
        System.out.println("Добро пожаловать в игру 21!");
        System.out.println("Правила игры: цель игры - набрать сумму очков, как можно ближе к 21, но не превышая его.");
        System.out.println("Карты с 2 по 10 имеют номинальную стоимость, валет, дама и король стоят 10 очков, туз может стоить 1 или 11 очков.");
        System.out.println("Если сумма очков игрока превышает 21, он проигрывает. Если сумма очков дилера превышает 21, он проигрывает.");
        System.out.println("Если игрок и дилер набрали одинаковое количество очков, объявляется ничья.");
        System.out.println("Игра продолжается до тех пор, пока игрок не решит выйти из игры.");
        System.out.println("Удачи!\n-------");

        startRound();
    }

    public void startRound() {
        System.out.println("Раунд " + rounds++);

        player.resetHand();
        dealer.resetHand();

        player.takeCard(deck);
        player.takeCard(deck);

        dealer.takeCard(deck);
        dealer.takeCard(deck);

        System.out.println("Дилер раздал карты.");
        System.out.println(player);
        System.out.println(dealer.getHiddenHandString());

        System.out.println("Ваш ход\n-------");

        boolean flag = true;

        while (true) {
            System.out.print("\nВведите 1, чтобы взять карту, 0, чтобы остановиться: ");
            int choice = scanner.nextInt();
            if (choice == 1) {
                player.takeCard(deck);
                System.out.println("Вы открыли карту " + player.lastCards());
                System.out.println(player);
                System.out.println(dealer.getHiddenHandString());

                if (player.getScore() > 21) {
                    dealer.addWin();
                    System.out.println("Вы проиграли. Ваш счет: " + player.getScore() + ", счет дилера: " + dealer.getScore());
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

        System.out.println("Дилер открыл скрытую карту " + dealer.lastCards());
        System.out.println(player);
        System.out.println(dealer.getHiddenHandString());
        System.out.println();

        if (flag) {
            System.out.println("Ход дилера\n-------");

            while (dealer.getScore() < 17) {
                dealer.takeCard(deck);
                System.out.println("Дилер взял карту " + dealer.lastCards());
                System.out.println(player);
                System.out.println(dealer);
            }

            if (dealer.getScore() > 21) {
                player.addWin();
                System.out.println("Дилер проиграл. Ваш счет: " + player.getScore() + ", счет дилера: " + dealer.getScore());
            } else if (dealer.getScore() > player.getScore()) {
                dealer.addWin();
                System.out.println("Вы проиграли. Ваш счет: " + player.getScore() + ", счет дилера: " + dealer.getScore());
            } else if (dealer.getScore() < player.getScore()) {
                player.addWin();
                System.out.println("Вы выиграли! Ваш счет: " + player.getScore() + ", счет дилера: " + dealer.getScore());
            } else {
                System.out.println("Ничья! Ваш счет: " + player.getScore() + ", счет дилера: " + dealer.getScore());
            }
        }

        System.out.print("Раунд окончен. Счет " + player.getScoreWins() + ":" + dealer.getScoreWins());
        if (player.getScoreWins() > dealer.getScoreWins()) {
            System.out.println(" - Вы лидируете!");
        } else if (player.getScoreWins() < dealer.getScoreWins()) {
            System.out.println(" - Дилер лидирует!");
        } else {
            System.out.println(" - Ничья!");
        }

        System.out.print("Хотите продолжить игру? (1 - да, 0 - нет): ");
        int continueChoice = scanner.nextInt();
        if (continueChoice == 1) {
            startRound();
        } else {
            System.out.println("Игра окончена. Ваши победы: " + player.getScoreWins() + ", победы дилера: " + dealer.getScoreWins());
        }
    }
}