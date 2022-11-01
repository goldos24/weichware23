package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.plugin2023.Move;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PossibleMoveIterableTest {
    @Test
    void iterator() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        List<Move> expected = GameRuleLogic.getPossibleMoveStream(gameState).toList();
        List<Move> got = new ArrayList<>();
        PossibleMoveIterable iterable = new PossibleMoveIterable(gameState);
        for (Move move : iterable) {
            got.add(move);
        }
        assertEquals(expected, got);
    }
}