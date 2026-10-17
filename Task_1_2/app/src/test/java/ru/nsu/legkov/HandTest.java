package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.legkov.cards.Card;
import ru.nsu.legkov.cards.Rank;
import ru.nsu.legkov.entity.Hand;

/** Тесты подсчёта очков и хранения руки. */
class HandTest {
    @Test
    void emptyHandAndClear() {
        Hand hand = new Hand();
        assertEquals(0, hand.getScore());
        assertEquals("Рука пуста", hand.toString());
        assertEquals("Рука пуста", hand.lastCard());
        hand.addCard(TestCards.card(Rank.FIVE));
        hand.addCard(TestCards.card(Rank.SIX));
        assertEquals(11, hand.getScore());
        assertEquals("Шестерка Пики (6)", hand.lastCard());
        assertEquals("Пятерка Пики (5), Шестерка Пики (6)", hand.toString());
        hand.clearCard();
        assertTrue(hand.getCards().isEmpty());
        assertEquals(0, hand.getScore());
    }

    @Test
    void aceIsReducedOnlyWhenNeeded() {
        Hand hand = new Hand();
        hand.addCard(TestCards.card(Rank.ACE));
        hand.addCard(TestCards.card(Rank.FIVE));
        assertEquals(16, hand.getScore());
        hand.addCard(TestCards.card(Rank.KING));
        assertEquals(16, hand.getScore());
        hand.addCard(TestCards.card(Rank.SIX));
        assertEquals(22, hand.getScore());
    }

    @Test
    void multipleAcesAreCountedCorrectly() {
        Hand hand = new Hand();
        hand.addCard(TestCards.card(Rank.ACE));
        hand.addCard(TestCards.card(Rank.ACE));
        assertEquals(12, hand.getScore());
        hand.addCard(TestCards.card(Rank.NINE));
        assertEquals(21, hand.getScore());
        hand.addCard(TestCards.card(Rank.ACE));
        assertEquals(12, hand.getScore());
    }

    @Test
    void hiddenCardDoesNotAppearInDisplayedHandScore() {
        Hand hand = new Hand();
        Card hidden = TestCards.card(Rank.KING);
        hidden.setHide(true);
        hand.addCard(TestCards.card(Rank.FIVE));
        hand.addCard(hidden);
        assertEquals(5, hand.getScore());
        assertTrue(hand.toString().contains("<скрытая карта>"));
        hidden.setHide(false);
        assertEquals(15, hand.getScore());
    }
}
