package sc.player2023.logic.nnet;

import org.junit.jupiter.api.Test;
import sc.player2023.logic.nnet.activations.LeakyReLU;
import sc.player2023.logic.nnet.activations.Linear;
import sc.player2023.logic.nnet.activations.ReLU;
import sc.player2023.logic.nnet.costs.MeanSquaredError;
import sc.player2023.logic.nnet.learning.MomentumBackPropagation;
import sc.player2023.logic.nnet.learning.DataSet;
import sc.player2023.logic.nnet.learning.DataSetRow;
import sc.player2023.logic.nnet.math.MatrixInitializer;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MomentumTest {

    @Test
    void learnXORWithReLU() {
        Random random = new Random(0);
        NeuralNetwork neuralNetwork = NeuralNetworkFactory.create(
            List.of(2, 10, 1),
            List.of(new ReLU(), new Linear()),
            MatrixInitializer.uniformRandom(random),
            MatrixInitializer.fill(0.0)
        );
        LearningAlgorithm learningAlgorithm = new MomentumBackPropagation(0.01, 0.9, new MeanSquaredError());
        DataSet dataSet = new DataSet(
            new DataSetRow(new double[]{0, 0}, new double[]{0}),
            new DataSetRow(new double[]{1, 0}, new double[]{1}),
            new DataSetRow(new double[]{0, 1}, new double[]{1}),
            new DataSetRow(new double[]{1, 1}, new double[]{0})
        );
        for (int i = 0; i < 1000; ++i) {
            neuralNetwork.learn(dataSet, learningAlgorithm);
        }

        double[][] testData = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        for (double[] data : testData) {
            double result = neuralNetwork.propagate(data)[0];

            int actual = result > 0.5 ? 1 : 0;
            int expected = ((int) data[0]) ^ ((int) data[1]);

            assertEquals(expected, actual);
        }
    }

    @Test
    void learnANDWithReLU() {
        Random random = new Random(0);
        NeuralNetwork neuralNetwork = NeuralNetworkFactory.create(
            List.of(2, 10, 1),
            List.of(new ReLU(), new Linear()),
            MatrixInitializer.uniformRandom(random),
            MatrixInitializer.fill(0.0)
        );
        LearningAlgorithm learningAlgorithm = new MomentumBackPropagation(0.01, 0.9, new MeanSquaredError());
        DataSet dataSet = new DataSet(
            new DataSetRow(new double[]{0, 0}, new double[]{0}),
            new DataSetRow(new double[]{1, 0}, new double[]{0}),
            new DataSetRow(new double[]{0, 1}, new double[]{0}),
            new DataSetRow(new double[]{1, 1}, new double[]{1})
        );
        for (int i = 0; i < 1000; ++i) {
            neuralNetwork.learn(dataSet, learningAlgorithm);
        }

        double[][] testData = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        for (double[] data : testData) {
            double result = neuralNetwork.propagate(data)[0];

            int actual = result > 0.5 ? 1 : 0;
            int expected = ((int) data[0]) & ((int) data[1]);

            assertEquals(expected, actual);
        }
    }

    @Test
    void learnORWithLeakyReLU() {
        Random random = new Random(0);
        NeuralNetwork neuralNetwork = NeuralNetworkFactory.create(
            List.of(2, 10, 1),
            List.of(new LeakyReLU(), new Linear()),
            MatrixInitializer.uniformRandom(random),
            MatrixInitializer.fill(0.0)
        );
        LearningAlgorithm learningAlgorithm = new MomentumBackPropagation(0.01, 0.9, new MeanSquaredError());
        DataSet dataSet = new DataSet(
            new DataSetRow(new double[]{0, 0}, new double[]{0}),
            new DataSetRow(new double[]{1, 0}, new double[]{1}),
            new DataSetRow(new double[]{0, 1}, new double[]{1}),
            new DataSetRow(new double[]{1, 1}, new double[]{1})
        );
        for (int i = 0; i < 1000; ++i) {
            neuralNetwork.learn(dataSet, learningAlgorithm);
        }

        double[][] testData = new double[][]{{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        for (double[] data : testData) {
            double result = neuralNetwork.propagate(data)[0];

            int actual = result > 0.5 ? 1 : 0;
            int expected = ((int) data[0]) | ((int) data[1]);

            assertEquals(expected, actual);
        }
    }
}
