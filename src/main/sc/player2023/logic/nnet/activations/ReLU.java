package sc.player2023.logic.nnet.activations;

import sc.player2023.logic.nnet.ActivationFunction;
import sc.player2023.logic.nnet.ValueDerivativePair;

public class ReLU implements ActivationFunction {

    private final double slope;

    public ReLU(double slope) {
        this.slope = slope;
    }

    public ReLU() {
        this(1.0);
    }

    @Override
    public ValueDerivativePair calculate(double input) {
        double result = this.slope * Math.max(0, input);
        double derivative = input >= 0.0 ? this.slope : 0.0;
        return new ValueDerivativePair(result, derivative);
    }
}
