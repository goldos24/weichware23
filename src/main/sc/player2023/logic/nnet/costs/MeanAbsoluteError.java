package sc.player2023.logic.nnet.costs;

import sc.player2023.logic.nnet.math.Matrix;

public class MeanAbsoluteError extends MeanError {

    @Override
    public Matrix calculate(Matrix output, Matrix target) {
        return super.calculate(output, target).map(Math::abs);
    }
}
