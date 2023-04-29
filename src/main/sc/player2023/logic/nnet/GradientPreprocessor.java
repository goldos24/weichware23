package sc.player2023.logic.nnet;

import sc.player2023.logic.nnet.math.Matrix;

public interface GradientPreprocessor {

    GradientPreprocessor NONE = matrix -> matrix;

    Matrix process(Matrix matrix);
}
