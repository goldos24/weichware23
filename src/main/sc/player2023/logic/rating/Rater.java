package sc.player2023.logic.rating;

import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;

public interface Rater {
    Rating rate(@Nonnull ImmutableGameState gameState);
    default String getAnalytics(@Nonnull ImmutableGameState gameState) {
        return this.getClass().getSimpleName() + "{" + rate(gameState).rating() + "}";
    }
}
