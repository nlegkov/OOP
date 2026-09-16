package ru.nsu.legkov;

/**
 * Класс Player представляет игрока в игре.
 * Он содержит руку игрока и количество побед.
 */
public class Player {
    private final Hand hand = new Hand();
    private int scoreWins = 0;

    /**
     * Метод для взятия карты игроком.
     * Игрок может взять карту, если его текущий счет меньше 21.
     *
     * @param cards Колода карт, из которой берется карта.
     */
    public void takeCard(DeckOfCards cards) {
        if (hand.getScore() < 21) {
            hand.getCard(cards);
        }
    }

    /**
     * Метод для сброса руки игрока.
     */
    public void resetHand() {
        hand.clearCard();
    }

    /**
     * Метод для увеличения количества побед игрока.
     */
    public void addWin() {
        scoreWins++;
    }

    /**
     * Метод для получения количества побед игрока.
     *
     * @return Количество побед игрока.
     */
    public int getScoreWins() {
        return scoreWins;
    }

    /**
     * Метод для получения текущего счета игрока.
     *
     * @return Текущий счет игрока.
     */
    public int getScore() {
        return hand.getScore();
    }

    /**
     * Метод для получения последней карты игрока.
     *
     * @return Строковое представление последней карты игрока.
     */
    public String lastCards() {
        return hand.lastCard();
    }

    /**
     * Переопределенный метод toString() для представления руки игрока и его счета.
     *
     * @return Строковое представление руки игрока и его счета.
     */
    @Override
    public String toString() {
        return "Рука игрока: [" + hand.toString() + "] -> " + getScore();
    }
}
