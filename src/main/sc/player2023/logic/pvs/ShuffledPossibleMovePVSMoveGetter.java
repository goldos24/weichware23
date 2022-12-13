package sc.player2023.logic.pvs;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.MoveGetter;
import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.move.PossibleMoveStreamFactory;
import sc.player2023.logic.rating.alphabeta.AlphaBeta;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.rating.alphabeta.AlphaBetaFactory;
import sc.player2023.logic.rating.alphabeta.FailHardPVSAlphaBetaUtil;
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

    private static List<Move> getShuffledPossibleMoves(@Nonnull ImmutableGameState gameState) {
        var board = gameState.getBoard();
        var currentTeam = gameState.getCurrentTeam();
        var unmodifieableMoveList = PossibleMoveStreamFactory.getPossibleMoves(board, currentTeam).toList();
        ArrayList<Move> result = new ArrayList<>(unmodifieableMoveList);
        Collections.shuffle(result);
        return result;
    }

    private static Rating pvs(@Nonnull ImmutableGameState gameState, int depth, AlphaBeta alphaBeta,
                              @Nonnull Rater rater, @Nonnull TimeMeasurer timeMeasurer) {
        List<Move> possibleMoves = getShuffledPossibleMoves(gameState);
        if (depth < 0 || gameState.isOver() || timeMeasurer.ranOutOfTime()) {
            return rater.rate(gameState);
        }
        boolean firstChild = true;
        double score;
        double postMoveRatingFactor = getRatingFactorForNextMove(gameState);
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
        Rating alpha = Rating.NEGATIVE_INFINITY, beta = Rating.POSITIVE_INFINITY;
        AlphaBeta alphaBeta = new AlphaBeta(alpha, beta);
        Move bestMove = null;
        List<Move> possibleMoves = getShuffledPossibleMoves(gameState);
        boolean firstChild = true;
        double score;
        double postMoveRatingFactor = getRatingFactorForNextMove(gameState);
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
