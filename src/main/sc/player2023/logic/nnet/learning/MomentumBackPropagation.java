package sc.player2023.logic.nnet.learning;

import sc.player2023.logic.nnet.CostFunction;
import sc.player2023.logic.nnet.GradientPreprocessor;
import sc.player2023.logic.nnet.NeuralNetwork;
import sc.player2023.logic.nnet.Regularizer;
import sc.player2023.logic.nnet.math.Matrix;

import java.util.ArrayList;
import java.util.List;

public class MomentumBackPropagation extends BackPropagation {

    private final double momentum;

    public MomentumBackPropagation(double learningRate, double momentum) {
        super(learningRate);
        this.momentum = momentum;
    }

    public MomentumBackPropagation(double learningRate, double momentum, CostFunction costFunctionForTotalError) {
        super(learningRate, costFunctionForTotalError);
        this.momentum = momentum;
    }

    public MomentumBackPropagation(double learningRate, double momentum, CostFunction costFunctionForTotalError, Regularizer regularizer) {
        super(learningRate, costFunctionForTotalError, regularizer);
        this.momentum = momentum;
    }

    public MomentumBackPropagation(double learningRate, double momentum, CostFunction costFunctionForTotalError, Regularizer regularizer, GradientPreprocessor gradientPreprocessor) {
        super(learningRate, costFunctionForTotalError, regularizer, gradientPreprocessor);
        this.momentum = momentum;
    }

    private List<Matrix> previousWeightDeltas = new ArrayList<>();
    private List<Matrix> previousBiasDeltas = new ArrayList<>();

    @Override
    public void updateNetwork(NeuralNetwork network, List<Matrix> gradients, List<Matrix> deltas) {
        for (Matrix delta : deltas) {
            previousWeightDeltas.add(new Matrix(delta.getDimensions()));
        }
        for (Matrix gradient : gradients) {
            previousBiasDeltas.add(new Matrix(gradient.getDimensions()));
        }

        Matrix[] weights = network.getWeights();
        Matrix[] biases = network.getBiases();
        for (int i = 0; i < deltas.size(); ++i) {
            Matrix delta = deltas.get(i);
            Matrix gradient = gradients.get(i);

            Matrix weightMomentum = previousWeightDeltas.get(i).scale(momentum);
            weights[i] = updateWeights(weights[i], delta.add(weightMomentum));
            Matrix biasMomentum = previousBiasDeltas.get(i).scale(momentum);
            biases[i] = updateBiases(biases[i], gradient.add(biasMomentum));
        }

        previousWeightDeltas = deltas;
        previousBiasDeltas = gradients;
    }
}
