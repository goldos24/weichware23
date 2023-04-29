package sc.player2023.logic.nnet.learning;

import sc.player2023.logic.nnet.CostFunction;
import sc.player2023.logic.nnet.LearningAlgorithm;
import sc.player2023.logic.nnet.costs.MeanError;
import sc.player2023.logic.nnet.math.Matrix;

public class BackPropagation implements LearningAlgorithm {

    private final double learningRate;
    private final CostFunction costFunctionForTotalError;

    public BackPropagation(double learningRate) {
        this.learningRate = learningRate;
        this.costFunctionForTotalError = new MeanError();
    }

    public BackPropagation(double learningRate, CostFunction costFunctionForTotalError) {
        this.learningRate = learningRate;
        this.costFunctionForTotalError = costFunctionForTotalError;
    }

    @Override
    public CostFunction costFunctionForTotalError() {
        return this.costFunctionForTotalError;
    }

    @Override
    public Matrix updateParameter(Matrix parameter, Matrix delta) {
        return parameter.add(delta.scale(this.learningRate));
    }
}
