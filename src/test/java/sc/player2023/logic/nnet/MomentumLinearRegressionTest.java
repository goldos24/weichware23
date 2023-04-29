package sc.player2023.logic.nnet;

import org.junit.jupiter.api.Test;
import sc.player2023.logic.nnet.activations.LeakyReLU;
import sc.player2023.logic.nnet.costs.MeanSquaredError;
import sc.player2023.logic.nnet.learning.DataSet;
import sc.player2023.logic.nnet.learning.MomentumBackPropagation;
import sc.player2023.logic.nnet.math.MatrixInitializer;
import sc.player2023.logic.nnet.preprocess.NormClipping;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MomentumLinearRegressionTest {

    @Test
    void learnLinearRegressionWithMomentum() {
        Random random = new Random();
        List<Double> coefficients = List.of(7.0, -5.0, 0.5, 3.0, 0.25, 6.0, -10.0, -1.0, 3.9, 12.3, -6.2);
        DataSet dataSet = LinearRegression.generateLinearRegressionModel(300, coefficients, -3, 3, 0.2, random);

        NeuralNetwork neuralNetwork = NeuralNetworkFactory.create(
            List.of(10, 25, 1),
            List.of(new LeakyReLU(), new LeakyReLU()),
            MatrixInitializer.gaussianRandom(random),
            MatrixInitializer.fill(0.0)
        );
        LearningAlgorithm learningAlgorithm = new MomentumBackPropagation(0.01, 0.9, new MeanSquaredError(), Regularizer.NONE, new NormClipping(2.0));

        for (int i = 0; i < 500; ++i) {
            neuralNetwork.learn(dataSet, learningAlgorithm);
        }

        List<Double> data = List.of(-1.0, 2.8, 1.25, 0.6, -0.17, -1.6, 2.0, -3.0, 2.5, -0.2);
        double expected = coefficients.get(0);
        double[] inputs = new double[data.size()];
        for (int i = 0; i < data.size(); ++i) {
            inputs[i] = data.get(i);
            expected += coefficients.get(i + 1) * data.get(i);
        }

        double actual = neuralNetwork.propagate(inputs)[0];

        assertEquals(expected, actual, 0.5);
    }
}
