package sc.player2023.logic.nnet;

import sc.player2023.logic.nnet.math.Matrix;

import java.util.List;
import java.util.stream.DoubleStream;

public interface CostFunction {

    Matrix calculate(Matrix output, Matrix target);

    default double calculateTotal(Matrix output, Matrix target) {
        Matrix error = this.calculate(output, target);
        List<Double> flatErrors = error.flatten();
        DoubleStream errorsStream = flatErrors.stream().mapToDouble(Double::doubleValue);
        double errorSum = errorsStream.sum();
        return errorSum / flatErrors.size();
    }
}
