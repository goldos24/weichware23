package sc.player2023.logic.nnet;

import sc.player2023.logic.nnet.math.Matrix;

public interface ActivationFunction {

    ValueDerivativePair calculate(double input);

    default Matrix getGradient(Matrix matrix) {
        return matrix.map(value -> calculate(value).derivative());
    }
}
