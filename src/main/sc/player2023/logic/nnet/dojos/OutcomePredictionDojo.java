package sc.player2023.logic.nnet.dojos;

import sc.player2023.logic.nnet.LearningAlgorithm;
import sc.player2023.logic.nnet.NeuralNetwork;
import sc.player2023.logic.nnet.NeuralNetworkFactory;
import sc.player2023.logic.nnet.Regularizer;
import sc.player2023.logic.nnet.activations.LeakyReLU;
import sc.player2023.logic.nnet.activations.Linear;
import sc.player2023.logic.nnet.costs.MeanSquaredError;
import sc.player2023.logic.nnet.learning.DataSet;
import sc.player2023.logic.nnet.learning.MomentumBackPropagation;
import sc.player2023.logic.nnet.learning.strategies.MinibatchLearning;
import sc.player2023.logic.nnet.math.MatrixInitializer;
import sc.player2023.logic.nnet.preprocess.NormClipping;

import java.util.List;
import java.util.Random;

public class OutcomePredictionDojo extends LearningDojo {

    static NeuralNetwork createNetwork() {
        Random random = new Random();
        return NeuralNetworkFactory.create(
            List.of(258, 400, 256, 128, 4),
            List.of(new LeakyReLU(), new LeakyReLU(), new LeakyReLU(), new Linear()),
            MatrixInitializer.gaussianRandom(random, 0.7),
            MatrixInitializer.fill(0.0)
        );
    }

    static LearningAlgorithm createLearningAlgorithm() {
        return new MomentumBackPropagation(0.0001, 0.85, new MeanSquaredError(), Regularizer.NONE, new NormClipping(8.0));
    }

    public OutcomePredictionDojo(DataSet dataSet, int steps, int accuracyCalculationInterval, int stepSaveInterval) {
        super(createNetwork(), dataSet, steps, stepSaveInterval, accuracyCalculationInterval, createLearningAlgorithm(), new MinibatchLearning(100));
    }

    public static void main(String[] args) {
        OutcomePredictionData data = new OutcomePredictionData(100, 100000);
        new OutcomePredictionDojo(data.createDataSet(), 100, 1, 10)
            .learn();
    }
}
