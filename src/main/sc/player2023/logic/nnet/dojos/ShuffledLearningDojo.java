package sc.player2023.logic.nnet.dojos;

import sc.player2023.logic.nnet.LearningAlgorithm;
import sc.player2023.logic.nnet.NeuralNetwork;
import sc.player2023.logic.nnet.learning.DataSet;
import sc.player2023.logic.nnet.learning.DataSetRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShuffledLearningDojo extends LearningDojo {

    public ShuffledLearningDojo(NeuralNetwork network, DataSet dataSet, int steps, int stepSaveInterval, int accuracyCalculationInterval, LearningAlgorithm learningAlgorithm) {
        super(network, dataSet, steps, stepSaveInterval, accuracyCalculationInterval, learningAlgorithm);
    }

    @Override
    public void doLearnStep() {
        List<DataSetRow> rows = new ArrayList<>(dataSet.rowsList());
        Collections.shuffle(rows);
        ConsoleProgressUpdate progress = new ConsoleProgressUpdate("Learning row", rows.size());
        for (DataSetRow dataSetRow : rows) {
            progress.printStateAndIncrement();
            this.network.learn(dataSetRow, learningAlgorithm);
        }
    }
}
