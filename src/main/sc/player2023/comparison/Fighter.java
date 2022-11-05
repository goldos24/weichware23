package sc.player2023.comparison;

import sc.player2023.logic.MoveGetter;
import sc.player2023.logic.rating.Rater;

import javax.annotation.Nonnull;

public record Fighter(@Nonnull MoveGetter moveGetter, @Nonnull Rater rater) {
}
