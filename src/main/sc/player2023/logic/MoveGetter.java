package sc.player2023.logic;

import javax.annotation.Nonnull;

import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.rating.Rater;
import sc.plugin2023.Move;

public interface MoveGetter {
    Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer);
    // TimeMeasurer needs to be reset directly before function call
}
