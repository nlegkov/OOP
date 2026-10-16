package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.nsu.legkov.cards.Card;
import ru.nsu.legkov.cards.Rank;
import ru.nsu.legkov.cards.Suit;
import ru.nsu.legkov.coregame.DeckOfCards;
import ru.nsu.legkov.entity.Dealer;
import ru.nsu.legkov.entity.Hand;
import ru.nsu.legkov.entity.Player;
import ru.nsu.legkov.user.ConsoleUserInterface;

/**
 * Тесты игры Blackjack (21).
 */
class MainTest {

    private final InputStream oldIn = System.in;

    /**
     * Возвращает стандартный поток ввода после теста.
     */
    @AfterEach
    void restoreInput() {
        System.setIn(oldIn);
    }

    /**
     * Проверка текстовых названий мастей.
     */
    @Test
    void suitShouldReturnCorrectNames() {
        assertEquals("Червы", Suit.CHERVI.getName());
        assertEquals("Бубны", Suit.BUBNI.getName());
        assertEquals("Крести", Suit.KRESTI.getName());
        assertEquals("Пики", Suit.PIKI.getName());
    }

    /**
     * Проверка достоинств и названий рангов карт.
     */
    @Test
    void rankValuesAndNamesShouldBeCorrect() {
        assertEquals(2, Rank.TWO.getValue());
        assertEquals("Двойка", Rank.TWO.getName());

        assertEquals(10, Rank.TEN.getValue());
        assertEquals(10, Rank.JACK.getValue());
        assertEquals(10, Rank.QUEEN.getValue());
        assertEquals(10, Rank.KING.getValue());

        assertEquals(11, Rank.ACE.getValue());
        assertEquals("Туз", Rank.ACE.getName());
    }

    /**
     * Проверка номинальной стоимости карт.
     */
    @Test
    void cardNominalValueShouldBeCorrect() {
        Card ace = new Card(Suit.CHERVI, Rank.ACE);
        Card tenCard = new Card(Suit.PIKI, Rank.TEN);
        Card kingCard = new Card(Suit.PIKI, Rank.KING);

        assertEquals(11, ace.getValue());
        assertEquals(10, tenCard.getValue());
        assertEquals(10, kingCard.getValue());

        assertTrue(ace.isAce());
        assertFalse(tenCard.isAce());
    }

    /**
     * Проверка Туза как 11 очков в руке.
     */
    @Test
    void aceShouldCountAsEleven() {
        Hand hand = new Hand();

        hand.addCard(new Card(Suit.CHERVI, Rank.ACE));
        hand.addCard(new Card(Suit.KRESTI, Rank.FIVE));

        assertEquals(16, hand.getScore());
    }

    /**
     * Проверка динамического уменьшения стоимости Туза с 11 до 1 при переборе (>21).
     */
    @Test
    void aceShouldReduceToOneOnBust() {
        Hand hand = new Hand();

        hand.addCard(new Card(Suit.CHERVI, Rank.ACE)); // 11
        hand.addCard(new Card(Suit.PIKI, Rank.FIVE));   // 11 + 5 = 16
        hand.addCard(new Card(Suit.BUBNI, Rank.KING));  // 11 + 5 + 10 = 26 ->
        // Туз становится 1 -> (1 + 5 + 10 = 16)

        assertEquals(16, hand.getScore());
    }

    /**
     * Проверка обработки нескольких тузов на одной руке.
     */
    @Test
    void multipleAcesShouldBeHandledCorrectly() {
        Hand hand = new Hand();

        hand.addCard(new Card(Suit.CHERVI, Rank.ACE)); // 11
        hand.addCard(new Card(Suit.PIKI, Rank.ACE));   // 11 + 1 = 12
        hand.addCard(new Card(Suit.KRESTI, Rank.NINE)); // 12 + 9 = 21

        assertEquals(21, hand.getScore());
    }

    /**
     * Проверка того, что закрытая карта не учитывается в общей сумме очков.
     */
    @Test
    void closedCardShouldNotBeCounted() {
        Hand hand = new Hand();

        Card hiddenCard = new Card(Suit.CHERVI, Rank.TEN);
        hiddenCard.setHide(true);

        hand.addCard(hiddenCard);
        hand.addCard(new Card(Suit.KRESTI, Rank.FIVE));

        assertEquals(5, hand.getScore());
        assertEquals("<скрытая карта>", hiddenCard.toString());
    }

    /**
     * Проверка очистки руки.
     */
    @Test
    void resetShouldClearHand() {
        Player player = new Player();
        DeckOfCards deck = new DeckOfCards();

        player.takeCard(deck);
        player.takeCard(deck);

        player.resetHand();

        assertEquals(0, player.getScore());
        assertEquals("Рука пуста", player.lastCards());
    }

