package ru.nsu.legkov;

public class Main {
    /**
     * Main метод начала игры. Создает объект Game и запускает игру.
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.startGame();
    }
}