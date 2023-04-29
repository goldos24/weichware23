package sc.player2023.logic.nnet.preprocess;

import sc.player2023.logic.nnet.GradientPreprocessor;
import sc.player2023.logic.nnet.math.Matrix;

public class NormClipping implements GradientPreprocessor {

    private final double limit;

    public NormClipping(double limit) {
        this.limit = limit;
    }

    @Override
    public Matrix process(Matrix matrix) {
        double norm = matrix.frobeniusNorm();
        if (norm < this.limit) {
            return matrix;
        }

        Matrix normalizedMatrix = matrix.scale(1.0 / norm);
        return normalizedMatrix.scale(limit);
    }
}
