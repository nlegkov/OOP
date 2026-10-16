package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.nsu.legkov.cards.Card;
import ru.nsu.legkov.cards.Rank;
import ru.nsu.legkov.cards.Suit;
import ru.nsu.legkov.coregame.DeckOfCards;
import ru.nsu.legkov.coregame.Game;
import ru.nsu.legkov.coregame.GameState;
import ru.nsu.legkov.coregame.Round;
import ru.nsu.legkov.entity.Dealer;
import ru.nsu.legkov.entity.Hand;
import ru.nsu.legkov.entity.Player;
import ru.nsu.legkov.user.ConsoleUserInterface;
import ru.nsu.legkov.user.UserInterface;

/**
 * Проверки правил игры, одного раунда и консольного интерфейса.
 * В игровых сценариях используются заранее известные карты.
 */
class MainTest {
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    @AfterEach
    void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private static Card card(Rank rank) {
        return new Card(Suit.PIKI, rank);
    }

    private static void play(GameState state, Player player, Dealer dealer,
                             FixedDeck deck, int... choices) {
        new Round(state, player, dealer, deck, new TestUi(choices)).play();
    }

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
        assertFalse(card(Rank.KING).isAce());
        assertEquals(10, card(Rank.KING).getValue());
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
    void emptyHandAndClear() {
        Hand hand = new Hand();
        assertEquals(0, hand.getScore());
        assertEquals("Рука пуста", hand.toString());
        assertEquals("Рука пуста", hand.lastCard());
        hand.addCard(card(Rank.FIVE));
        hand.addCard(card(Rank.SIX));
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
        hand.addCard(card(Rank.ACE));
        hand.addCard(card(Rank.FIVE));
        assertEquals(16, hand.getScore());
        hand.addCard(card(Rank.KING));
        assertEquals(16, hand.getScore());
        hand.addCard(card(Rank.SIX));
        assertEquals(22, hand.getScore());
    }

    @Test
    void multipleAcesAreCountedCorrectly() {
        Hand hand = new Hand();
        hand.addCard(card(Rank.ACE));
        hand.addCard(card(Rank.ACE));
        assertEquals(12, hand.getScore());
        hand.addCard(card(Rank.NINE));
        assertEquals(21, hand.getScore());
        hand.addCard(card(Rank.ACE));
        assertEquals(12, hand.getScore());
    }

    @Test
    void hiddenCardDoesNotAppearInDisplayedHandScore() {
        Hand hand = new Hand();
        Card hidden = card(Rank.KING);
        hidden.setHide(true);
        hand.addCard(card(Rank.FIVE));
        hand.addCard(hidden);
        assertEquals(5, hand.getScore());
        assertTrue(hand.toString().contains("<скрытая карта>"));
        hidden.setHide(false);
        assertEquals(15, hand.getScore());
    }

    @Test
    void playerTakesCardsAndResets() {
        Player player = new Player();
        FixedDeck deck = new FixedDeck(card(Rank.TEN), card(Rank.SEVEN));
        player.takeCard(deck);
        player.takeCard(deck);
        assertEquals(17, player.getScore());
        assertEquals(2, player.getHand().getCards().size());
        assertTrue(player.toString().contains("Очки: 17"));
        player.resetHand();
        assertEquals(0, player.getScore());
        assertEquals("Рука пуста", player.lastCards());
    }

    @Test
    void dealerDecisionUsesFixedScores() {
        Dealer dealer = new Dealer();
        assertTrue(dealer.shouldTakeCard());
        dealer.takeCard(new FixedDeck(card(Rank.TEN)));
        assertEquals(10, dealer.getScore());
        assertTrue(dealer.shouldTakeCard());
        dealer.takeCard(new FixedDeck(card(Rank.SIX)));
        assertEquals(16, dealer.getScore());
        assertTrue(dealer.shouldTakeCard());
        dealer.takeCard(new FixedDeck(card(Rank.ACE)));
        assertEquals(17, dealer.getScore());
        assertFalse(dealer.shouldTakeCard());
        assertEquals(3, dealer.getHand().getCards().size());
        assertTrue(dealer.toString().contains("Очки: 17"));
    }

