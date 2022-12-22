package sc.player2023.logic.rating;

/**
 * @author Till Fransson
 * @since 13.11.2022
 */
public record SearchWindow(double lowerBound, double upperBound) {

    public boolean canBeCutBeta() {
        return lowerBound >= upperBound;
    }

    public boolean canBeCutScore(double score) {
        return lowerBound < score && score < upperBound;
    }

}
