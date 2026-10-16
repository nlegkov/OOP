package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import ru.nsu.legkov.cards.Card;
import ru.nsu.legkov.coregame.DeckOfCards;

/** Тесты формирования и обновления колоды. */
class DeckOfCardsTest {
    @Test
    void deckContainsExactly52DistinctCardsAndRestarts() {
        DeckOfCards deck = new DeckOfCards();
        Set<String> seen = new HashSet<>();
        for (int i = 0; i < 52; i++) {
            Card next = deck.getCard();
            assertNotNull(next);
            assertTrue(seen.add(next.getSuit().name() + next.getRank().name()));
        }
        assertEquals(52, seen.size());
        assertNotNull(deck.getCard());
        deck.newDeck();
        assertNotNull(deck.getCard());
    }
}
