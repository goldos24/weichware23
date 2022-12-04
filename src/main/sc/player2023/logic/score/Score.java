package sc.player2023.logic.score;

import javax.annotation.Nonnull;

/**
 * @author Till Fransson
 * @since 04.12.2022
 */
public record Score(int score) {

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

    @Override
    public String toString() {
        return Integer.toString(score);
    }
}
