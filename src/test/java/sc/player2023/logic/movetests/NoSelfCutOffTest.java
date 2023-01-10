package sc.player2023.logic.movetests;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.Logic;
import sc.player2023.logic.TimeMeasurerFixture;
import sc.player2023.logic.board.BoardParser;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.pvs.FailSoftPVSMoveGetter;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.score.GameScore;
import sc.player2023.logic.transpositiontable.SmartTransPositionTableFactory;
import sc.player2023.logic.transpositiontable.TransPositionTable;
import sc.player2023.logic.transpositiontable.TransPositionTableFactory;
import sc.plugin2023.Move;
import static sc.player2023.logic.MoveGetters.FAIL_SOFT_PVS_MOVE_GETTER;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NoSelfCutOffTest {
    Rater rater = Logic.createCombinedRater();

    TransPositionTableFactory transPositionTableFactory = new SmartTransPositionTableFactory();


    Move getMoveForDepth(BoardPeek situation, Team team, GameScore teamPointsMap, int depth) {
        TransPositionTable table = transPositionTableFactory.createTransPositionTableFromDepth(5);
        return FAIL_SOFT_PVS_MOVE_GETTER.getBestMoveForDepth(
                new ImmutableGameState(teamPointsMap, situation, team),
                rater,
                TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer(),
                depth,
                table
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
        Move[] unexpectedMoves = new Move[]{
                new Move(new Coordinates(14, 4), new Coordinates(15, 5)),
                new Move(new Coordinates(7, 5), new Coordinates(9, 5))
        };
        Move got = getMoveForDepth(BoardParser.boardFromString(situation), Team.ONE, new GameScore(0, 0), 0);
        for (Move unexpected : unexpectedMoves) {
            assertNotEquals(unexpected, got);
        }
    }
}
