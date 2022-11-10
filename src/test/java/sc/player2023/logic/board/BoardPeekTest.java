package sc.player2023.logic.board;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.BoardFixture;
import sc.player2023.logic.GameRuleLogic;
import sc.plugin2023.Board;
import sc.plugin2023.Field;
import sc.plugin2023.GameState;
import sc.plugin2023.Move;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void withMovePerformedSetPenguin() {
        Board emptyBoard = BoardFixture.createTestBoardOneFishPerField();
        BoardPeek emptyImmutableBoard = new BoardPeek(emptyBoard.clone());
        Move move = new Move(null, new Coordinates(0, 0));
        GameState emptyGameState = new GameState(emptyBoard);
        emptyGameState.performMove(move);
        BoardPeek boardWithMovePerformed = emptyImmutableBoard.withMovePerformed(move, Team.ONE);
        GameRuleLogic.createBoardCoordinateStream().forEach(coordinates -> assertEquals(emptyGameState.getBoard().get(coordinates), boardWithMovePerformed.get(coordinates)));
    }

    @Test
    void withMovePerformedMovePenguin() {
        Board emptyBoard = BoardFixture.createTestBoardOneFishPerFieldAndAllPenguinsOnBoard();
        BoardPeek emptyImmutableBoard = new BoardPeek(emptyBoard.clone());
        Coordinates from = new Coordinates(1, 1);
        Coordinates to = new Coordinates(5, 5);
        Move move = new Move(from, to);
        GameState emptyGameState = new GameState(emptyBoard);
        emptyGameState.performMove(move);
        BoardPeek boardWithMovePerformed = emptyImmutableBoard.withMovePerformed(move, Team.ONE);
        Board expectedBoard = emptyGameState.getBoard();
        BoardPeek expected = new BoardPeek(expectedBoard);
        assertEquals(expected, boardWithMovePerformed, "withMovePerformed");
    }

    @Test
    void getBitSetPositionMask() {
        long expected = 1 << 9;
        long got = BoardPeek.getBitSetPositionMask(1, 1);
        assertEquals(expected, got);
    }

    @Test
    void getBitFrom8x8BitSet() {
        long bitset = 0x10000;
        assertTrue(BoardPeek.getBitFrom8x8BitSet(0, 2, bitset));
    }

    @Test
    void bitSet8x8WithSingleBitChanged() {
        long initialBitSet = 0x1L;
        long expectedResult = 0x10000L;
        long got = BoardPeek.bitSet8x8WithSingleBitChanged(0, 0, initialBitSet, false);
        got = BoardPeek.bitSet8x8WithSingleBitChanged(0, 2, got, true);
        assertEquals(expectedResult, got);
    }
}