package ru.nsu.legkov;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты для проверки логики игры.
 */
class MainTest {

    private DeckOfCards deck;
    private Hand hand;

    @BeforeEach
    void setUp() {
        deck = new DeckOfCards();
        hand = new Hand();
    }

    @Test
    @DisplayName("Проверка номиналов карт и Enum Rank")
    void testRankValues() {
        assertEquals(2, Rank.TWO.getValue());
        assertEquals("Двойка", Rank.TWO.getName());
        assertEquals(10, Rank.KING.getValue());
        assertEquals(11, Rank.ACE.getValue());
        assertEquals(13, Rank.values().length);
    }

    @Test
    @DisplayName("Проверка мастей Enum Suit")
    void testSuitValues() {
        assertEquals("Пики", Suit.PIKI.getName());
        assertEquals(4, Suit.values().length);
    }

    @Test
    @DisplayName("Инициализация и взятие карт из колоды")
    void testDeckOfCards() {
        assertNotNull(deck.getCard());

        for (int i = 0; i < 51; i++) {
            deck.getCard();
        }

        assertNull(deck.getCard());

        deck.newDeck();
        assertNotNull(deck.getCard());
    }

    @Test
    @DisplayName("Подсчет очков: динамический пересчет Тузов (усыхание)")
    void testHandAceSoftToHard() {
        hand.getCards().add(new Card(Suit.CHERVI, Rank.ACE)); // 11
        hand.getCards().add(new Card(Suit.PIKI, Rank.FIVE));   // 5 -> 16
        assertEquals(16, hand.getScore());

        hand.getCards().add(new Card(Suit.BUBNI, Rank.QUEEN)); // 10 -> стало 26, Туз усыхает до 1 -> 16
        assertEquals(16, hand.getScore());
    }

    @Test
    @DisplayName("Подсчет очков: несколько Тузов на одной руке")
    void testMultipleAcesInHand() {
        hand.getCards().add(new Card(Suit.CHERVI, Rank.ACE)); // 11
        hand.getCards().add(new Card(Suit.PIKI, Rank.ACE));   // 11 -> 22 -> усыхает до 12
        assertEquals(12, hand.getScore());

        hand.getCards().add(new Card(Suit.BUBNI, Rank.ACE));  // 12 + 11 -> 23 -> ещё один усыхает -> 13
        assertEquals(13, hand.getScore());
    }

    @Test
    @DisplayName("Проверка методов lastCard и toString класса Hand")
    void testHandStringMethods() {
        assertEquals("Рука пуста", hand.lastCard());
        assertEquals("Рука пуста", hand.toString());

        hand.getCards().add(new Card(Suit.PIKI, Rank.TEN));
        assertEquals("Десятка Пики (10)", hand.lastCard());
        assertEquals("Десятка Пики (10)", hand.toString());

        hand.getCards().add(new Card(Suit.CHERVI, Rank.JACK));
        assertEquals("Валет Червы (10)", hand.lastCard());
        assertEquals("Десятка Пики (10), Валет Червы (10)", hand.toString());
    }

    @Test
    @DisplayName("Взятие карты из пустой колоды (авто-пересоздание)")
    void testTakeCardFromEmptyDeck() {
        for (int i = 0; i < 52; i++) {
            deck.getCard();
        }

        // Вызов getCard должен запустить создание новой колоды в Hand
        hand.getCard(deck);
        assertEquals(1, hand.getCards().size());
    }

    @Test
    @DisplayName("Проверка усыхания Туза с 11 до 1")
    void testAceSoftToHardConversion() {
        Card ace = new Card(Suit.CHERVI, Rank.ACE);
        Card five = new Card(Suit.BUBNI, Rank.FIVE);
        Card queen = new Card(Suit.KRESTI, Rank.QUEEN);

        hand.getCards().add(ace);
        hand.getCards().add(five);
        hand.getCards().add(queen);

        assertEquals(16, hand.getScore(), "Счет должен быть 16 (11 + 5 + 10)");
    }

    @Test
    @DisplayName("Проверка логики Dealer (ограничение добора до 17 очков)")
    void testDealerLogic() {
        Dealer dealer = new Dealer();
        assertEquals(0, dealer.getScoreWins());

        dealer.addWin();
        assertEquals(1, dealer.getScoreWins());

        assertEquals("[]", dealer.getHiddenHandString());

        dealer.takeCard(deck);
        dealer.takeCard(deck);
        assertTrue(dealer.getHiddenHandString().contains("<закрытая карта>"));
        assertTrue(dealer.toString().contains("Рука дилера:"));

        dealer.resetHand();
        dealer.takeCard(deck);
        dealer.takeCard(deck);

        int currentScore = dealer.getScore();
        if (currentScore >= 17) {
            dealer.takeCard(deck);
            assertEquals(currentScore, dealer.getScore());
        }

        dealer.resetHand();
        assertEquals(0, dealer.getScore());
    }

    @Test
    @DisplayName("Несколько Тузов: только один сдувается при необходимости")
    void testMultipleAces() {
        hand.getCards().add(new Card(Suit.CHERVI, Rank.ACE));
        hand.getCards().add(new Card(Suit.PIKI, Rank.ACE));

        assertEquals(12, hand.getScore(), "Два туза должны давать 12 очков");

        hand.getCards().add(new Card(Suit.BUBNI, Rank.NINE));
        assertEquals(21, hand.getScore(), "Туз + Туз + 9 должно дать ровно 21");
    }


}