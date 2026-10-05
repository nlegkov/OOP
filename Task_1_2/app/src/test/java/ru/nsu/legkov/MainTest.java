package ru.nsu.legkov;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.nsu.legkov.User.ConsoleUserInterface;
import ru.nsu.legkov.cards.Card;
import ru.nsu.legkov.cards.Rank;
import ru.nsu.legkov.cards.Suit;
import ru.nsu.legkov.coreGame.DeckOfCards;
import ru.nsu.legkov.entity.Dealer;
import ru.nsu.legkov.entity.Hand;
import ru.nsu.legkov.entity.Player;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

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
        hand.addCard(new Card(Suit.BUBNI, Rank.KING));  // 11 + 5 + 10 = 26 -> Туз становится 1 -> (1 + 5 + 10 = 16)

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
        ConsoleUserInterface ui = createUI("0\n");

        assertEquals(0, ui.getPlayerChoice());
    }

    /**
     * Проверка ввода пользователя: выбор взять карту (1).
     */
    @Test
    void inputShouldReturnOneForTakeCardAction() {
        ConsoleUserInterface ui = createUI("1\n");

        assertEquals(1, ui.getPlayerChoice());
    }

    /**
     * Проверка повторного запроса ввода при ошибочном значении ('5', затем '1').
     */
    @Test
    void inputShouldRetryOnWrongPlayerAction() {
        ConsoleUserInterface ui = createUI("5\n1\n");

        assertEquals(1, ui.getPlayerChoice());
    }

    /**
     * Проверка ввода некорректного символа перед верным значением ('abc', затем '0').
     */
    @Test
    void inputShouldRetryOnNonNumberAction() {
        ConsoleUserInterface ui = createUI("abc\n0\n");

        assertEquals(0, ui.getPlayerChoice());
    }

    /**
     * Проверка согласия продолжить игру (1 -> true).
     */
    @Test
    void askToContinueShouldReturnTrueForOne() {
        ConsoleUserInterface ui = createUI("1\n");

        assertTrue(ui.askToContinue());
    }

    /**
     * Проверка отказа продолжить игру (0 -> false).
     */
    @Test
    void askToContinueShouldReturnFalseForZero() {
        ConsoleUserInterface ui = createUI("0\n");

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
    private ConsoleUserInterface createUI(String text) {
        ByteArrayInputStream stream = new ByteArrayInputStream(
                text.getBytes(StandardCharsets.UTF_8)
        );

        System.setIn(stream);

        return new ConsoleUserInterface();
    }
}