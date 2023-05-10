package sc.player2023.logic.nnet.dojos;

import sc.player2023.logic.nnet.LearningAlgorithm;
import sc.player2023.logic.nnet.NeuralNetwork;
import sc.player2023.logic.nnet.NeuralNetworkSerializer;
import sc.player2023.logic.nnet.learning.DataSet;
import sc.player2023.logic.nnet.learning.DataSetRow;

import java.io.IOException;

public class LearningDojo {

    protected final NeuralNetwork network;
    protected final DataSet dataSet;
    protected final int steps;
    protected final int stepSaveInterval;
    protected final LearningAlgorithm learningAlgorithm;

    public LearningDojo(NeuralNetwork network, DataSet dataSet, int steps, int stepSaveInterval, LearningAlgorithm learningAlgorithm) {
        this.network = network;
        this.dataSet = dataSet;
        this.steps = steps;
        this.stepSaveInterval = stepSaveInterval;
        this.learningAlgorithm = learningAlgorithm;
    }

    public void doLearnStep() {
        for (DataSetRow dataSetRow : dataSet.rowsList()) {
            this.network.learn(dataSetRow, learningAlgorithm);
        }
    }

    public void learn() {
        for (int step = 0; step < steps; ++step) {
            this.doLearnStep();
            if (step % stepSaveInterval == 0) {
                this.saveNetwork();
            }
        }
    }

    private void saveNetwork() {
        try {
            NeuralNetworkSerializer.save("network.ser", this.network);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
