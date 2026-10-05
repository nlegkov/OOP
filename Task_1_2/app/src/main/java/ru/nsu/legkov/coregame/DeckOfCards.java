package ru.nsu.legkov.coregame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.nsu.legkov.cards.Card;
import ru.nsu.legkov.cards.Rank;
import ru.nsu.legkov.cards.Suit;

/**
 * Класс DeckOfCards представляет колоду игральных карт.
 */
public class DeckOfCards {
    private List<Card> cards;

    /**
     * Конструктор класса DeckOfCards. Инициализирует и перемешивает колоду.
     */
    public DeckOfCards() {
        newDeck();
    }

    /**
     * Создает новую колоду из 52 карт и перемешивает её.
     */
    public void newDeck() {
        cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards);
    }

    /**
     * Выдает верхнюю карту из колоды. Если колода пуста, пересоздает её.
     *
     * @return Извлеченная карта.
     */
    public Card getCard() {
        if (cards.isEmpty()) {
            newDeck();
        }
        return cards.remove(cards.size() - 1);
    }
}