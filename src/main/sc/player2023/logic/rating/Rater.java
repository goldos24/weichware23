package sc.player2023.logic.rating;

import sc.player2023.logic.ImmutableGameState;

import javax.annotation.Nonnull;

public interface Rater {
    Rating rate(@Nonnull ImmutableGameState gameState);
}
