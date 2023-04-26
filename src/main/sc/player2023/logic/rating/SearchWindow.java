package sc.player2023.logic.rating;

import java.util.Objects;

/**
 * @author Till Fransson
 * @since 13.11.2022
 */
public final class SearchWindow {
    private final int lowerBound;
    private final int upperBound;

    public SearchWindow(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public int lowerBound() {
        return lowerBound;
    }

    public int upperBound() {
        return upperBound;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SearchWindow) obj;
        return this.lowerBound == that.lowerBound &&
                this.upperBound == that.upperBound;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lowerBound, upperBound);
    }

    @Override
    public String toString() {
        return "SearchWindow[" +
                "lowerBound=" + lowerBound + ", " +
                "upperBound=" + upperBound + ']';
    }

    public boolean canBeCutBeta() {
        return lowerBound >= upperBound;
    }

    public boolean canBeCutScore(int score) {
        return lowerBound < score && score < upperBound;
    }

}
