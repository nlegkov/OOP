package ru.nsu.legkov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.nsu.legkov.coregame.GameState;

/** Тесты состояния игры. */
class GameStateTest {
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
}
