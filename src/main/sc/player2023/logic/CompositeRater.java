package sc.player2023.logic;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

@Immutable
public record CompositeRater(Rater[] raters) implements Rater {

    @Override
    public int rate(@Nonnull ImmutableGameState immutableGameState)
    {
        int result = 0;
        for(Rater rater : raters) {
            result += rater.rate(immutableGameState);
        }
        return result;
    }
}
