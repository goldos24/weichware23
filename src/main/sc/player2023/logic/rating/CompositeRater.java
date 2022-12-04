package sc.player2023.logic.rating;

import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.Arrays;

@Immutable
public record CompositeRater(Rater[] raters) implements Rater {

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
