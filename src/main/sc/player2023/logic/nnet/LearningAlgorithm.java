package sc.player2023.logic.nnet;

import sc.player2023.logic.nnet.math.Matrix;

public interface LearningAlgorithm {

    CostFunction costFunctionForTotalError();

    Matrix updateParameter(Matrix parameter, Matrix delta);
}
