package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.api.plugins.Vector;
import sc.player2023.Direction;
import sc.player2023.logic.board.BoardFixture;
import sc.player2023.logic.board.BoardParser;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.move.PossibleMoveStreamFactory;
import sc.plugin2023.Field;
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
        assertFalse(GameRuleLogic.canMoveTo(board, BoardFixture.FIRST_PENGUIN_COORDINATES));
    }

    @Test
    void getEmptyFieldCountFromBoard() {
        String situation = """
                P     P       G\s
                         P      \s
                              -\s
                     -   G      \s
                - G   -     =  \s
                       =       P\s
                      -        \s
                         3   G  \s
                """;
        int expected = 48;
        int got = GameRuleLogic.getEmptyFieldCountFromBoard(BoardParser.boardFromString(situation));
        assertEquals(expected, got);
    }

    @Test
    void getPossibleMoveStream() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        Set<Move> expected = PossibleMoveStreamFactory
                .getPossibleMoves(gameState.getBoard(), Team.ONE).collect(Collectors.toSet());
        Set<Move> got = GameRuleLogic.getPossibleMoveStream(gameState).collect(Collectors.toSet());
        assertEquals(expected, got);
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
    void coordsValidFalse() {
        assertFalse(GameRuleLogic.coordsValid(null));
    }

    @Test
    void nextCoord() {
        assertEquals(new Coordinates(2, 0), GameRuleLogic.nextCoord(new Coordinates(0, 0)));
        assertEquals(new Coordinates(1, 1), GameRuleLogic.nextCoord(new Coordinates(14, 0)));
    }

    @Test
    void createBoardCoordinateStream() {
        assertEquals(GameRuleLogic.createBoardCoordinateStream().filter(GameRuleLogic::coordsValid).count(), 64);
    }

    @Test
    void withMovePerformed() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        Move move = new Move(BoardFixture.FIRST_PENGUIN_COORDINATES, new Coordinates(3, 1));
        gameState = GameRuleLogic.withMovePerformed(gameState, move);
        Field expected = new Field(0, null);
        Field got = gameState.getBoard().get(BoardFixture.FIRST_PENGUIN_COORDINATES);
        assertEquals(expected, got);
        System.out.println(gameState.getBoard());
    }

    @Test
    void collinearityTestSimpleVertical() {
        Vector start = new Vector(0, 2);
        Vector end = new Vector(0, 8);
        assertTrue(GameRuleLogic.isCollinear(start, end));
    }

    @Test
    void collinearityTestSimpleHorizontal() {
        Vector start = new Vector(2, 0);
        Vector end = new Vector(8, 0);
        assertTrue(GameRuleLogic.isCollinear(start, end));
    }

    @Test
    void collinearityTestSimpleDiagonal() {
        Vector start = new Vector(1, 1);
        Vector end = new Vector(4, 4);
        assertTrue(GameRuleLogic.isCollinear(start, end));
    }

    @Test
    void collinearityTestNegativeDiagonal() {
        Vector start = new Vector(-1, -1);
        Vector end = new Vector(4, 4);
        assertTrue(GameRuleLogic.isCollinear(start, end));
    }

    @Test
    void collinearityTestNegativeDiagonalFalse() {
        Vector start = new Vector(-1, 1);
        Vector end = new Vector(4, 4);
        assertFalse(GameRuleLogic.isCollinear(start, end));
    }

    @Test
    void collinearityTestNegativeDiagonalTrue() {
        Vector start = new Vector(-1, 1);
        Vector end = new Vector(4, -4);
        assertTrue(GameRuleLogic.isCollinear(start, end));
    }



}