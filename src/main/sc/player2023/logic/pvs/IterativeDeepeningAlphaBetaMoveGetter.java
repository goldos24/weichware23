package sc.player2023.logic.pvs;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.MoveGetter;
import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.move.PossibleMoveIterable;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.rating.SearchWindow;
import sc.player2023.logic.transpositiontable.TransPositionTable;
import sc.player2023.logic.transpositiontable.TransPositionTableFactory;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;

import static sc.player2023.logic.GameRuleLogic.getPossibleMoves;

public interface IterativeDeepeningAlphaBetaMoveGetter extends MoveGetter {
    Logger log();
    TransPositionTableFactory transPositionTableFactory();

    AlphaBetaSearch algorithm();

    @Override
    default Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        Move bestMove = getPossibleMoves(gameState).get(0);
        int depth = 0;
        while (!timeMeasurer.ranOutOfTime()) {
            TransPositionTable transPositionTable = transPositionTableFactory().createTransPositionTableFromDepth(depth);
            Move currentMove = getBestMoveForDepth(gameState, rater, timeMeasurer, depth, transPositionTable);
            if (currentMove == null) {
                continue;
            }
            if (timeMeasurer.ranOutOfTime() && depth != 0) {
                break;
            }
            if (depth > GameRuleLogic.BOARD_WIDTH * GameRuleLogic.BOARD_HEIGHT) {
                break;
            }
            bestMove = currentMove;
            depth++;
        }
        log().info("PVS Depth : {}", depth - 1);
        log().info("Board: {}", gameState.getBoard());
        return bestMove;
    }

    @Nullable
    default Move getBestMoveForDepth(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater,
                                           TimeMeasurer timeMeasurer, int depth,
                                           TransPositionTable transPositionTable) {
        SearchWindow searchWindow = new SearchWindow(Rating.PRIMITIVE_LOWER_BOUND, Rating.PRIMITIVE_UPPER_BOUND);
        return algorithm().search(gameState, depth, searchWindow, new ConstantPVSParameters(rater, timeMeasurer, transPositionTable,
                PossibleMoveIterable::new)).move();
    }
}
