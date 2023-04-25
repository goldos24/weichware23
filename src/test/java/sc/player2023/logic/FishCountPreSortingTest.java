package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.board.BoardParser;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.move.FishCountPreSorting;
import sc.player2023.logic.score.GameScore;
import sc.plugin2023.Move;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Till Fransson
 * @since 25.04.2023
 */
public class FishCountPreSortingTest {

    private static final Logger log = getLogger(FishCountPreSortingTest.class);


    @Test
    void givenGameState_whenGetPossibleMoves_thenReturnPossibleMovesInAnyOrder() {
        ImmutableGameState testGameState = GameStateFixture.createTestGameState();
        List<Move> actualPossibleMoves = FishCountPreSorting.getPossibleMoves(testGameState);
        Set<Move> actual = new HashSet<>(actualPossibleMoves);
        List<Move> expectedPossibleMoves = GameRuleLogic.getPossibleMoves(testGameState);
        Set<Move> expected = new HashSet<>(expectedPossibleMoves);
        log.info("moves: {}", actual);
        log.info("board: {}", testGameState.getBoard());
        assertEquals(expected, actual, "getPossibleMoves");
    }
    
    @Test
    void givenGameState_whenGetPossibleMoves_thenReturnFirstMoveWithHighestFish() {
        String boardString = """
                3 - G G G G - -\s
                 - - P P P P - -\s
                - - - - - - - -\s
                 - - - - - - - -\s
                - - - - - - - -\s
                 - - - - - - - -\s
                - - - - - - - -\s
                 - - - - - - - -\s
                                
                """;
        BoardPeek board = BoardParser.boardFromString(boardString);
        GameScore gameScore = new GameScore(0, 0);
        ImmutableGameState gameState = new ImmutableGameState(gameScore, board, Team.ONE);
        List<Move> actualPossibleMoves = FishCountPreSorting.getPossibleMoves(gameState);
        Move expected = new Move(new Coordinates(4, 0), new Coordinates(0, 0));
        Move actual = actualPossibleMoves.get(0);
        assertEquals(expected, actual);
    }
    
}
