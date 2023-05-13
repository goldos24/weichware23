package sc.player2023.logic.nnet.learning.strategies;

import sc.player2023.logic.nnet.LearningAlgorithm;
import sc.player2023.logic.nnet.LearningStrategy;
import sc.player2023.logic.nnet.NeuralNetwork;
import sc.player2023.logic.nnet.learning.DataSet;

public class BatchLearning implements LearningStrategy {

    @Override
    public void doIteration(NeuralNetwork neuralNetwork, DataSet dataSet, LearningAlgorithm learningAlgorithm) {
        learningAlgorithm.notifyNewIteration();
        neuralNetwork.learn(dataSet, learningAlgorithm);
    }
}
