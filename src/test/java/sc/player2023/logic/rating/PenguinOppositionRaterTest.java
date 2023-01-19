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

    @Test
    void addReachableCoordsToSet() {
    }

    @Test
    void rate() {
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
        PenguinOppositionRater rater = new PenguinOppositionRater();
        assertEquals(new Rating(22), rater.rate(gameState), "PenguinOpposition");
    }
}