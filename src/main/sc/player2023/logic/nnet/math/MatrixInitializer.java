package sc.player2023.logic.nnet.math;

import java.util.Random;

public interface MatrixInitializer {

    static MatrixInitializer fill(double value) {
        return (row, column) -> value;
    }

    static MatrixInitializer uniformRandom(Random random) {
        return (row, column) -> random.nextDouble();
    }

    static MatrixInitializer gaussianRandom(Random random, double standardDeviation) {
        return (row, column) -> random.nextGaussian() * standardDeviation;
    }

    double getValue(int row, int column);
}
