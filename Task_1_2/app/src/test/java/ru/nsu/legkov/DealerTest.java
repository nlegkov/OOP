package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.legkov.cards.Rank;
import ru.nsu.legkov.entity.Dealer;

/** Тесты правил дилера. */
class DealerTest {
    @Test
    void dealerDecisionUsesFixedScores() {
        Dealer dealer = new Dealer();
        assertTrue(dealer.shouldTakeCard());
        dealer.takeCard(new FixedDeck(TestCards.card(Rank.TEN)));
        assertEquals(10, dealer.getScore());
        assertTrue(dealer.shouldTakeCard());
        dealer.takeCard(new FixedDeck(TestCards.card(Rank.SIX)));
        assertEquals(16, dealer.getScore());
        assertTrue(dealer.shouldTakeCard());
        dealer.takeCard(new FixedDeck(TestCards.card(Rank.ACE)));
        assertEquals(17, dealer.getScore());
        assertFalse(dealer.shouldTakeCard());
        assertEquals(3, dealer.getHand().getCards().size());
        assertTrue(dealer.toString().contains("Очки: 17"));
    }

    @Test
    void dealerCanTakeInitialCardsEvenIfFirstIsAce() {
        Dealer dealer = new Dealer();
        FixedDeck deck = new FixedDeck(TestCards.card(Rank.ACE), TestCards.card(Rank.KING));
        dealer.takeCard(deck);
        dealer.takeCard(deck, true);
        assertEquals(2, dealer.getHand().getCards().size());
        assertTrue(dealer.getHand().getCards().get(1).isHide());
        dealer.openCard();
        assertFalse(dealer.getHand().getCards().get(1).isHide());
        assertEquals(21, dealer.getScore());
        assertFalse(dealer.shouldTakeCard());
        dealer.resetHand();
        assertEquals(0, dealer.getScore());
    }
}