    /**
     * Проверка получения карты из колоды.
     */
    @Test
    void deckShouldReturnCard() {
        DeckOfCards deck = new DeckOfCards();

        Card card = deck.getCard();

        assertNotNull(card);
    }

    /**
     * Проверка работы колоды после извлечения всех 52 карт (автоматический сброс и создание новой).
     */
    @Test
    void deckShouldResetWhenAllCardsUsed() {
        DeckOfCards deck = new DeckOfCards();

        Card card = null;

        for (int i = 0; i < 53; ++i) {
            card = deck.getCard();
        }

        assertNotNull(card);
    }

    /**
     * Логика дилера: дилер должен добирать карту, пока счет меньше 17.
     */
    @Test
    void dealerShouldTakeCardWhenScoreLessThan17() {
        Dealer dealer = new Dealer();
        DeckOfCards deck = new DeckOfCards();

        // Дилер берет карт до порога 17 очков
        while (dealer.getScore() < 17) {
            int prevScore = dealer.getScore();
            dealer.takeCard(deck);
            assertTrue(dealer.getScore() > prevScore);
        }

        int scoreAfterThreshold = dealer.getScore();
        // Попытка взять карту при очках >= 17 не должна изменить счет
        dealer.takeCard(deck);
        assertEquals(scoreAfterThreshold, dealer.getScore());
    }

    /**
     * Раскрытие закрытых карт дилера.
     */
    @Test
    void dealerShouldOpenHiddenCards() {
        Dealer dealer = new Dealer();
        DeckOfCards deck = new DeckOfCards();

        dealer.takeCard(deck, false); // Видимая карта
        dealer.takeCard(deck, true);  // Скрытая карта

        int scoreWithHidden = dealer.getScore();
        dealer.openCard(); // Раскрываем карты
        int scoreAfterOpen = dealer.getScore();

        assertTrue(scoreAfterOpen >= scoreWithHidden);
    }

    /**
     * Проверка ввода пользователя: выбор остановиться (0).
     */
    @Test
    void inputShouldReturnZeroForStopAction() {
        ConsoleUserInterface ui = createUi("0\n");

        assertEquals(0, ui.getPlayerChoice());
    }

    /**
     * Проверка ввода пользователя: выбор взять карту (1).
     */
    @Test
    void inputShouldReturnOneForTakeCardAction() {
        ConsoleUserInterface ui = createUi("1\n");

        assertEquals(1, ui.getPlayerChoice());
    }

    /**
     * Проверка повторного запроса ввода при ошибочном значении ('5', затем '1').
     */
    @Test
    void inputShouldRetryOnWrongPlayerAction() {
        ConsoleUserInterface ui = createUi("5\n1\n");

        assertEquals(1, ui.getPlayerChoice());
    }

    /**
     * Проверка ввода некорректного символа перед верным значением ('abc', затем '0').
     */
    @Test
    void inputShouldRetryOnNonNumberAction() {
        ConsoleUserInterface ui = createUi("abc\n0\n");

        assertEquals(0, ui.getPlayerChoice());
    }

    /**
     * Проверка согласия продолжить игру (1 -> true).
     */
    @Test
    void askToContinueShouldReturnTrueForOne() {
        ConsoleUserInterface ui = createUi("1\n");

        assertTrue(ui.askToContinue());
    }

    /**
     * Проверка отказа продолжить игру (0 -> false).
     */
    @Test
    void askToContinueShouldReturnFalseForZero() {
        ConsoleUserInterface ui = createUi("0\n");

        assertFalse(ui.askToContinue());
    }

    /**
     * Проверка вывода руки игрока в консоль.
     */
    @Test
    void printShouldOutputCards() {
        Player player = new Player();
        DeckOfCards deck = new DeckOfCards();

        player.takeCard(deck);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(output));

        try {
            System.out.println(player);
        } finally {
            System.setOut(oldOut);
        }

