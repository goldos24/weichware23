package sc.player2023.logic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

import static sc.player2023.logic.GameRuleLogic.withMovePerformed;

public class PVSMoveGetter implements MoveGetter {

    private static final Logger log = LoggerFactory.getLogger(PVSMoveGetter.class);

    private static Rating pvs(@Nonnull ImmutableGameState gameState, int depth, double alpha, double beta,
                              @Nonnull Rater rater, @Nonnull TimeMeasurer timeMeasurer) {
        Iterable<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        if (depth < 0 || gameState.isOver() || timeMeasurer.ranOutOfTime()) {
            return rater.rate(gameState);
        }
        boolean firstChild = true;
        double score;
        for (Move move : possibleMoves) {
            ImmutableGameState childGameState = withMovePerformed(gameState, move);
            if (firstChild) {
                firstChild = false;
                Rating negated = pvs(childGameState, depth - 1, -beta, -alpha, rater, timeMeasurer).negate();
                score = negated.rating();
            }
            else {
                Rating negated = pvs(childGameState, depth - 1, -alpha - 1, -alpha, rater, timeMeasurer).negate();
                score = negated.rating(); /* * search with a null window */
                if (alpha < score && score < beta) {
                    Rating otherNegated = pvs(childGameState, depth - 1, -beta, -score, rater, timeMeasurer).negate();
                    score = otherNegated.rating(); /* if it failed high, do a full re-search */
                }
            }
            alpha = Math.max(alpha, score);
            if (alpha >= beta) {
                break; /* beta cut-off */
            }
        }
        return new Rating(alpha);
    }

    public PVSMoveGetter() {

    }

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        Move bestMove = null;
        int depth = 0;
        while (!timeMeasurer.ranOutOfTime() || depth == 0) {
            Move currentMove = getBestMoveForDepth(gameState, rater, timeMeasurer, depth);
            if(currentMove == null) {
                continue;
            }
            if(timeMeasurer.ranOutOfTime() && depth != 0) {
                break;
            }
            if(depth > GameRuleLogic.BOARD_WIDTH * GameRuleLogic.BOARD_HEIGHT)
                break;
            bestMove = currentMove;
            depth++;
        }
        log.info("PVS Depth : {}", depth-1);
        return bestMove;
    }

    @Nullable
    static Move getBestMoveForDepth(@NotNull ImmutableGameState gameState, @NotNull Rater rater, TimeMeasurer timeMeasurer, int depth) {
        Rating highestRating = Rating.NEGATIVE_INFINITY;
        Move bestMove = null;
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        for (Move move : possibleMoves) {
            ImmutableGameState childGameState = withMovePerformed(gameState, move);
            Rating currentRating = pvs(childGameState, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, rater, timeMeasurer)
                    .negate();
            if (currentRating.isGreaterThan(highestRating)) {
                highestRating = currentRating;
                bestMove = move;
            }
        }
        if (bestMove == null && possibleMoves.size() > 0) {
            return possibleMoves.get(0);
        }
        return bestMove;
    }

}
