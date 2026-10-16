package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.legkov.cards.Rank;
import ru.nsu.legkov.coregame.GameState;
import ru.nsu.legkov.coregame.Round;
import ru.nsu.legkov.entity.Dealer;
import ru.nsu.legkov.entity.Player;

/** Тесты игровых сценариев одного раунда. */
class RoundTest {
    private static void play(GameState state, Player player, Dealer dealer,
                             FixedDeck deck, int... choices) {
        new Round(state, player, dealer, deck, new TestUi(choices)).play();
    }

    @Test
    void roundPlayerWinsByHigherScore() {
        GameState state = new GameState();
        Player player = new Player();
        Dealer dealer = new Dealer();
        play(state, player, dealer, new FixedDeck(TestCards.card(Rank.TEN), TestCards.card(Rank.NINE),
                TestCards.card(Rank.TEN), TestCards.card(Rank.SEVEN)), 0);
        assertEquals(19, player.getScore());
        assertEquals(17, dealer.getScore());
        assertEquals(1, state.getScorePlayer());
        assertEquals(0, state.getScoreDealer());
        assertEquals(1, state.getNumberOfRounds());
        assertFalse(dealer.getHand().getCards().get(1).isHide());
    }

    @Test
    void roundDealerWinsByHigherScore() {
        GameState state = new GameState();
        play(state, new Player(), new Dealer(), new FixedDeck(
                TestCards.card(Rank.EIGHT), TestCards.card(Rank.EIGHT), TestCards.card(Rank.TEN), TestCards.card(Rank.NINE)), 0);
        assertEquals(0, state.getScorePlayer());
        assertEquals(1, state.getScoreDealer());
    }

    @Test
    void roundWithEqualScoreIsDraw() {
        GameState state = new GameState();
        play(state, new Player(), new Dealer(), new FixedDeck(
                TestCards.card(Rank.TEN), TestCards.card(Rank.EIGHT), TestCards.card(Rank.TEN), TestCards.card(Rank.EIGHT)), 0);
        assertEquals(0, state.getScorePlayer());
        assertEquals(0, state.getScoreDealer());
        assertEquals(1, state.getNumberOfRounds());
    }

    @Test
    void roundDealerBustAwardsPlayer() {
        GameState state = new GameState();
        Dealer dealer = new Dealer();
        play(state, new Player(), dealer, new FixedDeck(TestCards.card(Rank.TEN), TestCards.card(Rank.EIGHT),
                TestCards.card(Rank.NINE), TestCards.card(Rank.SIX), TestCards.card(Rank.KING)), 0);
        assertEquals(25, dealer.getScore());
        assertEquals(1, state.getScorePlayer());
        assertEquals(0, state.getScoreDealer());
    }

    @Test
    void roundPlayerBustSkipsDealerTurn() {
        GameState state = new GameState();
        Dealer dealer = new Dealer();
        FixedDeck deck = new FixedDeck(TestCards.card(Rank.TEN), TestCards.card(Rank.NINE),
                TestCards.card(Rank.TEN), TestCards.card(Rank.SEVEN), TestCards.card(Rank.FIVE));
        play(state, new Player(), dealer, deck, 1);
        assertEquals(1, state.getScoreDealer());
        assertEquals(0, state.getScorePlayer());
        assertEquals(0, deck.remaining());
        assertTrue(dealer.getHand().getCards().get(1).isHide());
    }

    @Test
    void roundPlayerCanHitAndThenStand() {
        GameState state = new GameState();
        Player player = new Player();
        FixedDeck deck = new FixedDeck(TestCards.card(Rank.FIVE), TestCards.card(Rank.SIX),
                TestCards.card(Rank.TEN), TestCards.card(Rank.SEVEN), TestCards.card(Rank.EIGHT));
        play(state, player, new Dealer(), deck, 1, 0);
        assertEquals(19, player.getScore());
        assertEquals(1, state.getScorePlayer());
        assertEquals(0, deck.remaining());
    }

    @Test
    void roundDealerDrawsToSeventeenAndStops() {
        GameState state = new GameState();
        Dealer dealer = new Dealer();
        FixedDeck deck = new FixedDeck(TestCards.card(Rank.TEN), TestCards.card(Rank.EIGHT),
                TestCards.card(Rank.EIGHT), TestCards.card(Rank.SEVEN), TestCards.card(Rank.TWO), TestCards.card(Rank.KING));
        play(state, new Player(), dealer, deck, 0);
        assertEquals(17, dealer.getScore());
        assertEquals(3, dealer.getHand().getCards().size());
        assertEquals(1, deck.remaining());
        assertEquals(1, state.getScorePlayer());
    }

    @Test
    void roundResetsBothHandsBeforeDealing() {
        GameState state = new GameState();
        Player player = new Player();
        Dealer dealer = new Dealer();
        player.getHand().addCard(TestCards.card(Rank.TWO));
        dealer.getHand().addCard(TestCards.card(Rank.THREE));
        play(state, player, dealer, new FixedDeck(TestCards.card(Rank.TEN), TestCards.card(Rank.EIGHT),
                TestCards.card(Rank.TEN), TestCards.card(Rank.EIGHT)), 0);
        assertEquals(2, player.getHand().getCards().size());
        assertEquals(2, dealer.getHand().getCards().size());
        assertEquals(18, player.getScore());
    }
}
