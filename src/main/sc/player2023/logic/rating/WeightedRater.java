package sc.player2023.logic.rating;

import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;

public record WeightedRater(int weight, Rater originalRater) implements Rater {

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        Rating rating = originalRater.rate(gameState);
        return rating.multiply(weight);
    }

    public String getAnalytics(@Nonnull ImmutableGameState gameState) {
        return "(" + rate(gameState).rating() + "=" + weight + "*" + originalRater.getAnalytics(gameState) + ")";
    }

}
