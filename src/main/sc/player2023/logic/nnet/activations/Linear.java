package sc.player2023.logic.nnet.activations;

import sc.player2023.logic.nnet.ActivationFunction;
import sc.player2023.logic.nnet.ValueDerivativePair;

public class Linear implements ActivationFunction {

    @Override
    public ValueDerivativePair calculate(double input) {
        return new ValueDerivativePair(input, 1.0);
    }
}
