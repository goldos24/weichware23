package sc.player2023.logic.nnet;

import sc.player2023.logic.nnet.learning.DataSet;

public interface LearningStrategy {

    void doIteration(NeuralNetwork neuralNetwork, DataSet dataSet, LearningAlgorithm learningAlgorithm);
}
