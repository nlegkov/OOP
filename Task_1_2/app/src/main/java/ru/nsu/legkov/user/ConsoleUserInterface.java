package ru.nsu.legkov.user;

import java.util.Scanner;

import ru.nsu.legkov.entity.Dealer;
import ru.nsu.legkov.entity.Player;

/**
 * Реализация консольного интерфейса пользователя.
 */
public class ConsoleUserInterface implements UserInterface {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void showWelcomeMessage() {
        System.out.println("Добро пожаловать в игру 21!");
        System.out.println("Правила игры: цель игры -"
                + " набрать сумму очков, как можно ближе к 21, но не превышая его.");
        System.out.println("Карты с 2 по 10 имеют номинальную"
                + " стоимость, валет, дама и король стоят 10 очков, туз "
                + "может стоить 1 или 11 очков.");
        System.out.println("Если сумма очков игрока превышает 21, "
                + "он проигрывает. Если сумма очков дилера превышает 21,"
                + " он проигрывает.");
        System.out.println("Если игрок и дилер набрали одинаковое "
                + "количество очков, объявляется ничья.");
        System.out.println("Игра продолжается до тех пор, "
                + "пока игрок не решит выйти из игры.");
        System.out.println("Удачи!\n-------");
    }

    @Override
    public void showRoundStart(int roundNumber) {
        System.out.println("Раунд "
                + roundNumber
                + "\n-------");
    }

    @Override
    public void showGameState(Player player, Dealer dealer) {
        System.out.println(player);
        System.out.println(dealer);
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public int getPlayerChoice() {
        while (true) {
            System.out.print("\nВведите 1, чтобы взять карту, 0, чтобы остановиться: ");
            String input = scanner.next();
            if (isValidInt(input)) {
                int choice = Integer.parseInt(input);
                if (choice == 0 || choice == 1) {
                    return choice;
                }
            }
            System.out.println("Некорректный ввод. Пожалуйста, введите 0 или 1.");
        }
    }

    @Override
    public boolean askToContinue() {
        while (true) {
            System.out.print("Хотите продолжить игру? (1 - да, 0 - нет): ");
            String input = scanner.next();
            if (isValidInt(input)) {
                int choice = Integer.parseInt(input);
                if (choice == 1) {
                    return true;
                }
                if (choice == 0) {
                    return false;
                }
            }
            System.out.println("Некорректный ввод. Введите 1 (да) или 0 (нет).");
        }
    }

    private boolean isValidInt(String input) {
        for (char c : input.toCharArray()) {
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return !input.isEmpty();
    }
}
