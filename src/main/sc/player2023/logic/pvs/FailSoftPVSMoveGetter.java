package sc.player2023.logic.pvs;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.MoveGetter;
import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.move.PossibleMoveStreamFactory;
import sc.player2023.logic.rating.AlphaBeta;
import sc.player2023.logic.rating.RatingWithMove;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.transpositiontable.SimpleTransPositionTableFactory;
import sc.player2023.logic.transpositiontable.TransPositionTable;
import sc.player2023.logic.transpositiontable.TransPositionTableFactory;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sc.player2023.logic.GameRuleLogic.withMovePerformed;
import static sc.player2023.logic.pvs.MoveGetterUtil.getRatingFactorForNextMove;

public class FailSoftPVSMoveGetter implements MoveGetter {

    private static final Logger log = LoggerFactory.getLogger(FailSoftPVSMoveGetter.class);

    private static List<Move> getShuffledPossibleMoves(@Nonnull ImmutableGameState gameState) {
        var board = gameState.getBoard();
        var currentTeam = gameState.getCurrentTeam();
        var unmodifieableMoveList = PossibleMoveStreamFactory.getPossibleMoves(board, currentTeam).toList();
        ArrayList<Move> result = new ArrayList<>(unmodifieableMoveList);
        Collections.shuffle(result);
        return result;
    }


    private static RatingWithMove pvs(@Nonnull ImmutableGameState gameState, int depth, AlphaBeta alphaBeta,
                              @Nonnull Rater rater, @Nonnull TimeMeasurer timeMeasurer,
                              TransPositionTable transPositionTable) {
        if(transPositionTable.hasGameState(gameState)) {
            return new RatingWithMove(null, transPositionTable.getRatingForGameState(gameState));
        }
        List<Move> possibleMoves = getShuffledPossibleMoves(gameState);
        if (depth < 0 || gameState.isOver() || possibleMoves.isEmpty() || timeMeasurer.ranOutOfTime()) {
            Rating rating = rater.rate(gameState);
            transPositionTable.add(gameState, rating);
            return new RatingWithMove(null, rating);
        }
        boolean firstChild = true;
        Move bestMove = null;
        double score, bestScore = Double.NEGATIVE_INFINITY;
        double alpha = alphaBeta.alpha();
        double beta = alphaBeta.beta();
        double postMoveRatingFactor = getRatingFactorForNextMove(gameState);
        for (Move move : possibleMoves) {
            ImmutableGameState childGameState = withMovePerformed(gameState, move);
            if (firstChild) {
                firstChild = false;
                AlphaBeta newAlphaBeta = new AlphaBeta(-beta, -alpha);
                Rating negated = pvs(childGameState, depth - 1, newAlphaBeta, rater, timeMeasurer, transPositionTable)
                        .rating().multiply(
                        postMoveRatingFactor
                );
                score = negated.rating();
                bestScore = score;
                bestMove = move;
                if(score > alpha ) {
                    if (score >= beta)
                        break;
                    alpha = score;
                }
            }
            else {
                AlphaBeta newAlphaBeta = new AlphaBeta(-alpha - 1, -alpha);
                Rating negated = pvs(childGameState, depth - 1, newAlphaBeta, rater, timeMeasurer, transPositionTable).
                        rating().multiply(
                        postMoveRatingFactor
                );
                score = negated.rating(); /* * search with a null window */
                if (score > alpha && score < beta) {
                    AlphaBeta newAlphaBetaCut = new AlphaBeta(-beta, -alpha);
                    Rating otherNegated = pvs(childGameState, depth - 1, newAlphaBetaCut, rater,
                                              timeMeasurer, transPositionTable).
                            rating().multiply(
                            postMoveRatingFactor
                    );
                    score = otherNegated.rating(); /* if it failed high, do a full re-search */
                    if(score > alpha) {
                        alpha = score;
                    }
                }
                if(score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                    if(score >= beta)
                        break; // Beta cut-off
                }
            }
        }
        return new RatingWithMove(bestMove, new Rating(bestScore));
    }

    public FailSoftPVSMoveGetter() {

    }

    TransPositionTableFactory transPositionTableFactory = new SimpleTransPositionTableFactory();

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
        return pvs(gameState, depth, new AlphaBeta(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), rater, timeMeasurer, transPositionTable).move();
    }

}
