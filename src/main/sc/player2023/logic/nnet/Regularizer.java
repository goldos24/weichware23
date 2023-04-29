package sc.player2023.logic.nnet;

import sc.player2023.logic.nnet.math.Matrix;

public interface Regularizer {

    Regularizer NONE = (weights, delta) -> delta;

    Matrix regularize(Matrix weights, Matrix delta);
}
