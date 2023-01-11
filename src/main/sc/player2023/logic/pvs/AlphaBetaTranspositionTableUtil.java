package sc.player2023.logic.pvs;

import org.jetbrains.annotations.NotNull;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.rating.Rating;
import sc.player2023.logic.transpositiontable.TransPositionTable;

public class AlphaBetaTranspositionTableUtil {
    public static void addExactIfNotPresent(@NotNull ImmutableGameState gameState, TransPositionTable transPositionTable, Rating rating) {
        if(!transPositionTable.hasGameState(gameState)) {
            transPositionTable.addExact(gameState, rating);
        }
    }

}
