package sc.player2023.logic.rating.alphabeta;

public class FailHardPVSAlphaBetaUtil {

    public static boolean canBeCutBeta(AlphaBeta alphaBeta) {
        return alphaBeta.alpha() >= alphaBeta.beta();
    }

    public static boolean canBeCutScore(AlphaBeta alphaBeta, double score) {
        return alphaBeta.alpha() < score && score < alphaBeta.beta();
    }

}
