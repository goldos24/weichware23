package sc.player2023.logic.score;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * @author Till Fransson
 * @since 04.12.2022
 */
public final class Score {
    private final int score;

    public Score(int score) {
        this.score = score;
    }

    public int score() {
        return score;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Score) obj;
        return this.score == that.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(score);
    }

    @Override
    public String toString() {
        return "Score[" +
                "score=" + score + ']';
    }

    public static final Score ZERO = new Score(0);

    @Nonnull
    public Score add(int otherScore) {
        return new Score(score + otherScore);
    }

    @Nonnull
    public Score add(@Nonnull Score otherScore) {
        return add(otherScore.score);
    }

    @Nonnull
    public Score subtract(int otherScore) {
        return new Score(score - otherScore);
    }

    @Nonnull
    public Score subtract(@Nonnull Score otherScore) {
        return subtract(otherScore.score);
    }

    public boolean isGreaterThan(@Nonnull Score otherScore) {
        return score > otherScore.score;
    }

    public boolean isLessThan(@Nonnull Score otherScore) {
        return score < otherScore.score;
    }
}
