package sc.player2023.logic.pvs;

import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.MoveGetter;
import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.rating.alphabeta.AlphaBeta;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.rating.alphabeta.AlphaBetaFactory;
import sc.player2023.logic.rating.alphabeta.FailHardPVSAlphaBetaUtil;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

import static sc.player2023.logic.GameRuleLogic.withMovePerformed;

public class PVSMoveGetter implements MoveGetter {

    private static final Logger log = LoggerFactory.getLogger(PVSMoveGetter.class);

    private static Rating pvs(@Nonnull ImmutableGameState gameState, int depth, AlphaBeta alphaBeta,
                              @Nonnull Rater rater, @Nonnull TimeMeasurer timeMeasurer) {
        Iterable<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        if (depth < 0 || gameState.isOver() || timeMeasurer.ranOutOfTime()) {
            return rater.rate(gameState);
        }
        boolean firstChild = true;
        double score;
        double postMoveRatingFactor = MoveGetterUtil.getRatingFactorForNextMove(gameState);
        for (Move move : possibleMoves) {
            ImmutableGameState childGameState = withMovePerformed(gameState, move);
            if (firstChild) {
                firstChild = false;
                AlphaBeta newAlphaBeta = AlphaBetaFactory.inverseOf(alphaBeta);
                Rating negated = pvs(childGameState, depth - 1, newAlphaBeta, rater, timeMeasurer).multiply(
                        postMoveRatingFactor
                );
                score = negated.rating();
            }
            else {
                AlphaBeta newAlphaBeta = AlphaBetaFactory.alphaNullWindow(alphaBeta);
                Rating negated = pvs(childGameState, depth - 1, newAlphaBeta, rater, timeMeasurer).multiply(
                        postMoveRatingFactor
                );
                score = negated.rating(); /* * search with a null window */
                if (FailHardPVSAlphaBetaUtil.canBeCutScore(alphaBeta, score)) {
                    AlphaBeta newAlphaBetaCut = new AlphaBeta(alphaBeta.beta().negate(), new Rating(-score));
                    Rating otherNegated = pvs(childGameState, depth - 1, newAlphaBetaCut, rater,
                                              timeMeasurer).multiply(
                            postMoveRatingFactor
                    );
                    score = otherNegated.rating(); /* if it failed high, do a full re-search */
                }
            }
            if(score > alphaBeta.alpha().rating()) {
                alphaBeta = AlphaBetaFactory.withNewAlpha(alphaBeta, new Rating(score));
            }
            if (FailHardPVSAlphaBetaUtil.canBeCutBeta(alphaBeta)) {
                break; /* beta cut-off */
            }
        }
        return alphaBeta.alpha();
    }

    public PVSMoveGetter() {

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
        return bestMove;
    }

    public static Move getBestMoveWithDepthOfThree(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater,
                                                   TimeMeasurer timeMeasurer) {
        return getBestMoveForDepth(gameState, rater, timeMeasurer, 3);
    }

    @Nullable
    public static Move getBestMoveForDepth(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater,
                                           TimeMeasurer timeMeasurer, int depth) {
        Rating alpha = Rating.NEGATIVE_INFINITY, beta = Rating.POSITIVE_INFINITY;
        AlphaBeta alphaBeta = new AlphaBeta(alpha, beta);
        Move bestMove = null;
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        boolean firstChild = true;
        double score;
        double postMoveRatingFactor = MoveGetterUtil.getRatingFactorForNextMove(gameState);
        for (Move move : possibleMoves) {
            ImmutableGameState childGameState = withMovePerformed(gameState, move);
            if (firstChild) {
                firstChild = false;
                AlphaBeta newAlphaBeta = AlphaBetaFactory.inverseOf(alphaBeta);
                Rating negated = pvs(childGameState, depth - 1, newAlphaBeta, rater, timeMeasurer).multiply(
                        postMoveRatingFactor
                );
                score = negated.rating();
            }
            else {
                AlphaBeta newAlphaBeta = AlphaBetaFactory.alphaNullWindow(alphaBeta);
                Rating negated = pvs(childGameState, depth - 1, newAlphaBeta, rater, timeMeasurer).multiply(
                        postMoveRatingFactor
                );
                score = negated.rating(); /* * search with a null window */
                if (FailHardPVSAlphaBetaUtil.canBeCutScore(alphaBeta, score)) {
                    AlphaBeta newAlphaBetaCut = new AlphaBeta(alphaBeta.beta().negate(), new Rating(-score));
                    Rating otherNegated = pvs(childGameState, depth - 1, newAlphaBetaCut, rater,
                            timeMeasurer).multiply(
                            postMoveRatingFactor
                    );
                    score = otherNegated.rating(); /* if it failed high, do a full re-search */
                }
            }
            if(score > alphaBeta.alpha().rating()) {
                alphaBeta = AlphaBetaFactory.withNewAlpha(alphaBeta, new Rating(score));
                bestMove = move;
            }
            if (FailHardPVSAlphaBetaUtil.canBeCutBeta(alphaBeta)) {
                break; /* beta cut-off */
            }
        }
        if (bestMove == null && possibleMoves.size() > 0) {
            return possibleMoves.get(0);
        }
        return bestMove;
    }

}
