package sc.player2023.logic.rating;

import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class WeightedRater implements Rater {
    private final int weight;
    private final Rater originalRater;

    public WeightedRater(int weight, Rater originalRater) {
        this.weight = weight;
        this.originalRater = originalRater;
    }

    public int weight() {
        return weight;
    }

    public Rater originalRater() {
        return originalRater;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (WeightedRater) obj;
        return this.weight == that.weight &&
                Objects.equals(this.originalRater, that.originalRater);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, originalRater);
    }

    @Override
    public String toString() {
        return "WeightedRater[" +
                "weight=" + weight + ", " +
                "originalRater=" + originalRater + ']';
    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        Rating rating = originalRater.rate(gameState);
        return rating.multiply(weight);
    }

    public String getAnalytics(@Nonnull ImmutableGameState gameState) {
        return "(" + rate(gameState).rating() + "=" + weight + "*" + originalRater.getAnalytics(gameState) + ")";
    }

}
