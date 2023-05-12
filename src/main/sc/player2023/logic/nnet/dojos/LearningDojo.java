package sc.player2023.logic.nnet.dojos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.nnet.LearningAlgorithm;
import sc.player2023.logic.nnet.NeuralNetwork;
import sc.player2023.logic.nnet.NeuralNetworkSerializer;
import sc.player2023.logic.nnet.learning.DataSet;
import sc.player2023.logic.nnet.learning.DataSetRow;
import sc.player2023.logic.nnet.math.Matrix;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class LearningDojo {

    public static Logger log = LoggerFactory.getLogger(LearningDojo.class);

    protected final NeuralNetwork network;
    protected final DataSet dataSet;
    protected final int steps;
    protected final int stepSaveInterval;
    protected final int accuracyCalculationInterval;
    protected final LearningAlgorithm learningAlgorithm;

    public LearningDojo(NeuralNetwork network, DataSet dataSet, int steps, int stepSaveInterval, int accuracyCalculationInterval, LearningAlgorithm learningAlgorithm) {
        this.network = network;
        this.dataSet = dataSet;
        this.steps = steps;
        this.stepSaveInterval = stepSaveInterval;
        this.accuracyCalculationInterval = accuracyCalculationInterval;
        this.learningAlgorithm = learningAlgorithm;
    }

    public void doLearnStep() {
        List<DataSetRow> rows = dataSet.rowsList();
        ConsoleProgressUpdate progress = new ConsoleProgressUpdate("Learning row", rows.size());
        for (DataSetRow dataSetRow : rows) {
            progress.printStateAndIncrement();
            this.network.learn(dataSetRow, learningAlgorithm);
        }
    }

    private final Random random = new Random();

    private double calculateNetworkErrorFromRandomSample() {
        List<DataSetRow> rows = this.dataSet.rowsList();
        int index = random.nextInt(rows.size());
        DataSetRow row = rows.get(index);
        double[] outputs = network.propagate(row.inputs());
        Matrix output = Matrix.columnOf(outputs);
        Matrix target = Matrix.columnOf(row.targetOutputs());
        return this.learningAlgorithm.costFunctionForTotalError().calculateTotal(output, target);
    }

    public void learn() {
        for (int step = 0; step < steps; ++step) {
            this.doLearnStep();

            if ((step + 1) % this.accuracyCalculationInterval == 0) {
                log.info("Network error: " + this.calculateNetworkErrorFromRandomSample());
            }

            if ((step + 1) % this.stepSaveInterval == 0) {
                log.info("Saving network");
                this.saveNetwork();
                log.info("Done saving network");
            }
        }
        this.saveNetwork();
    }

    private void saveNetwork() {
        try {
            NeuralNetworkSerializer.save("network.ser", this.network);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