        assertTrue(output.toString().contains("Рука игрока:"));
    }

    /**
     * Вспомогательный метод создания объекта интерфейса с подменой System.in.
     *
     * @param text данные для стандартного ввода
     * @return объект консольного интерфейса
     */
    private ConsoleUserInterface createUi(String text) {
        ByteArrayInputStream stream = new ByteArrayInputStream(
                text.getBytes(StandardCharsets.UTF_8)
        );

        System.setIn(stream);

        return new ConsoleUserInterface();
    }
    private static Card card(Rank rank) {
        return new Card(Suit.PIKI, rank);
    }

    private static void play(GameState state, Player player, Dealer dealer,
                             DeckOfCards deck, int... choices) {
        new Round(state, player, dealer, deck, new TestUi(choices)).play();
    }

    @Test
    void playerWinsWithHigherScore() {
        GameState state = new GameState();
        Player player = new Player();
        Dealer dealer = new Dealer();
        FixedDeck deck = new FixedDeck(card(Rank.TEN), card(Rank.NINE),
                card(Rank.TEN), card(Rank.SEVEN));

        play(state, player, dealer, deck, 0);

        assertEquals(19, player.getScore());
        assertEquals(17, dealer.getScore());
        assertEquals(1, state.getScorePlayer());
        assertEquals(0, state.getScoreDealer());
        assertEquals(1, state.getNumberOfRounds());
        assertFalse(dealer.getHand().getCards().get(1).isHide());
    }

    @Test
    void dealerWinsWithHigherScore() {
        GameState state = new GameState();
        Player player = new Player();
        Dealer dealer = new Dealer();
        play(state, player, dealer, new FixedDeck(card(Rank.EIGHT), card(Rank.EIGHT),
                card(Rank.TEN), card(Rank.NINE)), 0);

        assertEquals(0, state.getScorePlayer());
        assertEquals(1, state.getScoreDealer());
    }

    @Test
    void equalScoresResultInDraw() {
        GameState state = new GameState();
        play(state, new Player(), new Dealer(),
                new FixedDeck(card(Rank.TEN), card(Rank.EIGHT),
                        card(Rank.TEN), card(Rank.EIGHT)), 0);

        assertEquals(0, state.getScorePlayer());
        assertEquals(0, state.getScoreDealer());
        assertEquals(1, state.getNumberOfRounds());
    }

    @Test
    void dealerBustGivesPlayerWin() {
        GameState state = new GameState();
        Dealer dealer = new Dealer();
        play(state, new Player(), dealer,
                new FixedDeck(card(Rank.TEN), card(Rank.EIGHT),
                        card(Rank.NINE), card(Rank.SIX), card(Rank.KING)), 0);

        assertEquals(25, dealer.getScore());
        assertEquals(1, state.getScorePlayer());
        assertEquals(0, state.getScoreDealer());
    }

    @Test
    void playerBustStopsRoundBeforeDealerTurn() {
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
    void dealerDrawsToSeventeenThenStops() {
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

    private static final class FixedDeck extends DeckOfCards {
        private final Queue<Card> cards;

        private FixedDeck(Card... cards) {
            this.cards = new ArrayDeque<>(Arrays.asList(cards));
        }

        @Override
        public Card getCard() {
            Card next = cards.poll();
            if (next == null) {
                throw new AssertionError("Неожиданный запрос карты");
            }
            return next;
        }

        private int remaining() {
            return cards.size();
        }
    }

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
                throw new AssertionError("Неожиданный запрос выбора игрока");
            }
            return choice;
        }

        @Override
        public boolean askToContinue() {
            return false;
        }
    }

    @Test
    void dealerStopsTakingCardsAtSeventeen() {
        Dealer dealer = new Dealer();
        FixedDeck deck = new FixedDeck(Rank.TEN, Rank.SIX, Rank.TWO, Rank.KING);

        dealer.takeCard(deck);
        assertEquals(10, dealer.getScore());
        dealer.takeCard(deck);
        assertEquals(16, dealer.getScore());
        dealer.takeCard(deck);
        assertEquals(18, dealer.getScore());

        dealer.takeCard(deck);
        assertEquals(18, dealer.getScore());
        assertEquals(3, dealer.getHand().getCards().size());
        assertEquals(1, deck.remaining());
    }

    @Test
    void hiddenCardIsRevealedAndHandCanBeReset() {
        Dealer dealer = new Dealer();
        FixedDeck deck = new FixedDeck(Rank.TEN, Rank.SEVEN);

        dealer.takeCard(deck);
        dealer.takeCard(deck, true);
        assertEquals(10, dealer.getScore());
        assertTrue(dealer.getHand().getCards().get(1).isHide());

        dealer.openCard();
        assertFalse(dealer.getHand().getCards().get(1).isHide());
        assertEquals(17, dealer.getScore());

        dealer.resetHand();
        assertEquals(0, dealer.getScore());
        assertTrue(dealer.getHand().getCards().isEmpty());
    }

    private static final class FixedDeck extends DeckOfCards {
        private final Queue<Card> cards = new ArrayDeque<>();

        private FixedDeck(Rank... ranks) {
            Arrays.stream(ranks).forEach(rank -> cards.add(new Card(Suit.PIKI, rank)));
        }

        @Override
        public Card getCard() {
            Card next = cards.poll();
            if (next == null) {
                throw new AssertionError("Неожиданный запрос карты");
            }
            return next;
        }

        private int remaining() {
            return cards.size();
        }
    }
}