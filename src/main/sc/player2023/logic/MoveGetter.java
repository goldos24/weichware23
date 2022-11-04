package sc.player2023.logic;

import javax.annotation.Nonnull;
import sc.plugin2023.Move;

public interface MoveGetter {
    Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater);
}
