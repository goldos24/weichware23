package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.plugin2023.Move;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GameRuleLogicTest {

    @Test
    void canMoveTo() {
        BoardPeek board = new BoardPeek(BoardFixture.createTestBoard());
        assertTrue(GameRuleLogic.canMoveTo(board, new Coordinates(0, 0)));
        assertFalse(GameRuleLogic.canMoveTo(board, BoardFixture.firstPenguinCoords));
    }

    @Test
    void getPossibleMoveStream() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        Set<Move> expected = PossibleMoveStreamFactory.getPossibleMoves(gameState.getBoard(), Team.ONE).collect(Collectors.toSet());
        Set<Move> got = GameRuleLogic.getPossibleMoveStream(gameState).collect(Collectors.toSet());
        assertEquals(expected,got);
    }

    @Test
    void allPenguinsPlaced() {
        BoardPeek emptyBoard = new BoardPeek(BoardFixture.createTestBoardOneFishPerField());
        BoardPeek normalBoard = new BoardPeek(BoardFixture.createTestBoard());
        assertFalse(GameRuleLogic.allPenguinsPlaced(emptyBoard, Team.ONE));
        assertTrue(GameRuleLogic.allPenguinsPlaced(normalBoard, Team.ONE));
    }

    @Test
    void coordsValid() {
        List<Coordinates> invalidCoords = List.of(new Coordinates(-2, 0), new Coordinates(2, 1),
                new Coordinates(1, 2), new Coordinates(16, 0));
        for(Coordinates invalidCoord : invalidCoords) {
            assertFalse(GameRuleLogic.coordsValid(invalidCoord));
        }
        List<Coordinates> validCoords = List.of(new Coordinates(2, 0), new Coordinates(2, 2),
                new Coordinates(1, 1), new Coordinates(15, 1));
        for(Coordinates validCoord : validCoords) {
            assertTrue(GameRuleLogic.coordsValid(validCoord));
        }
    }
    @Test
    void nextCoord() {
        assertEquals(new Coordinates(2, 0), GameRuleLogic.nextCoord(new Coordinates(0,0)));
        assertEquals(new Coordinates(1, 1), GameRuleLogic.nextCoord(new Coordinates(14,0)));
    }

    @Test
    void createBoardCoordinateStream() {
        assertEquals(GameRuleLogic.createBoardCoordinateStream().filter(GameRuleLogic::coordsValid).count(), 64);
    }
}