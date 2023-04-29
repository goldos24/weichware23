package sc.player2023.logic.nnet;

import sc.player2023.logic.nnet.math.MatrixInitializer;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NeuralNetworkTest {

    @Test
    void givenNetworkWithOneInputToOneOutput_andWeightOf1_returnsSameValue() {
        double value = 0.5;
        NeuralNetwork neuralNetwork = NeuralNetworkFactory.create(List.of(1, 1));
        double actual = neuralNetwork.propagate(value)[0];
        double expected = 0.5;
        assertEquals(expected, actual);
    }

    @Test
    void givenNetworkWithTwoInputsAndOneOutput_andWeightOf1_returnsSum() {
        double firstValue = 0.91;
        double secondValue = 0.12;
        NeuralNetwork neuralNetwork = NeuralNetworkFactory.create(List.of(2, 1));
        double actual = neuralNetwork.propagate(firstValue, secondValue)[0];
        double expected = 1.03;
        assertEquals(expected, actual);
    }

    @Test
    void givenNetworkWithThreeInputsAndTwoOutputs_returnsSums() {
        double[] inputs = {0.15, 0.7, 1.2};
        NeuralNetwork neuralNetwork = NeuralNetworkFactory.create(List.of(3, 2));
        double[] actual = neuralNetwork.propagate(inputs);
        double[] expected = {2.05, 2.05};
        assertArrayEquals(expected, actual);
    }

    @Test
    void givenNetworkWithTwoInputsAndTwoOutputs_failsOnPropagateWithWrongInputsAmount() {
        double[] inputs = {0.0, 0.0, 0.0};
        NeuralNetwork neuralNetwork = NeuralNetworkFactory.create(List.of(2, 2));
        assertThrows(NeuralNetworkPropagateException.class, () ->
            neuralNetwork.propagate(inputs), "Incorrect input size: got 3, expected 2");
    }

    @Test
    void givenNetworkWithTwoInputsThreeHiddenAndOneOutput_andPointFiveWeights_returnsCorrectValue() {
        NeuralNetwork neuralNetwork = NeuralNetworkFactory.create(List.of(2, 3, 1),
            MatrixInitializer.fill(0.5),
            MatrixInitializer.fill(0.0));

        double[] actual = neuralNetwork.propagate(12.4, -6.8);

        double hiddenNeuronValue = 12.4 * 0.5 - 6.8 * 0.5;
        double[] expected = {3 * 0.5 * hiddenNeuronValue};
        assertArrayEquals(expected, actual);
    }
}
