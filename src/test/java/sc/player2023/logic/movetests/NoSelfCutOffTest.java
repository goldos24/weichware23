package sc.player2023.logic.movetests;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.board.BoardParser;
import sc.player2023.logic.Logic;
import sc.player2023.logic.TimeMeasurerFixture;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.pvs.PVSMoveGetter;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.score.GameScore;
import sc.plugin2023.Move;

import static org.junit.jupiter.api.Assertions.*;
import static sc.player2023.logic.score.GameScore.START_SCORE;

public class NoSelfCutOffTest {
    Rater rater = Logic.createCombinedRater();

    Move getMoveForDepth(BoardPeek situation, Team team, GameScore gameScore, int depth) {
        return PVSMoveGetter.getBestMoveForDepth(
                new ImmutableGameState(gameScore, situation, team),
                rater,
                TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer(),
                depth
        );
    }

    @Test
    void endgameCheckForNoStupidMoves() {
        String situation = """
                P - - - P - 2 1\s
                 - - P - - - 1 1\s
                2 - - G - - - 2\s
                 - 1 - - 2 - 1 1\s
                G - - 2 - - - G\s
                 2 - 2 G 1 - - 2\s
                1 1 - - - - - 1\s
                 1 2 - 2 - P - 4\s
                """;
        Move[] unexpectedMoves = new Move[]{
                new Move(new Coordinates(14, 4), new Coordinates(15, 5)),
                new Move(new Coordinates(7, 5), new Coordinates(9, 5))
        };
        Move got = getMoveForDepth(BoardParser.boardFromString(situation), Team.ONE, START_SCORE, 0);
        for (Move unexpected : unexpectedMoves) {
            assertNotEquals(unexpected, got);
        }
    }
}
