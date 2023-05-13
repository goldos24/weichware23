package sc.player2023.logic.nnet;

import sc.player2023.logic.nnet.math.Matrix;

import java.util.List;

public interface LearningAlgorithm {

    CostFunction costFunctionForTotalError();

    GradientPreprocessor gradientPreprocessor();

    Matrix updateWeights(Matrix weights, Matrix delta);

    Matrix updateBiases(Matrix biases, Matrix gradient);

    void notifyNewIteration();

    default void updateNetwork(NeuralNetwork network, List<Matrix> gradients, List<Matrix> deltas) {
        Matrix[] weights = network.getWeights();
        Matrix[] biases = network.getBiases();
        for (int i = 0; i < deltas.size(); ++i) {
            Matrix delta = deltas.get(i);
            Matrix gradient = gradients.get(i);

            weights[i] = updateWeights(weights[i], delta);
            biases[i] = updateBiases(biases[i], gradient);
        }
    }
}
