package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


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

    @Test
    void multipleDecksContainCorrectNumberOfCards() {
        DeckOfCards deck = new DeckOfCards(10);

        for (int i = 0; i < 520; i++) {
            assertNotNull(deck.getCard());
        }
    }

    @Test
    void multipleDecksContainTenCopiesOfEachCard() {
        DeckOfCards deck = new DeckOfCards(10);
        int aceCount = 0;

        for (int i = 0; i < 520; i++) {
            Card card = deck.getCard();

            if (card.getSuit() == Suit.PIKI && card.getRank() == Rank.ACE) {
                aceCount++;
            }
        }

        assertEquals(10, aceCount);
    }

    @Test
    void invalidDeckCountThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new DeckOfCards(0));
        assertThrows(IllegalArgumentException.class, () -> new DeckOfCards(-1));
    }
}
