package ru.nsu.legkov;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckOfCards {
    private final List<Card> cards = new ArrayList<>();

    /**
     * Конструктор класса DeckOfCards.
     * Создает новую колоду карт и перемешивает её.
     */
    public DeckOfCards() {
        newDeck();
    }

    /**
     * Перемешивает колоду карт.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Получает карту из колоды.
     * Если колода пуста, возвращает null.
     *
     * @return Карта из колоды или null, если колода пуста.
     */
    public Card getCard() {
        if (cards.isEmpty()) {
            return null;
        }

        return cards.remove(0);
    }

    /**
     * Создает новую колоду карт, состоящую из 52 карт (4 масти по 13 рангов).
     * После создания колоды, она перемешивается.
     */
    public void newDeck() {
        cards.clear();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }

        shuffle();
    }
}