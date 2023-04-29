package sc.player2023.logic.nnet;

import sc.player2023.logic.nnet.activations.Linear;
import sc.player2023.logic.nnet.math.Matrix;
import sc.player2023.logic.nnet.math.MatrixDimensions;
import sc.player2023.logic.nnet.math.MatrixInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class NeuralNetworkFactory {

    public static NeuralNetwork create(List<Integer> neuronsPerLayer,
                                       List<ActivationFunction> activationFunctions,
                                       MatrixInitializer weightInitializer,
                                       MatrixInitializer biasInitializer) {
        assert neuronsPerLayer.size() >= 2;
        List<Matrix> weights = new ArrayList<>();
        List<Matrix> biases = new ArrayList<>();
        for (int i = 0; i < neuronsPerLayer.size() - 1; ++i) {
            int layerNeuronsIn = neuronsPerLayer.get(i);
            int layerNeuronsOut = neuronsPerLayer.get(i + 1);

            Matrix weightMatrix = Matrix.fromInitializer(new MatrixDimensions(layerNeuronsOut, layerNeuronsIn), weightInitializer);
            weights.add(weightMatrix);
            Matrix biasMatrix = Matrix.fromInitializer(new MatrixDimensions(layerNeuronsOut, 1), biasInitializer);
            biases.add(biasMatrix);
        }

        return new NeuralNetwork(weights, biases, activationFunctions);
    }

    public static NeuralNetwork create(List<Integer> neuronsPerLayer,
                                       MatrixInitializer weightInitializer,
                                       MatrixInitializer biasInitializer) {
        List<ActivationFunction> activationFunctions = Stream
            .generate(() -> (ActivationFunction) new Linear())
            .limit(neuronsPerLayer.size() - 1)
            .toList();
        return create(neuronsPerLayer, activationFunctions, weightInitializer, biasInitializer);
    }

    public static NeuralNetwork create(List<Integer> neuronsPerLayer) {
        return create(neuronsPerLayer,
            MatrixInitializer.fill(1), MatrixInitializer.fill(0));
    }
}
