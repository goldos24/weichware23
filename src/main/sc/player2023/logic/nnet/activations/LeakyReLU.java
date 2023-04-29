package sc.player2023.logic.nnet.activations;

import sc.player2023.logic.nnet.ValueDerivativePair;

public class LeakyReLU extends ReLU {

    private final double leakSlope;

    public LeakyReLU() {
        this.leakSlope = 0.01;
    }

    public LeakyReLU(double slope, double leakSlope) {
        super(slope);
        this.leakSlope = leakSlope;
    }

    @Override
    public ValueDerivativePair calculate(double input) {
        if (input >= 0) {
            return super.calculate(input);
        } else {
            double result = this.leakSlope * input;
            return new ValueDerivativePair(result, this.leakSlope);
        }
    }
}
