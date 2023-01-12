package sc.player2023.logic.pvs;

import org.jetbrains.annotations.NotNull;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.rating.SearchWindow;
import sc.player2023.logic.transpositiontable.RatingType;
import sc.player2023.logic.transpositiontable.TransPositionTable;
import sc.player2023.logic.transpositiontable.TransPositionTableEntry;

import javax.annotation.Nonnull;

import static sc.player2023.logic.transpositiontable.RatingType.*;

public class AlphaBetaTranspositionTableUtil {
    public static void addExactIfNotPresent(@Nonnull ImmutableGameState gameState, TransPositionTable transPositionTable, Rating rating) {
        if(tableDoesntHaveGameState(gameState, transPositionTable)) {
            transPositionTable.addExact(gameState, rating);
        }
    }

    private static boolean tableDoesntHaveGameState(@NotNull ImmutableGameState gameState, TransPositionTable transPositionTable) {
        return !transPositionTable.hasGameState(gameState);
    }

    public static void addIfNotPresent(ImmutableGameState gameState, TransPositionTable transPositionTable, Rating rating, SearchWindow searchWindow) {
        if(transPositionTable.hasGameState(gameState))
            return;
        RatingType ratingType = calculateRatingType(rating, searchWindow);
        TransPositionTableEntry entry = new TransPositionTableEntry(rating, ratingType);
        transPositionTable.add(gameState, entry);
    }

    public static boolean isGameStateKnownToBeLowerThanRating(ImmutableGameState gameState, TransPositionTable transPositionTable, Rating rating) {
        if(tableDoesntHaveGameState(gameState, transPositionTable))
            return false;
        TransPositionTableEntry entry = transPositionTable.getTransPositionTableEntry(gameState);
        if(entry.ratingType() == FAIL_HIGH)
            return false;
        return entry.rating().isLessThan(rating);
    }

    public static boolean isGameStateKnownToBeHigherThanUpperBound(ImmutableGameState gameState, TransPositionTable transPositionTable, SearchWindow searchWindow) {
        if(tableDoesntHaveGameState(gameState, transPositionTable))
            return false;
        TransPositionTableEntry entry = transPositionTable.getTransPositionTableEntry(gameState);
        if(entry.ratingType() == FAIL_LOW)
            return false;
        return entry.rating().rating() > searchWindow.upperBound();
    }

    public static RatingType calculateRatingType(Rating rating, SearchWindow searchWindow) {
        if(rating.rating() <= searchWindow.lowerBound())
            return FAIL_LOW;
        if(rating.rating() >= searchWindow.upperBound())
            return FAIL_HIGH;
        return EXACT;
    }
}
