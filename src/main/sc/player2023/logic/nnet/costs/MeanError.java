package sc.player2023.logic.nnet.costs;

import sc.player2023.logic.nnet.CostFunction;
import sc.player2023.logic.nnet.math.Matrix;

public class MeanError implements CostFunction {

    @Override
    public Matrix calculate(Matrix output, Matrix target) {
        return target.subtract(output);
    }
}
