package sc.player2023.logic.nnet.learning.strategies;

import sc.player2023.logic.nnet.LearningAlgorithm;
import sc.player2023.logic.nnet.LearningStrategy;
import sc.player2023.logic.nnet.NeuralNetwork;
import sc.player2023.logic.nnet.learning.DataSet;
import sc.player2023.logic.nnet.learning.DataSetRow;

import java.util.List;
import java.util.Random;

public class StochasticLearning implements LearningStrategy {

    private final Random random;

    public StochasticLearning(Random random) {
        this.random = random;
    }

    public StochasticLearning() {
        this.random = new Random();
    }

    @Override
    public void doIteration(NeuralNetwork neuralNetwork, DataSet dataSet, LearningAlgorithm learningAlgorithm) {
        List<DataSetRow> rows = dataSet.rowsList();
        int randomIndex = random.nextInt(rows.size());
        DataSetRow randomRow = rows.get(randomIndex);
        neuralNetwork.learn(randomRow, learningAlgorithm);
    }
}
