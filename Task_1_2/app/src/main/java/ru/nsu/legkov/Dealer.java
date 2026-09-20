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
    public void takeCard(DeckOfCards deck) {
        takeCard(deck, false);
    }

    public void takeCard(DeckOfCards deck, boolean isHidden) {
        if (hand.getScore() < 17) {
            Card card = deck.getCard();
            card.setHand(isHidden);
            hand.addCard(card);
        }
    }

    /**
     * Метод для сброса руки дилера.
     */
    public void resetHand() {
        hand.clearCard();
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

    /**
     * Метод для получения скрытой руки дилера.
     * Первая карта скрыта, остальные карты видны.
     *
     * @return Строковое представление скрытой руки дилера.
     */
    public void openCard() {
        hand.getCards().forEach(card -> card.setHand(false));
    }
}
