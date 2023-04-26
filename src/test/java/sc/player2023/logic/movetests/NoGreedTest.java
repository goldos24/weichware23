package sc.player2023.logic.movetests;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.Logic;
import sc.player2023.logic.MoveGetter;
import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.board.BoardParser;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.pvs.AspiredPVSMoveGetter;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.score.GameScore;
import sc.plugin2023.Move;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Till Fransson
 * @since 08.01.2023
 */
public class NoGreedTest {

    @Test
    void givenBoardWithTwoFoursInCorner_whenFindBestMove_thenDoNotSacrificePenguinForCorner() {
        String boardString = """
                4 3 3 =   = = 3\s
                 - = P = = G - =\s
                - = G = - - P -\s
                 = =     = - = -\s
                - = - =     = =\s
                 - G - - = P = -\s
                = - - = = - = -\s
                 3 = =   = 3 3 4
                """;
        BoardPeek board = BoardParser.boardFromString(boardString);
        GameScore gameScore = new GameScore(3, 3);
        Team currentTeam = Team.ONE;
        ImmutableGameState gameState = new ImmutableGameState(gameScore, board, currentTeam);
        Rater rater = Logic.createCombinedRater();
        TimeMeasurer timeMeasurer = Logic.createDefaultRunningTimeMeasurer();
        MoveGetter moveGetter = new AspiredPVSMoveGetter();
        Move bestMove = moveGetter.getBestMove(gameState, rater, timeMeasurer);
        Coordinates to = new Coordinates(14, 6);
        Move unexpected = new Move(null, to);
        assertNotEquals(unexpected, bestMove, () -> "Did not expect greedy Move in Board: \n" + boardString);
    }
}
