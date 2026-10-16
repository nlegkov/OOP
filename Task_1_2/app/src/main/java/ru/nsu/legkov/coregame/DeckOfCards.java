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
    private final int deckCount;
    private List<Card> cards;

    private final int deckCount;
    private List<Card> cards;

    /**
     * Создаёт одну стандартную колоду.
     */
    public DeckOfCards() {
        this(1);
    }

    /**
     * Создаёт указанное количество стандартных колод.
     *
     * @param deckCount количество колод, больше нуля
     */
    public DeckOfCards(int deckCount) {
        if (deckCount <= 0) {
            throw new IllegalArgumentException(
                    "Количество колод должно быть положительным"
            );
        }

        this.deckCount = deckCount;
        newDeck();
    }

    /**
     * Создаёт и перемешивает общую колоду карт.
     */
    public void newDeck() {
        cards = new ArrayList<>();

        for (int i = 0; i < deckCount; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(suit, rank));
                }
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