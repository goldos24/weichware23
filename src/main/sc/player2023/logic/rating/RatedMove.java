package sc.player2023.logic.rating;

import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Till Fransson
 * @since 03.11.2022
 */
public final class RatedMove implements Comparable<RatedMove> {
    public RatedMove(
            @Nonnull
            Rating rating,
            @Nullable
            Move move) {
        this.rating = rating;
        this.move = move;
    }

    @Nonnull
    Rating rating;
    @Nullable
    Move move;

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj != null && obj.getClass() == this.getClass();
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "RatedMove[]";
    }

    @Override
    public int compareTo(@Nonnull RatedMove o) {
        return rating.compareTo(o.rating);
    }

    public Move move() {
        return move;
    }

    public Rating rating() {
        return rating;
    }
}
