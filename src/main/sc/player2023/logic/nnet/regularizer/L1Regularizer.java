package sc.player2023.logic.nnet.regularizer;

import sc.player2023.logic.nnet.Regularizer;
import sc.player2023.logic.nnet.math.Matrix;

public class L1Regularizer implements Regularizer {

    private final double lambda;

    public L1Regularizer(double lambda) {
        this.lambda = lambda;
    }


    @Override
    public Matrix regularize(Matrix weights, Matrix delta) {
        Matrix regularizationTerm = weights.map(value -> Math.signum(value) * this.lambda);
        return delta.add(regularizationTerm);
    }
}
