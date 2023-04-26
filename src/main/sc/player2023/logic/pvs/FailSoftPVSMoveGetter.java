package sc.player2023.logic.pvs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.move.PossibleMoveStreamFactory;
import sc.player2023.logic.transpositiontable.SmartTransPositionTableFactory;
import sc.player2023.logic.transpositiontable.TransPositionTableFactory;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FailSoftPVSMoveGetter implements IterativeDeepeningAlphaBetaMoveGetter {

    private static final Logger log = LoggerFactory.getLogger(FailSoftPVSMoveGetter.class);

    @Override
    public Logger log() {
        return log;
    }

    public static List<Move> getShuffledPossibleMoves(@Nonnull ImmutableGameState gameState) {
        var board = gameState.getBoard();
        var currentTeam = gameState.getCurrentTeam();
        ArrayList<Move> result = PossibleMoveStreamFactory.getPossibleMoves(board, currentTeam).collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(result);
        return result;
    }

    @Override public AlphaBetaSearch algorithm() {
        return PrincipalVariationSearch::pvs;
    }

    public FailSoftPVSMoveGetter() {

    }
    TransPositionTableFactory transPositionTableFactory = new SmartTransPositionTableFactory();

    @Override
    public TransPositionTableFactory transPositionTableFactory() {
        return transPositionTableFactory;
    }

}
