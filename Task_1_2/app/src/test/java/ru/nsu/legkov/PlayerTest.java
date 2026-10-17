package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.legkov.cards.Rank;
import ru.nsu.legkov.entity.Player;

/** Тесты действий игрока. */
class PlayerTest {
    @Test
    void playerTakesCardsAndResets() {
        Player player = new Player();
        FixedDeck deck = new FixedDeck(TestCards.card(Rank.TEN), TestCards.card(Rank.SEVEN));
        player.takeCard(deck);
        player.takeCard(deck);
        assertEquals(17, player.getScore());
        assertEquals(2, player.getHand().getCards().size());
        assertTrue(player.toString().contains("Очки: 17"));
        player.resetHand();
        assertEquals(0, player.getScore());
        assertEquals("Рука пуста", player.lastCards());
    }
}
