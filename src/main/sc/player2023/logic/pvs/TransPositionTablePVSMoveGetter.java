package sc.player2023.logic.pvs;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.MoveGetter;
import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.move.PossibleMoveStreamFactory;
import sc.player2023.logic.rating.SearchWindow;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.transpositiontable.SmartTransPositionTableFactory;
import sc.player2023.logic.transpositiontable.TransPositionTable;
import sc.player2023.logic.transpositiontable.TransPositionTableFactory;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sc.player2023.logic.GameRuleLogic.withMovePerformed;
import static sc.player2023.logic.pvs.MoveGetterUtil.getRatingFactorForNextMove;

public class TransPositionTablePVSMoveGetter implements MoveGetter {

    private static final Logger log = LoggerFactory.getLogger(TransPositionTablePVSMoveGetter.class);

    private static List<Move> getShuffledPossibleMoves(@Nonnull ImmutableGameState gameState) {
        var board = gameState.getBoard();
        var currentTeam = gameState.getCurrentTeam();
        var unmodifieableMoveList = PossibleMoveStreamFactory.getPossibleMoves(board, currentTeam).toList();
        ArrayList<Move> result = new ArrayList<>(unmodifieableMoveList);
        Collections.shuffle(result);
        return result;
    }

    private static Rating pvs(@Nonnull ImmutableGameState gameState, int depth, SearchWindow searchWindow,
                              @Nonnull Rater rater, @Nonnull TimeMeasurer timeMeasurer,
                              TransPositionTable transPositionTable) {
        if(transPositionTable.hasGameState(gameState)) {
            return transPositionTable.getRatingForGameState(gameState);
        }
        List<Move> possibleMoves = getShuffledPossibleMoves(gameState);
        if (depth < 0 || gameState.isOver() || timeMeasurer.ranOutOfTime()) {
            Rating rating = rater.rate(gameState);
            transPositionTable.add(gameState, rating);
            return rating;
        }
        boolean firstChild = true;
        double score;
        double alpha = searchWindow.lowerBound();
        double beta = searchWindow.upperBound();
        double postMoveRatingFactor = getRatingFactorForNextMove(gameState);
        for (Move move : possibleMoves) {
            ImmutableGameState childGameState = withMovePerformed(gameState, move);
            if (firstChild) {
                firstChild = false;
                SearchWindow newSearchWindow = new SearchWindow(-beta, -alpha);
                Rating negated = pvs(childGameState, depth - 1, newSearchWindow, rater, timeMeasurer, transPositionTable).multiply(
                        postMoveRatingFactor
                );
                score = negated.rating();
            }
            else {
                SearchWindow newSearchWindow = new SearchWindow(-alpha - 1, -alpha);
                Rating negated = pvs(childGameState, depth - 1, newSearchWindow, rater, timeMeasurer, transPositionTable).multiply(
                        postMoveRatingFactor
                );
                score = negated.rating(); /* * search with a null window */
                if (searchWindow.canBeCutScore(score)) {
                    SearchWindow newSearchWindowCut = new SearchWindow(-beta, -score);
                    Rating otherNegated = pvs(childGameState, depth - 1, newSearchWindowCut, rater,
                                              timeMeasurer, transPositionTable).multiply(
                            postMoveRatingFactor
                    );
                    score = otherNegated.rating(); /* if it failed high, do a full re-search */
                }
            }
            alpha = Math.max(alpha, score);
            SearchWindow newSearchWindow = new SearchWindow(alpha, beta);
            if (newSearchWindow.canBeCutBeta()) {
                break; /* beta cut-off */
            }
        }
        return new Rating(alpha);
    }

    public TransPositionTablePVSMoveGetter() {

    }

    TransPositionTableFactory transPositionTableFactory = new SmartTransPositionTableFactory();

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        Move bestMove = null;
        int depth = 0;
        while (!timeMeasurer.ranOutOfTime() || depth == 0) {
            TransPositionTable transPositionTable = transPositionTableFactory.createTransPositionTableFromDepth(depth);
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
        log.info("PVS Depth : {}", depth - 1);
        log.info("Board: {}", gameState.getBoard());
        return bestMove;
    }
    @Nullable
    public static Move getBestMoveForDepth(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater,
                                           TimeMeasurer timeMeasurer, int depth,
                                           TransPositionTable transPositionTable) {
        double alpha = Double.NEGATIVE_INFINITY, score, beta = Double.POSITIVE_INFINITY;
        SearchWindow searchWindow = new SearchWindow(alpha, beta);
        Move bestMove = null;
        List<Move> possibleMoves = getShuffledPossibleMoves(gameState);
        boolean firstChild = true;
        double postMoveRatingFactor = getRatingFactorForNextMove(gameState);
        for (Move move : possibleMoves) {
            ImmutableGameState childGameState = withMovePerformed(gameState, move);
            if (firstChild) {
                firstChild = false;
                SearchWindow newSearchWindow = new SearchWindow(-beta, -alpha);
                Rating negated = pvs(childGameState, depth - 1, newSearchWindow, rater,
                                     timeMeasurer, transPositionTable).multiply(
                        postMoveRatingFactor
                );
                score = negated.rating();
            }
            else {
                SearchWindow newSearchWindow = new SearchWindow(-alpha - 1, -alpha);
                Rating negated = pvs(childGameState, depth - 1, newSearchWindow, rater,
                                     timeMeasurer, transPositionTable).multiply(
                        postMoveRatingFactor
                );
                score = negated.rating(); /* * search with a null window */
                if (searchWindow.canBeCutScore(score)) {
                    SearchWindow newSearchWindowCut = new SearchWindow(-beta, -score);
                    Rating otherNegated = pvs(childGameState, depth - 1, newSearchWindowCut, rater, timeMeasurer,
                                              transPositionTable).multiply(
                            postMoveRatingFactor
                    );
                    score = otherNegated.rating(); /* if it failed high, do a full re-search */
                }
            }
            if (score > alpha) {
                bestMove = move;
            }
            alpha = Math.max(alpha, score);
            SearchWindow newSearchWindow = new SearchWindow(alpha, beta);
            if (newSearchWindow.canBeCutBeta()) {
                break; /* beta cut-off */
            }
        }
        if (bestMove == null && possibleMoves.size() > 0) {
            return possibleMoves.get(0);
        }
        return bestMove;
    }

}
