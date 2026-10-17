package ru.nsu.legkov;

import ru.nsu.legkov.cards.Card;
import ru.nsu.legkov.cards.Rank;
import ru.nsu.legkov.cards.Suit;

/** Вспомогательное создание карт с фиксированной мастью. */
final class TestCards {
    private TestCards() { }

    static Card card(Rank rank) {
        return new Card(Suit.PIKI, rank);
    }
}
