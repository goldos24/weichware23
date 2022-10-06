package sc.player2023.comparison;

import sc.player2023.logic.MoveGetter;
import sc.player2023.logic.Rater;

import javax.annotation.Nonnull;

public class Fighter {
    @Nonnull
    MoveGetter moveGetter;

    @Nonnull
    Rater rater;

    public Fighter(@Nonnull MoveGetter moveGetter, @Nonnull Rater rater) {
        this.moveGetter = moveGetter;
        this.rater = rater;
    }
}
