package sc.player2023.logic.movetests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.Logic;
import sc.player2023.logic.TimeMeasurerFixture;
import sc.player2023.logic.board.BoardParser;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.pvs.FailSoftPVSMoveGetter;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.score.GameScore;
import sc.player2023.logic.transpositiontable.SimpleTransPositionTableFactory;
import sc.player2023.logic.transpositiontable.TransPositionTableFactory;
import sc.plugin2023.Move;

public class OpponentCutOffTest {
    TransPositionTableFactory transPositionTableFactory = new SimpleTransPositionTableFactory();

    @Test void lateGameHugeCutOff() {
        String situation = """
                4 = G 3 P     -\s
                       3   = -  \s
                  = G -   -   =\s
                 G - = =     - =\s
                P -     = =   =\s
                     P     =   P\s
                               \s
                 -       3 4 G  \s
                """;
        Move expectedMove = new Move(new Coordinates(8, 0), new Coordinates(5, 3));
        Rater rater = Logic.createCombinedRater();
        ImmutableGameState gameState = new ImmutableGameState(new GameScore(3, 0), BoardParser.boardFromString(situation), Team.TWO);
        Move got = FailSoftPVSMoveGetter.getBestMoveForDepth(gameState, rater, TimeMeasurerFixture.createAlreadyRunningInfiniteTimeMeasurer(), 5,
                transPositionTableFactory.createTransPositionTableFromDepth(5));
        assertEquals(expectedMove, got);
    }
}
