package sc.player2023.logic.rating;

import org.jetbrains.annotations.Nullable;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;

/**
 * @author Till Fransson
 * @since 13.12.2022
 */
public record RatingWithMove(
        @Nullable Move move,
        @Nonnull Rating rating
) {
}
