package sc.player2023.logic;

import javax.annotation.Nonnull;

public interface Rater {
    int rate(@Nonnull ImmutableGameState gameState);
}
