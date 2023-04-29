package sc.player2023.logic.nnet.learning;

import sc.player2023.logic.nnet.CostFunction;
import sc.player2023.logic.nnet.LearningAlgorithm;
import sc.player2023.logic.nnet.Regularizer;
import sc.player2023.logic.nnet.costs.MeanError;
import sc.player2023.logic.nnet.math.Matrix;
import sc.player2023.logic.nnet.regularizer.NoRegularizer;

public class BackPropagation implements LearningAlgorithm {

    private final double learningRate;
    private final CostFunction costFunctionForTotalError;
    private final Regularizer regularizer;

    public BackPropagation(double learningRate) {
        this.learningRate = learningRate;
        this.costFunctionForTotalError = new MeanError();
        this.regularizer = new NoRegularizer();
    }

    public BackPropagation(double learningRate, CostFunction costFunctionForTotalError) {
        this.learningRate = learningRate;
        this.costFunctionForTotalError = costFunctionForTotalError;
        this.regularizer = new NoRegularizer();
    }

    public BackPropagation(double learningRate, CostFunction costFunctionForTotalError, Regularizer regularizer) {
        this.learningRate = learningRate;
        this.costFunctionForTotalError = costFunctionForTotalError;
        this.regularizer = regularizer;
    }

    @Override
    public CostFunction costFunctionForTotalError() {
        return this.costFunctionForTotalError;
    }

    @Override
    public Matrix updateWeights(Matrix weights, Matrix delta) {
        delta = this.regularizer.regularize(weights, delta);
        delta = delta.scale(this.learningRate);
        return weights.add(delta);
    }

    @Override
    public Matrix updateBiases(Matrix biases, Matrix gradient) {
        Matrix delta = gradient.scale(this.learningRate);
        return biases.add(delta);
    }
}
