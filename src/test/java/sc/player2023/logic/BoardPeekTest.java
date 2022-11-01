package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.plugin2023.Board;
import sc.plugin2023.Field;
import sc.plugin2023.GameState;
import sc.plugin2023.Move;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BoardPeekTest {

    @Test
    void get() {
        BoardPeek board = new BoardPeek(BoardFixture.createTestBoard());
        Field field = board.get(new Coordinates(0, 0));
        assertEquals(field.getFish(), BoardFixture.DEFAULT_FISH_COUNT);
    }

    @Test
    void fromStreams() {
        // Simple copy
        BoardPeek initialBoard = new BoardPeek(BoardFixture.createTestBoard());
        Stream<Coordinates> coordStream = GameRuleLogic.createBoardCoordinateStream();
        BoardPeek streamCopiedBoard = BoardPeek.fromStreams(coordStream.map(initialBoard::get));
        assertEquals(initialBoard, streamCopiedBoard);
    }

    @Test
    void withMovePerformed() {
        Board emptyBoard = BoardFixture.createTestBoardOneFishPerField();
        BoardPeek emptyImmutableBoard = new BoardPeek(emptyBoard.clone());
        Move move = new Move(null, new Coordinates(0, 0));
        GameState emptyGameState = new GameState(emptyBoard);
        emptyGameState.performMove(move);
        BoardPeek boardWithMovePerformed = emptyImmutableBoard.withMovePerformed(move, Team.ONE);
        GameRuleLogic.createBoardCoordinateStream().forEach(coordinates -> assertEquals(emptyGameState.getBoard().get(coordinates), boardWithMovePerformed.get(coordinates)));
    }
}