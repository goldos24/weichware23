package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Team;
import sc.player2023.logic.board.BoardParser;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.score.GameScore;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Till Fransson
 * @since 18.01.2023
 */
public class PenguinOppositionRaterTest {

    private final PenguinOppositionRater rater = new PenguinOppositionRater();

    @Test
    void rateInComplexCase() {
        String boardString = """
                4 3 3 =   = = 3\s
                 - = - = = - - =\s
                - = - = - - - -\s
                 = =     = - = -\s
                - G - =     = =\s
                 - - - - = - = -\s
                = - P = = G = -\s
                 3 = =   = 3 3 4
                """;
        BoardPeek board = BoardParser.boardFromString(boardString);
        GameScore gameScore = new GameScore(2, 1);
        Team team = Team.TWO;
        ImmutableGameState gameState = new ImmutableGameState(gameScore, board, team);
        assertEquals(new Rating(-27), rater.rate(gameState), "PenguinOpposition");
    }

    @Test
    void OppositionRaterWorksLikeReachable() {
        String boardString = """
                  - -       - -\s
                 G - - P -   - -\s
                            - -\s
                 - - - - - - - -\s
                - - - - - - - -\s
                 - - - - - - - -\s
                              -\s
                 G G G P P P   -\s
                """;
        BoardPeek board = BoardParser.boardFromString(boardString);
        GameScore gameScore = new GameScore(0, 0);
        ImmutableGameState gameState = new ImmutableGameState(gameScore, board, Team.ONE);
        Rating expected = Rating.ONE.negate();
        assertEquals(expected, rater.rate(gameState), "OppositionReachableRater");
    }

    @Test
    void OppositionRaterWorksDifferentThanReachable_inSimpleCase() {
        String boardString = """
                  - - - -   - -\s
                 G - - P -   - -\s
                            - -\s
                 - - - - - - - -\s
                - - - - - - - -\s
                 - - - - - - - -\s
                              -\s
                 G G G P P P   -\s
                """;
        BoardPeek board = BoardParser.boardFromString(boardString);
        GameScore gameScore = new GameScore(0, 0);
        ImmutableGameState gameState = new ImmutableGameState(gameScore, board, Team.ONE);
        Rating expected = Rating.TWO.negate();
        assertEquals(expected, rater.rate(gameState), "OppositionReachableRater");
    }
}