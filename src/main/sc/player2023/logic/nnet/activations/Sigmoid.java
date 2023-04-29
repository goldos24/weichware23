package sc.player2023.logic.nnet.activations;

import sc.player2023.logic.nnet.ActivationFunction;
import sc.player2023.logic.nnet.ValueDerivativePair;

public class Sigmoid implements ActivationFunction {

    @Override
    public ValueDerivativePair calculate(double input) {
        double value = 1 / (1 + Math.exp(-input));
        double derivative = input * (1 - input);
        return new ValueDerivativePair(value, derivative);
    }
}
