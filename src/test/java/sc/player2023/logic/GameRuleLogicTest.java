package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;

import static org.junit.jupiter.api.Assertions.*;

class GameRuleLogicTest {

    @Test
    void canMoveTo() {
        BoardPeek board = new BoardPeek(BoardFixture.createTestBoard());
        assertTrue(GameRuleLogic.canMoveTo(board, new Coordinates(0, 0)));
        assertFalse(GameRuleLogic.canMoveTo(board, BoardFixture.firstPenguinCoords));
    }
}