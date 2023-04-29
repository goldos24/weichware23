package sc.player2023.logic.nnet.regularizer;

import sc.player2023.logic.nnet.Regularizer;
import sc.player2023.logic.nnet.math.Matrix;

public class NoRegularizer implements Regularizer {
    @Override
    public Matrix regularize(Matrix weights, Matrix delta) {
        return delta;
    }
}
