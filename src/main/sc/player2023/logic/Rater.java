package sc.player2023.logic;

import javax.annotation.Nonnull;

public interface Rater {
    Rating rate(@Nonnull ImmutableGameState gameState);
}
