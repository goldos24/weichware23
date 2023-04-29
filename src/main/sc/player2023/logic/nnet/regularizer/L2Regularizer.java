package sc.player2023.logic.nnet.regularizer;

import sc.player2023.logic.nnet.Regularizer;
import sc.player2023.logic.nnet.math.Matrix;

public class L2Regularizer implements Regularizer {

    private final double lambda;

    public L2Regularizer(double lambda) {
        this.lambda = lambda;
    }

    @Override
    public Matrix regularize(Matrix weights, Matrix delta) {
        Matrix regularizationTerm = weights.map(value -> value * this.lambda);
        return delta.add(regularizationTerm);
    }
}
