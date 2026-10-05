package ru.nsu.legkov.entity;

import ru.nsu.legkov.cards.Card;
import ru.nsu.legkov.coreGame.DeckOfCards;

/**
 * Класс Dealer представляет дилера в игре Blackjack.
 */
public class Dealer {
    private final Hand hand;

    /**
     * Конструктор класса Dealer.
     */
    public Dealer() {
        this.hand = new Hand();
    }

    /**
     * Берет карту, если текущее количество очков меньше 17.
     *
     * @param deck Колода карт.
     */
    public void takeCard(DeckOfCards deck) {
        takeCard(deck, false);
    }

    /**
     * Берет карту с возможностью её скрытия.
     *
     * @param deck   Колода карт.
     * @param isHide Скрывать ли карту.
     */
    public void takeCard(DeckOfCards deck, boolean isHide) {
        if (hand.getScore() < 17) {
            Card card = deck.getCard();
            card.setHide(isHide);
            hand.addCard(card);
        }
    }

    /**
     * Открывает все скрытые карты на руке дилера.
     */
    public void openCard() {
        for (Card card : hand.getCards()) {
            card.setHide(false);
        }
    }

    /**
     * Возвращает очки дилера.
     *
     * @return Сумма очков.
     */
    public int getScore() {
        return hand.getScore();
    }

    /**
     * Возвращает объект руки дилера.
     *
     * @return Рука дилера.
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Сбрасывает карты на руке дилера.
     */
    public void resetHand() {
        hand.clearCard();
    }

    /**
     * Возвращает описание последней взятой дилером карты.
     *
     * @return Строка с описанием карты.
     */
    public String lastCards() {
        return hand.lastCard();
    }

    @Override
    public String toString() {
        return "Рука дилера: " + hand.toString() + " | Очки: " + getScore();
    }
}