    @Test
    void dealerCanTakeInitialCardsEvenIfFirstIsAce() {
        Dealer dealer = new Dealer();
        FixedDeck deck = new FixedDeck(card(Rank.ACE), card(Rank.KING));
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

    @Test
    void gameStateCountsRoundsAndWins() {
        GameState state = new GameState();
        assertEquals(0, state.getNumberOfRounds());
        assertTrue(state.toStringScoreRound().contains("ничья"));
        assertTrue(state.toStringScoreEndGame().contains("ничья"));
        state.incrementRounds();
        state.incrementScorePlayer();
        assertEquals(1, state.getNumberOfRounds());
        assertEquals(1, state.getScorePlayer());
        assertTrue(state.toStringScoreRound().contains("Вы лидируете"));
        assertTrue(state.toStringScoreEndGame().contains("Вы Победили"));
        state.incrementScoreDealer();
        state.incrementScoreDealer();
        assertEquals(2, state.getScoreDealer());
        assertTrue(state.toStringScoreRound().contains("Дилер лидирует"));
        assertTrue(state.toStringScoreEndGame().contains("Дилер победил"));
    }

    @Test
    void roundPlayerWinsByHigherScore() {
        GameState state = new GameState();
        Player player = new Player();
        Dealer dealer = new Dealer();
        play(state, player, dealer, new FixedDeck(card(Rank.TEN), card(Rank.NINE),
                card(Rank.TEN), card(Rank.SEVEN)), 0);
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
                card(Rank.EIGHT), card(Rank.EIGHT), card(Rank.TEN), card(Rank.NINE)), 0);
        assertEquals(0, state.getScorePlayer());
        assertEquals(1, state.getScoreDealer());
    }

    @Test
    void roundWithEqualScoreIsDraw() {
        GameState state = new GameState();
        play(state, new Player(), new Dealer(), new FixedDeck(
                card(Rank.TEN), card(Rank.EIGHT), card(Rank.TEN), card(Rank.EIGHT)), 0);
        assertEquals(0, state.getScorePlayer());
        assertEquals(0, state.getScoreDealer());
        assertEquals(1, state.getNumberOfRounds());
    }

    @Test
    void roundDealerBustAwardsPlayer() {
        GameState state = new GameState();
        Dealer dealer = new Dealer();
        play(state, new Player(), dealer, new FixedDeck(card(Rank.TEN), card(Rank.EIGHT),
                card(Rank.NINE), card(Rank.SIX), card(Rank.KING)), 0);
        assertEquals(25, dealer.getScore());
        assertEquals(1, state.getScorePlayer());
        assertEquals(0, state.getScoreDealer());
    }

    @Test
    void roundPlayerBustSkipsDealerTurn() {
        GameState state = new GameState();
        Dealer dealer = new Dealer();
        FixedDeck deck = new FixedDeck(card(Rank.TEN), card(Rank.NINE),
                card(Rank.TEN), card(Rank.SEVEN), card(Rank.FIVE));
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
        FixedDeck deck = new FixedDeck(card(Rank.FIVE), card(Rank.SIX),
                card(Rank.TEN), card(Rank.SEVEN), card(Rank.EIGHT));
        play(state, player, new Dealer(), deck, 1, 0);
        assertEquals(19, player.getScore());
        assertEquals(1, state.getScorePlayer());
        assertEquals(0, deck.remaining());
    }

    @Test
    void roundDealerDrawsToSeventeenAndStops() {
        GameState state = new GameState();
        Dealer dealer = new Dealer();
        FixedDeck deck = new FixedDeck(card(Rank.TEN), card(Rank.EIGHT),
                card(Rank.EIGHT), card(Rank.SEVEN), card(Rank.TWO), card(Rank.KING));
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
        player.getHand().addCard(card(Rank.TWO));
        dealer.getHand().addCard(card(Rank.THREE));
        play(state, player, dealer, new FixedDeck(card(Rank.TEN), card(Rank.EIGHT),
                card(Rank.TEN), card(Rank.EIGHT)), 0);
        assertEquals(2, player.getHand().getCards().size());
        assertEquals(2, dealer.getHand().getCards().size());
        assertEquals(18, player.getScore());
    }

    @Test
    void consoleReadsValidChoicesAndRetriesInvalidInput() {
        ConsoleUserInterface ui = consoleWithInput("abc\n5\n1\n-1\n0\n");
        assertEquals(1, ui.getPlayerChoice());
        assertEquals(0, ui.getPlayerChoice());
    }

    @Test
    void consoleReadsContinueAnswersAndRetriesInvalidInput() {
        ConsoleUserInterface ui = consoleWithInput("hello\n2\n1\nno\n0\n");
        assertTrue(ui.askToContinue());
        assertFalse(ui.askToContinue());
    }

    @Test
    void consoleRendersWelcomeRoundAndMessages() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
        ConsoleUserInterface ui = consoleWithInput("0\n");
        ui.showWelcomeMessage();
        ui.showRoundStart(2);
        ui.showMessage("Проверка сообщения");
        ui.showGameState(new Player(), new Dealer());
        String shown = output.toString(StandardCharsets.UTF_8);
        assertTrue(shown.contains("Добро пожаловать"));
        assertTrue(shown.contains("Раунд 2"));
        assertTrue(shown.contains("Проверка сообщения"));
        assertTrue(shown.contains("Рука игрока"));
        assertTrue(shown.contains("Рука дилера"));
    }

    @Test
    void gameCanRunOneCompleteRoundFromConsole() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
        System.setIn(new ByteArrayInputStream("0\n0\n".getBytes(StandardCharsets.UTF_8)));
        new Game().startGame();
        String shown = output.toString(StandardCharsets.UTF_8);
        assertTrue(shown.contains("Раунд 1"));
        assertTrue(shown.contains("Раунд окончен"));
        assertTrue(shown.contains("Игра окончена"));
    }

    private static ConsoleUserInterface consoleWithInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));
        return new ConsoleUserInterface();
    }

    /** Колода с заранее заданной последовательностью раздачи. */
    private static final class FixedDeck extends DeckOfCards {
        private final Queue<Card> cards;

        private FixedDeck(Card... cards) {
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

        private int remaining() {
            return cards.size();
        }
    }

    /** Интерфейс с заранее заданными ответами игрока. */
    private static final class TestUi implements UserInterface {
        private final Queue<Integer> choices = new ArrayDeque<>();

        private TestUi(int... choices) {
            for (int choice : choices) {
                this.choices.add(choice);
            }
        }

        @Override
        public void showWelcomeMessage() { }

        @Override
        public void showRoundStart(int roundNumber) { }

        @Override
        public void showGameState(Player player, Dealer dealer) { }

        @Override
        public void showMessage(String message) { }

        @Override
        public int getPlayerChoice() {
            Integer choice = choices.poll();
            if (choice == null) {
                throw new AssertionError("Раунд запросил лишний выбор игрока");
            }
            return choice;
        }

        @Override
        public boolean askToContinue() {
            return false;
        }
    }
}
