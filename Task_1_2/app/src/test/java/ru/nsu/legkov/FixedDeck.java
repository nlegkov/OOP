package ru.nsu.legkov;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import ru.nsu.legkov.cards.Card;
import ru.nsu.legkov.coregame.DeckOfCards;

/** Предсказуемая колода для тестирования игрового раунда. */
final class FixedDeck extends DeckOfCards {
    private final Queue<Card> cards;

    FixedDeck(Card... cards) {
        this.cards = new ArrayDeque<>(Arrays.asList(cards));
    }

    @Override
    public Card getCard() {
        Card next = cards.poll();
        if (next == null) {
            throw new AssertionError("Раунд запросил лишнюю карту");
        }
        return next;
    }

    int remaining() {
        return cards.size();
    }
}
