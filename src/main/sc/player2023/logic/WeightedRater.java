package sc.player2023.logic;

import javax.annotation.Nonnull;

public record WeightedRater(int weight, Rater originalRater) implements Rater {

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        Rating rating = originalRater.rate(gameState);
        return rating.multiply(weight);
    }

}
