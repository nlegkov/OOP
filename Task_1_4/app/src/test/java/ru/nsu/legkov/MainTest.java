package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Модульные тесты для проверки игры в 21.
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
    @DisplayName("Проверка методов класса Card")
    void testCardMethods() {
        Card card = new Card(Suit.CHERVI, Rank.ACE);
        assertEquals(Suit.CHERVI, card.getSuit());
        assertEquals(Rank.ACE, card.getRank());
        assertEquals(11, card.getValue());
        assertTrue(card.isAce());
        assertEquals("Туз Червы (11)", card.toString());
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
    @DisplayName("Подсчет очков: динамический пересчет Тузов")
    void testHandAceSoftToHard() {
        hand.getCards().add(new Card(Suit.CHERVI, Rank.ACE));
        hand.getCards().add(new Card(Suit.PIKI, Rank.FIVE));
        assertEquals(16, hand.getScore());

        // Туз усыхает с 11 до 1 очка
        hand.getCards().add(new Card(Suit.BUBNI, Rank.QUEEN));
        assertEquals(16, hand.getScore());
    }

    @Test
    @DisplayName("Подсчет очков: несколько Тузов на одной руке")
    void testMultipleAcesInHand() {
        hand.getCards().add(new Card(Suit.CHERVI, Rank.ACE));
        hand.getCards().add(new Card(Suit.PIKI, Rank.ACE));
        assertEquals(12, hand.getScore());

        // Второй туз также уменьшается при необходимости
        hand.getCards().add(new Card(Suit.BUBNI, Rank.ACE));
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
    @DisplayName("Взятие карты из пустой колоды")
    void testTakeCardFromEmptyDeck() {
        for (int i = 0; i < 52; i++) {
            deck.getCard();
        }

        hand.getCard(deck);
        assertEquals(1, hand.getCards().size());
    }

    @Test
    @DisplayName("Проверка функционала класса Player")
    void testPlayerLogic() {
        Player player = new Player();
        assertEquals(0, player.getScoreWins());

        player.addWin();
        assertEquals(1, player.getScoreWins());

        player.takeCard(deck);
        player.takeCard(deck);
        assertTrue(player.getScore() > 0);
        assertNotNull(player.lastCards());
        assertTrue(player.toString().contains("Рука игрока:"));

        player.resetHand();
        assertEquals(0, player.getScore());
    }

    @Test
    @DisplayName("Проверка логики Dealer")
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
        assertEquals(0, dealer.getScore());
    }
}