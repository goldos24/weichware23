package sc.player2023.logic.rating;

/**
 * @author Till Fransson
 * @since 13.11.2022
 */
public record SearchWindow(double alpha, double beta) {

    public boolean canBeCutBeta() {
        return alpha >= beta;
    }

    public boolean canBeCutScore(double score) {
        return alpha < score && score < beta;
    }

}
