package ru.nsu.legkov;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс DeckOfCards представляет колоду карт.
 * Колода состоит из 52 карт (4 масти по 13 рангов).
 */
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
     * Если колода пуста, создается новая колода и перемешивается.
     *
     * @return Карта из колоды.
     */
    public Card getCard() {
        if (cards.isEmpty()) {
            System.out.println("В колоде больше нет карт."
                    + " Дилер взял новую колоду и тщательно перемешал её.");
            newDeck();
            return cards.remove(0);
        } else {
            return cards.remove(0);
        }
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