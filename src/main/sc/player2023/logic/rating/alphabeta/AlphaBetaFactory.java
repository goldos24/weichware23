package sc.player2023.logic.rating.alphabeta;

import sc.player2023.logic.rating.Rating;

public class AlphaBetaFactory {
    public static AlphaBeta inverseOf(AlphaBeta source) {
        return new AlphaBeta(source.beta().negate(), source.alpha().negate());
    }

    public static AlphaBeta withNewAlpha(AlphaBeta source, Rating newAlpha) {
        return new AlphaBeta(newAlpha, source.beta());
    }

    public static AlphaBeta withNewBeta(AlphaBeta source, Rating newBeta) {
        return new AlphaBeta(source.alpha(), newBeta);
    }

    public static AlphaBeta alphaNullWindow(AlphaBeta source) {
        return new AlphaBeta(source.alpha().negate().subtract(1), source.alpha().negate());
    }
}
