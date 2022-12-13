package sc.player2023.logic.rating.alphabeta;

public class FailHardPVSAlphaBetaUtil {

    public static boolean canBeCutBeta(AlphaBeta alphaBeta) {
        return alphaBeta.alpha().rating() >= alphaBeta.beta().rating();
    }

    public static boolean canBeCutScore(AlphaBeta alphaBeta, double score) {
        return alphaBeta.alpha().rating() < score && score < alphaBeta.beta().rating();
    }

}
