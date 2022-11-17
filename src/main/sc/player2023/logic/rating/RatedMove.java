package sc.player2023.logic.rating;

import org.jetbrains.annotations.NotNull;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;

/**
 * @author Till Fransson
 * @since 03.11.2022
 */
public record RatedMove(@Nonnull Rating rating, @Nonnull Move move) implements Comparable<RatedMove> {

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RatedMove ratedMove)) {
            return false;
        }
        return rating.equals(ratedMove.rating) && move.equals(ratedMove.move);
    }

    @Override
    public String toString() {
        return "RatedMove {" + rating + ", " + move + "}";
    }

    @Override
    public int compareTo(@Nonnull RatedMove o) {
        return rating.compareTo(o.rating);
    }
}
