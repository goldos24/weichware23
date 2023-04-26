package sc.player2023.logic.bns;

import com.google.common.collect.Iterables;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.MoveGetter;
import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.move.FishCountPreSorting;
import sc.player2023.logic.move.PossibleMoveGenerator;
import sc.player2023.logic.pvs.FailSoftPVSMoveGetter;
import sc.player2023.logic.rating.RatedMove;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.rating.SearchWindow;
import sc.player2023.logic.transpositiontable.SmartTransPositionTableFactory;
import sc.player2023.logic.transpositiontable.TransPositionTable;
import sc.player2023.logic.transpositiontable.TransPositionTableFactory;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class ConstDepthBNSMoveGetter implements MoveGetter {

    public static Logger log = LoggerFactory.getLogger(ConstDepthBNSMoveGetter.class);

    private final int depth;

    public ConstDepthBNSMoveGetter(int depth) {
        this.depth = depth;
    }

    public static RatedMove bns(@Nonnull ImmutableGameState gameState, int depth, SearchWindow searchWindow,
                                @Nonnull Rater rater, @Nonnull TimeMeasurer timeMeasurer,
                                TransPositionTable transPositionTable, PossibleMoveGenerator moveGenerator,
                                @Nonnull BNSTestGuesser testGuesser) {
        Iterable<Move> possibleMoves = moveGenerator.getPossibleMoves(gameState);
        int subtreeCount = Iterables.size(possibleMoves);

        RatedMove bestMove = null;
        int betterCount;
        do {
            int test = testGuesser.makeNextGuess(searchWindow, subtreeCount);
            betterCount = 0;

            for (Move move : possibleMoves) {
                ImmutableGameState currentState = GameRuleLogic.withMovePerformed(gameState, move);
                SearchWindow subSearchWindow = new SearchWindow(-test, -test + 1);
                RatedMove result = FailSoftPVSMoveGetter.pvs(currentState, depth, subSearchWindow, rater, timeMeasurer,
                    transPositionTable, moveGenerator);
                Rating negatedRating = result.rating().negate();
                int ratingValue = negatedRating.rating();

                if (ratingValue >= test) {
                    betterCount += 1;
                    bestMove = result;
                }

                if (timeMeasurer.ranOutOfTime()) {
                    break;
                }
            }

            log.info("{} with better test value (test = {})", betterCount, test);

            if (betterCount == 0) {
                searchWindow = new SearchWindow(searchWindow.lowerBound(), test);
            } else {
                subtreeCount = betterCount;
                searchWindow = new SearchWindow(test, searchWindow.upperBound());
            }
        } while (searchWindow.getSpan() > 2 && betterCount != 1);

        return bestMove;
    }

    @Nullable
    public static Move getBestMoveForDepth(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater,
                                           TimeMeasurer timeMeasurer, int depth,
                                           TransPositionTable transPositionTable) {
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        Move bestMove = possibleMoves.get(0);
        SearchWindow searchWindow = new SearchWindow(-700, 700);
        BasicBNSTestGuesser guesser = new BasicBNSTestGuesser();

        Move foundMove = bns(gameState, depth, searchWindow, rater, timeMeasurer, transPositionTable,
            FishCountPreSorting::getPossibleMoves, guesser).move();
        if (foundMove == null) {
            return bestMove;
        }

        return foundMove;
    }

    TransPositionTableFactory transPositionTableFactory = new SmartTransPositionTableFactory();

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        TransPositionTable transPositionTable = transPositionTableFactory.createTransPositionTableFromDepth(this.depth);
        return getBestMoveForDepth(gameState, rater, timeMeasurer, this.depth, transPositionTable);
    }
}
