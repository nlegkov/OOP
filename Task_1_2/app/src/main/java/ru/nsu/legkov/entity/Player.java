package ru.nsu.legkov.entity;

import ru.nsu.legkov.coregame.DeckOfCards;

/**
 * Класс Player представляет игрока.
 */
public class Player {
    private final Hand hand;

    /**
     * Конструктор класса Player.
     */
    public Player() {
        this.hand = new Hand();
    }

    /**
     * Добавляет карту в руку игрока.
     *
     * @param deck Колода карт.
     */
    public void takeCard(DeckOfCards deck) {
        hand.addCard(deck.getCard());
    }

    /**
     * Возвращает очки игрока.
     *
     * @return Сумма очков.
     */
    public int getScore() {
        return hand.getScore();
    }

    /**
     * Возвращает объект руки.
     *
     * @return Рука игрока.
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Сбрасывает карты в руке игрока.
     */
    public void resetHand() {
        hand.clearCard();
    }

    /**
     * Возвращает описание последней взятой карты.
     *
     * @return Строка с описанием карты.
     */
    public String lastCards() {
        return hand.lastCard();
    }

    @Override
    public String toString() {
        return "Рука игрока: " + hand.toString() + " | Очки: " + getScore();
    }
}