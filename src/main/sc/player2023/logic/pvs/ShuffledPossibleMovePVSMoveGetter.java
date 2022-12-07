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
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static sc.player2023.logic.GameRuleLogic.withMovePerformed;

public class ShuffledPossibleMovePVSMoveGetter implements MoveGetter {

    private static double getRatingFactorForNextMove(@Nonnull ImmutableGameState gameState) {
        if(GameRuleLogic.anyPossibleMovesForPlayer(gameState.getBoard(), gameState.getCurrentTeam().opponent())) {
            return -1;
        }
        return 1;
    }

    private static final Logger log = LoggerFactory.getLogger(ShuffledPossibleMovePVSMoveGetter.class);

    private static Rating pvs(@Nonnull ImmutableGameState gameState, int depth, AlphaBeta alphaBeta,
                              @Nonnull Rater rater, @Nonnull TimeMeasurer timeMeasurer) {
        List<Move> possibleMoves = new ArrayList<>(PossibleMoveStreamFactory.getPossibleMoves(gameState.getBoard(), gameState.getCurrentTeam()).toList());
        Collections.shuffle(possibleMoves);
        if (depth < 0 || gameState.isOver() || timeMeasurer.ranOutOfTime()) {
            return rater.rate(gameState);
        }
        boolean firstChild = true;
        double score;
        double alpha = alphaBeta.alpha();
        double beta = alphaBeta.beta();
        double postMoveRatingFactor = getRatingFactorForNextMove(gameState);
        for (Move move : possibleMoves) {
            ImmutableGameState childGameState = withMovePerformed(gameState, move);
            if (firstChild) {
                firstChild = false;
                AlphaBeta newAlphaBeta = new AlphaBeta(-beta, -alpha);
                Rating negated = pvs(childGameState, depth - 1, newAlphaBeta, rater, timeMeasurer).multiply(
                        postMoveRatingFactor
                );
                score = negated.rating();
            }
            else {
                AlphaBeta newAlphaBeta = new AlphaBeta(-alpha - 1, -alpha);
                Rating negated = pvs(childGameState, depth - 1, newAlphaBeta, rater, timeMeasurer).multiply(
                        postMoveRatingFactor
                );
                score = negated.rating(); /* * search with a null window */
                if (alphaBeta.canBeCutScore(score)) {
                    AlphaBeta newAlphaBetaCut = new AlphaBeta(-beta, -score);
                    Rating otherNegated = pvs(childGameState, depth - 1, newAlphaBetaCut, rater,
                                              timeMeasurer).multiply(
                            postMoveRatingFactor
                    );
                    score = otherNegated.rating(); /* if it failed high, do a full re-search */
                }
            }
            alpha = Math.max(alpha, score);
            AlphaBeta newAlphaBeta = new AlphaBeta(alpha, beta);
            if (newAlphaBeta.canBeCutBeta()) {
                break; /* beta cut-off */
            }
        }
        return new Rating(alpha);
    }

    public ShuffledPossibleMovePVSMoveGetter() {

    }

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        Move bestMove = null;
        int depth = 0;
        while (!timeMeasurer.ranOutOfTime() || depth == 0) {
            Move currentMove = getBestMoveForDepth(gameState, rater, timeMeasurer, depth);
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
                                           TimeMeasurer timeMeasurer, int depth) {
        double alpha = Double.NEGATIVE_INFINITY, score, beta = Double.POSITIVE_INFINITY;
        AlphaBeta alphaBeta = new AlphaBeta(alpha, beta);
        Move bestMove = null;
        List<Move> possibleMoves = new ArrayList<>(PossibleMoveStreamFactory.getPossibleMoves(gameState.getBoard(), gameState.getCurrentTeam()).toList());
        Collections.shuffle(possibleMoves);
        boolean firstChild = true;
        double postMoveRatingFactor = getRatingFactorForNextMove(gameState);
        for (Move move : possibleMoves) {
            ImmutableGameState childGameState = withMovePerformed(gameState, move);
            if (firstChild) {
                firstChild = false;
                AlphaBeta newAlphaBeta = new AlphaBeta(-beta, -alpha);
                Rating negated = pvs(childGameState, depth - 1, newAlphaBeta, rater,
                                     timeMeasurer).multiply(
                        postMoveRatingFactor
                );
                score = negated.rating();
            }
            else {
                AlphaBeta newAlphaBeta = new AlphaBeta(-alpha - 1, -alpha);
                Rating negated = pvs(childGameState, depth - 1, newAlphaBeta, rater,
                                     timeMeasurer).multiply(
                        postMoveRatingFactor
                );
                score = negated.rating(); /* * search with a null window */
                if (alphaBeta.canBeCutScore(score)) {
                    AlphaBeta newAlphaBetaCut = new AlphaBeta(-beta, -score);
                    Rating otherNegated = pvs(childGameState, depth - 1, newAlphaBetaCut, rater, timeMeasurer).multiply(
                            postMoveRatingFactor
                    );
                    score = otherNegated.rating(); /* if it failed high, do a full re-search */
                }
            }
            if (score > alpha) {
                bestMove = move;
            }
            alpha = Math.max(alpha, score);
            AlphaBeta newAlphaBeta = new AlphaBeta(alpha, beta);
            if (newAlphaBeta.canBeCutBeta()) {
                break; /* beta cut-off */
            }
        }
        if (bestMove == null && possibleMoves.size() > 0) {
            return possibleMoves.get(0);
        }
        return bestMove;
    }

}
