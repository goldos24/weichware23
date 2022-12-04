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
import sc.plugin2023.Move;

import static org.junit.jupiter.api.Assertions.*;
public class NoSelfCutOffTest {
    Rater rater = Logic.createCombinedRater();

    Move getMoveForDepth(BoardPeek situation, Team team, Integer[] teamPointsMap, int depth) {
        return PVSMoveGetter.getBestMoveForDepth(
                new ImmutableGameState(teamPointsMap, situation, team),
                rater,
                TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer(),
                depth
        );
    }

    @Test
    void endgameCheckForNoStupidMoves() {
        String situation = """
            P       P   = -\s
                 P       - -\s
            =     G       =\s
               -     =   - -\s
            G     =       G\s
             =   = G -     =\s
            - -           -\s
             - =   =   P   4\s
            """;
        Move[] unexpectedMoves = new Move[] {
                new Move(new Coordinates(14, 4), new Coordinates(15, 5)),
                new Move(new Coordinates(7, 5), new Coordinates(9, 5))
        };
        ImmutableGameState gameState = new ImmutableGameState(new Integer[]{0, 0}, BoardParser.boardFromString(situation), Team.ONE);
        Move got = getMoveForDepth(BoardParser.boardFromString(situation), Team.ONE, new Integer[]{0, 0}, 0);
        for(Move unexpected : unexpectedMoves) {
            assertNotEquals(unexpected, got);
        }
    }
}
