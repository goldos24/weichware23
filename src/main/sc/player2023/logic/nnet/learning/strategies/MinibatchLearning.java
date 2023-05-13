package sc.player2023.logic.nnet.learning.strategies;

import sc.player2023.logic.nnet.LearningAlgorithm;
import sc.player2023.logic.nnet.LearningStrategy;
import sc.player2023.logic.nnet.NeuralNetwork;
import sc.player2023.logic.nnet.learning.DataSet;
import sc.player2023.logic.nnet.learning.DataSetRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MinibatchLearning implements LearningStrategy {

    private final Random random;
    private final int batchSize;

    public MinibatchLearning(Random random, int batchSize) {
        this.random = random;
        this.batchSize = batchSize;
    }

    public MinibatchLearning(int batchSize) {
        this.batchSize = batchSize;
        this.random = new Random();
    }

    private List<DataSetRow> createBatch(List<DataSetRow> rows) {
        List<DataSetRow> batch = new ArrayList<>();
        for (int i = 0; i < this.batchSize; ++i) {
            int index = this.random.nextInt(rows.size());
            batch.add(rows.get(index));
        }
        return batch;
    }

    @Override
    public void doIteration(NeuralNetwork neuralNetwork, DataSet dataSet, LearningAlgorithm learningAlgorithm) {
        List<DataSetRow> rows = dataSet.rowsList();
        if (rows.size() < this.batchSize) {
            neuralNetwork.learn(dataSet, learningAlgorithm);
        } else {
            List<DataSetRow> batch = this.createBatch(rows);
            batch.forEach(row -> neuralNetwork.learn(row, learningAlgorithm));
        }
    }
}
