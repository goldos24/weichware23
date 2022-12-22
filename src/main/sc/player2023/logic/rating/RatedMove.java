package sc.player2023.logic.rating;

import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Till Fransson
 * @since 03.11.2022
 */
public record RatedMove(@Nonnull Rating rating, @Nullable Move move) implements Comparable<RatedMove> {

    @Override
    public int compareTo(@Nonnull RatedMove o) {
        return rating.compareTo(o.rating);
    }
}
