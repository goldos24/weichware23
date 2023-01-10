package sc.player2023.logic.pvs;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.MoveGetter;
import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.move.PossibleMoveGenerator;
import sc.player2023.logic.move.PossibleMoveStreamFactory;
import sc.player2023.logic.rating.*;
import sc.player2023.logic.transpositiontable.SmartTransPositionTableFactory;
import sc.player2023.logic.transpositiontable.TransPositionTable;
import sc.player2023.logic.transpositiontable.TransPositionTableFactory;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sc.player2023.logic.GameRuleLogic.getPossibleMoves;
import static sc.player2023.logic.GameRuleLogic.withMovePerformed;
import static sc.player2023.logic.pvs.MoveGetterUtil.getRatingFactorForNextMove;
import static sc.player2023.logic.rating.RatingUtil.isInSearchWindow;

public class FailSoftPVSMoveGetter implements IterativeDeepeningAlphaBetaMoveGetter {

    private static final Logger log = LoggerFactory.getLogger(FailSoftPVSMoveGetter.class);

    @Override
    public Logger log() {
        return log;
    }

    public static List<Move> getShuffledPossibleMoves(@Nonnull ImmutableGameState gameState) {
        var board = gameState.getBoard();
        var currentTeam = gameState.getCurrentTeam();
        var unmodifieableMoveList = PossibleMoveStreamFactory.getPossibleMoves(board, currentTeam).toList();
        ArrayList<Move> result = new ArrayList<>(unmodifieableMoveList);
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
