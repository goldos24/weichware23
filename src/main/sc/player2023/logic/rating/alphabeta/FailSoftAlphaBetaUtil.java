package sc.player2023.logic.rating.alphabeta;

import sc.player2023.logic.rating.Rating;

public class FailSoftAlphaBetaUtil {
    public static boolean scoreGreaterThanAlpha(AlphaBeta alphaBeta, Rating score) {
        return score.isGreaterThan(alphaBeta.alpha());
    }

    public static boolean canDoBetaCutOff(AlphaBeta alphaBeta, Rating score) {
        return score.rating() >= alphaBeta.beta().rating();
    }
}
