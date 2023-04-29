package sc.player2023.logic.nnet.dojos;

import sc.player2023.logic.nnet.LearningAlgorithm;
import sc.player2023.logic.nnet.NeuralNetwork;
import sc.player2023.logic.nnet.learning.DataSet;
import sc.player2023.logic.nnet.learning.DataSetRow;

public class LearningDojo {

    protected final NeuralNetwork network;
    protected final DataSet dataSet;
    protected final int steps;
    protected final LearningAlgorithm learningAlgorithm;

    public LearningDojo(NeuralNetwork network, DataSet dataSet, int steps, LearningAlgorithm learningAlgorithm) {
        this.network = network;
        this.dataSet = dataSet;
        this.steps = steps;
        this.learningAlgorithm = learningAlgorithm;
    }

    public void doLearnStep() {
        for (DataSetRow dataSetRow : dataSet.rowsList()) {
            this.network.learn(dataSetRow, learningAlgorithm);
        }
    }

    public void learn() {
        for (int i = 0; i < steps; ++i) {
            this.doLearnStep();
        }
    }
}
