package sc.player2023.logic.pvs;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.MoveGetter;
import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.move.PossibleMoveGenerator;
import sc.player2023.logic.move.PossibleMoveIterable;
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

public class FailSoftPVSMoveGetter implements MoveGetter {

    private static final Logger log = LoggerFactory.getLogger(FailSoftPVSMoveGetter.class);

    public static List<Move> getShuffledPossibleMoves(@Nonnull ImmutableGameState gameState) {
        var board = gameState.getBoard();
        var currentTeam = gameState.getCurrentTeam();
        var unmodifieableMoveList = PossibleMoveStreamFactory.getPossibleMoves(board, currentTeam).toList();
        ArrayList<Move> result = new ArrayList<>(unmodifieableMoveList);
        Collections.shuffle(result);
        return result;
    }


    public static RatedMove pvs(@Nonnull ImmutableGameState gameState, int depth, SearchWindow searchWindow,
                                @Nonnull Rater rater, @Nonnull TimeMeasurer timeMeasurer,
                                TransPositionTable transPositionTable, PossibleMoveGenerator moveGenerator) {
        if (transPositionTable.hasGameState(gameState)) {
            return new RatedMove(transPositionTable.getRatingForGameState(gameState), null);
        }
        Iterable<Move> possibleMoves = moveGenerator.getPossibleMoves(gameState);
        boolean isEmpty = !possibleMoves.iterator().hasNext();
        if (depth < 0 || gameState.isOver() || isEmpty || timeMeasurer.ranOutOfTime()) {
            Rating rating = rater.rate(gameState);
            transPositionTable.add(gameState, rating);
            return new RatedMove(rating, null);
        }
        boolean firstChild = true;
        Move bestMove = null;
        double score, bestScore = Double.NEGATIVE_INFINITY;
        double lowerBound = searchWindow.lowerBound();
        double upperBound = searchWindow.upperBound();
        double postMoveRatingFactor = getRatingFactorForNextMove(gameState);
        for (Move move : possibleMoves) {
            ImmutableGameState childGameState = withMovePerformed(gameState, move);
            if (firstChild) {
                firstChild = false;
                SearchWindow newSearchWindow = new SearchWindow(-upperBound, -lowerBound);
                Rating negated = pvs(childGameState, depth - 1, newSearchWindow, rater, timeMeasurer,
                                     transPositionTable, PossibleMoveIterable::new)
                        .rating().multiply(
                                postMoveRatingFactor
                        );
                score = negated.rating();
                bestScore = score;
                bestMove = move;
                if (score > lowerBound) {
                    if (score >= upperBound) {
                        break;
                    }
                    lowerBound = score;
                }
            }
            else {
                SearchWindow newSearchWindow = new SearchWindow(-lowerBound - 1, -lowerBound);
                Rating negated = pvs(childGameState, depth - 1, newSearchWindow, rater, timeMeasurer,
                                     transPositionTable, PossibleMoveIterable::new).
                        rating().multiply(
                                postMoveRatingFactor
                        );
                score = negated.rating(); /* * search with a null window */
                if (score > lowerBound && score < upperBound) {
                    SearchWindow newSearchWindowCut = new SearchWindow(-upperBound, -lowerBound);
                    Rating otherNegated = pvs(childGameState, depth - 1, newSearchWindowCut, rater,
                                              timeMeasurer, transPositionTable, PossibleMoveIterable::new).
                            rating().multiply(
                                    postMoveRatingFactor
                            );
                    score = otherNegated.rating(); /* if it failed high, do a full re-search */
                    if (score > lowerBound) {
                        lowerBound = score;
                    }
                }
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                    if (score >= upperBound) {
                        break; // Beta cut-off
                    }
                }
            }
        }
        Rating rating = new Rating(bestScore);
        if(isInSearchWindow(new SearchWindow(lowerBound, upperBound), rating)) {
            transPositionTable.add(gameState, rating);
        }
        return new RatedMove(rating, bestMove);
    }

    public FailSoftPVSMoveGetter() {

    }

    TransPositionTableFactory transPositionTableFactory = new SmartTransPositionTableFactory();

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        Move bestMove = getPossibleMoves(gameState).get(0);
        int depth = 0;
        while (!timeMeasurer.ranOutOfTime()) {
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
        SearchWindow searchWindow = new SearchWindow(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        return pvs(gameState, depth, searchWindow, rater, timeMeasurer, transPositionTable,
                   FailSoftPVSMoveGetter::getShuffledPossibleMoves).move();
    }

}
