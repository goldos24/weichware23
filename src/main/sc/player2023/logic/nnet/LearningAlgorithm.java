package sc.player2023.logic.nnet;

import sc.player2023.logic.nnet.math.Matrix;

public interface LearningAlgorithm {

    CostFunction costFunctionForTotalError();

    Matrix updateWeights(Matrix weights, Matrix delta);

    Matrix updateBiases(Matrix biases, Matrix gradient);
}
