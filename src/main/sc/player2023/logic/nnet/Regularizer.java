package sc.player2023.logic.nnet;

import sc.player2023.logic.nnet.math.Matrix;

public interface Regularizer {

    Matrix regularize(Matrix weights, Matrix delta);
}
