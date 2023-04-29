package sc.player2023.logic.nnet.preprocess;

import sc.player2023.logic.nnet.GradientPreprocessor;
import sc.player2023.logic.nnet.math.Matrix;

public class ValueClipping implements GradientPreprocessor {

    private final double minLimit;
    private final double maxLimit;

    public ValueClipping(double minLimit, double maxLimit) {
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
    }

    private double limitValue(double value) {
        if (value <= minLimit) return minLimit;
        return Math.min(value, maxLimit);
    }

    @Override
    public Matrix process(Matrix matrix) {
        return matrix.map(this::limitValue);
    }
}
