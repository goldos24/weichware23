package sc.player2023.logic.rating;

/**
 * @author Till Fransson
 * @since 13.11.2022
 */
public record SearchWindow(int lowerBound, int upperBound) {

    public boolean canBeCutBeta() {
        return lowerBound >= upperBound;
    }

    public boolean canBeCutScore(int score) {
        return lowerBound < score && score < upperBound;
    }

}
