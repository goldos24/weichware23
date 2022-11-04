package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.board.BoardPeek;
import sc.plugin2023.GameState;
import sc.plugin2023.Move;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PossibleMoveStreamFactoryTest {
    @Test
    void getPossibleMovesForPenguin() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        GameState mutableGameState = new GameState(BoardFixture.createTestBoard());
        Coordinates penguinPos = BoardFixture.firstPenguinCoords;
        Stream<Move> expectedMoveStream =
                mutableGameState.getPossibleMoves().stream().filter(move -> Objects.equals(move.getFrom(), penguinPos));
        Set<Move> expected = expectedMoveStream.collect(Collectors.toSet());
        BoardPeek board = gameState.getBoard();
        Set<Move> got = PossibleMoveStreamFactory.getPossibleMovesForPenguin(board, penguinPos).collect(Collectors.toSet());
        assertEquals(expected, got);
    }

    @Test
    void getPossibleMoves() {
        ImmutableGameState gameState = GameStateFixture.createTestGameState();
        GameState mutableGameState = new GameState(BoardFixture.createTestBoard());
        possibleMoveEquivalenceCheck(gameState, mutableGameState);
    }

    @Test
    void getPossibleMovesAtBeginning() {
        ImmutableGameState gameState = GameStateFixture.createTestGameStateOneFishPerField();
        GameState mutableGameState = new GameState(BoardFixture.createTestBoardOneFishPerField());
        possibleMoveEquivalenceCheck(gameState, mutableGameState);
    }

    private static void possibleMoveEquivalenceCheck(ImmutableGameState gameState, GameState mutableGameState) {
        Stream<Move> expectedMoveStream =
                mutableGameState.getPossibleMoves().stream();
        Set<Move> expected = expectedMoveStream.collect(Collectors.toSet());
        BoardPeek board = gameState.getBoard();
        Set<Move> got = PossibleMoveStreamFactory.getPossibleMoves(board, Team.ONE).collect(Collectors.toSet());
        assertEquals(expected, got);
    }
}