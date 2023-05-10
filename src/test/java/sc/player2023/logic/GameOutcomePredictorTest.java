package sc.player2023.logic;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Team;
import sc.player2023.logic.board.BoardParser;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.score.GameScore;
import sc.player2023.logic.score.Score;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameOutcomePredictorTest {

    @Test
    void givenGameStateInFavorOfTeamTwo_thenGameOutcomePredictorPredictsPlausibleResult() {
        String boardString = """
                 3     G   =  \s
                P     3   = - -\s
               G         = P =\s
                -   -   - = -  \s
                   P     -   -\s
                =   =       P =\s
               G       3     =\s
                  =     G      \s
               """;
        BoardPeek board = BoardParser.boardFromString(boardString);
        ImmutableGameState gameState = new ImmutableGameState(new GameScore(new Score(32), new Score(31)), board, Team.TWO);
        GameOutcomePrediction prediction = GameOutcomePredictor.doPredictionForGameState(gameState, 100);
        assertEquals(Team.TWO, prediction.getLikelyWinner());
    }
}
