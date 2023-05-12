package sc.player2023.logic.nnet;

import sc.player2023.logic.nnet.math.Matrix;

import java.io.Serializable;

public interface ActivationFunction extends Serializable {

    ValueDerivativePair calculate(double input);

    default Matrix getGradient(Matrix matrix) {
        return matrix.map(value -> calculate(value).derivative());
    }
}
