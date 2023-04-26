package sc.player2023.logic.rating;

import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Arrays;
import java.util.Objects;

@Immutable
public final class CompositeRater implements Rater {
    private final Rater[] raters;

    public CompositeRater(Rater[] raters) {
        this.raters = raters;
    }

    public Rater[] raters() {
        return raters;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CompositeRater) obj;
        return Objects.equals(this.raters, that.raters);
    }

    @Override
    public int hashCode() {
        return Objects.hash((Object) raters);
    }

    @Override
    public String toString() {
        return "CompositeRater[" +
                "raters=" + raters + ']';
    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState state) {
        Rating result = Rating.ZERO;
        for (Rater rater : raters) {
            result = result.add(rater.rate(state));
        }
        return result;
    }

    @Override
    public String getAnalytics(@Nonnull ImmutableGameState gameState) {
        var analytics = Arrays.stream(raters).map(rater -> rater.getAnalytics(gameState)).toArray();
        return "CompositeRater" + Arrays.toString(analytics);
    }

}
