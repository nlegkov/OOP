package ru.nsu.legkov;

/**
 * Класс Main является точкой входа в программу.
 * Он содержит метод main, который запускает игру.
 */
public class Main {
    /**
     * Main метод начала игры. Создает объект Game и запускает игру.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.startGame();
    }
}