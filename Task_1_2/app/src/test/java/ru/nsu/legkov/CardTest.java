package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.legkov.cards.Card;
import ru.nsu.legkov.cards.Rank;
import ru.nsu.legkov.cards.Suit;

/** Тесты карт и номиналов. */
class CardTest {
    @Test
    void cardPropertiesAndVisibility() {
        Card ace = new Card(Suit.CHERVI, Rank.ACE);
        assertEquals(Suit.CHERVI, ace.getSuit());
        assertEquals(Rank.ACE, ace.getRank());
        assertEquals(11, ace.getValue());
        assertTrue(ace.isAce());
        assertEquals("Туз Червы (11)", ace.toString());
        ace.setHide(true);
        assertTrue(ace.isHide());
        assertTrue(ace.getHide());
        assertEquals("<скрытая карта>", ace.toString());
        ace.setHide(false);
        assertFalse(ace.isHide());
        assertEquals("Туз Червы (11)", ace.toString());
        assertFalse(TestCards.card(Rank.KING).isAce());
        assertEquals(10, TestCards.card(Rank.KING).getValue());
    }

    @Test
    void ranksAndSuitsAreComplete() {
        assertEquals(13, Rank.values().length);
        assertEquals(4, Suit.values().length);
        assertEquals("Двойка", Rank.TWO.getName());
        assertEquals(2, Rank.TWO.getValue());
        assertEquals(10, Rank.JACK.getValue());
        assertEquals(10, Rank.QUEEN.getValue());
        assertEquals(10, Rank.KING.getValue());
        assertEquals("Туз", Rank.ACE.getName());
        assertEquals("Червы", Suit.CHERVI.getName());
        assertEquals("Бубны", Suit.BUBNI.getName());
        assertEquals("Крести", Suit.KRESTI.getName());
        assertEquals("Пики", Suit.PIKI.getName());
    }
}
