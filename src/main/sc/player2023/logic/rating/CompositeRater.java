package sc.player2023.logic.rating;

import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

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

}
