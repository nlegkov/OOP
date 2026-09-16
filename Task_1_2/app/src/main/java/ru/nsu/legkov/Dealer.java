package ru.nsu.legkov;

/**
 * Класс Dealer представляет дилера в игре.
 * Он содержит руку дилера и количество побед.
 */
public class Dealer {
    private final Hand hand = new Hand();
    private int scoreWins = 0;

    /**
     * Метод для взятия карты дилером.
     * Дилер берет карту, если его текущий счет меньше 17.
     *
     * @param cards Колода карт, из которой берется карта.
     */
    public void takeCard(DeckOfCards cards) {
        if (hand.getScore() < 17) {
            hand.getCard(cards);
        }
    }

    /**
     * Метод для получения скрытой руки дилера.
     * Возвращает строковое представление руки дилера, где первая карта видна, а вторая скрыта.
     *
     * @return Строковое представление скрытой руки дилера.
     */
    public String getHiddenHandString() {
        if (hand.getCards().isEmpty()) {
            return "[]";
        }

        return "Рука дилера: [" + hand.getCards().get(0) + ", <закрытая карта>]";
    }

    /**
     * Метод для сброса руки дилера.
     */
    public void resetHand() {
        hand.clearCard();
    }

    /**
     * Метод для увеличения количества побед дилера.
     */
    public void addWin() {
        scoreWins++;
    }

    /**
     * Метод для получения количества побед дилера.
     *
     * @return Количество побед дилера.
     */
    public int getScoreWins() {
        return scoreWins;
    }

    /**
     * Метод для получения текущего счета дилера.
     *
     * @return Текущий счет дилера.
     */
    public int getScore() {
        return hand.getScore();
    }

    /**
     * Метод для получения последней карты дилера.
     *
     * @return Строковое представление последней карты дилера.
     */
    public String lastCards() {
        return hand.lastCard();
    }

    /**
     * Переопределенный метод toString() для представления руки дилера и его счета.
     *
     * @return Строковое представление руки дилера и его счета.
     */
    @Override
    public String toString() {
        return "Рука дилера: [" + hand.toString() + "] -> " + getScore();
    }
}
