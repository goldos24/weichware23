package sc.player2023.logic.nnet;

import sc.player2023.logic.nnet.learning.DataSet;
import sc.player2023.logic.nnet.learning.DataSetRow;
import sc.player2023.logic.nnet.math.Matrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NeuralNetwork implements Serializable {

    private final int inputs;
    private final int outputs;

    private final Matrix[] weights;
    private final Matrix[] biases;

    private final ActivationFunction[] activationFunctions;

    public NeuralNetwork(List<Matrix> weights, List<Matrix> biases, List<ActivationFunction> activationFunctions) {
        assert weights.size() >= 1;
        this.inputs = weights.get(0).getDimensions().columns();
        Matrix lastWeight = weights.get(weights.size() - 1);
        this.outputs = lastWeight.getDimensions().rows();

        this.weights = weights.toArray(new Matrix[0]);
        this.biases = biases.toArray(new Matrix[0]);
        this.activationFunctions = activationFunctions.toArray(new ActivationFunction[0]);
    }

    private void verifyCorrectInputSize(double[] inputs) {
        if (inputs.length != this.inputs) {
            String message = String.format("Incorrect input size: got %d, expected %d",
                inputs.length, this.inputs);
            throw new NeuralNetworkPropagateException(message);
        }
    }

    private Matrix propagateInputArray(double[] inputs) {
        Matrix current = Matrix.columnOf(inputs);
        for (int i = 0; i < this.weights.length; ++i) {
            Matrix weights = this.weights[i];
            Matrix biases = this.biases[i];
            ActivationFunction activationFunction = this.activationFunctions[i];

            current = weights.multiply(current);
            current = current.add(biases);
            current = current.map(value -> activationFunction.calculate(value).value());
        }
        return current;
    }

    public double[] propagate(double... inputs) {
        verifyCorrectInputSize(inputs);

        Matrix current = propagateInputArray(inputs);

        List<Double> result = current.flatten();
        assert result.size() == this.outputs;
        return result.stream().mapToDouble(Double::doubleValue).toArray();
    }

    private List<Matrix> accumulatePropagateInputArray(double[] inputs) {
        Matrix current = Matrix.columnOf(inputs);

        List<Matrix> matrices = new ArrayList<>();

        matrices.add(current);

        for (int i = 0; i < this.weights.length; ++i) {
            Matrix weights = this.weights[i];
            Matrix biases = this.biases[i];
            ActivationFunction activationFunction = this.activationFunctions[i];

            current = weights.multiply(current);
            current = current.add(biases);
            current = current.map(value -> activationFunction.calculate(value).value());
            matrices.add(current);
        }
        return matrices;
    }

    public void learn(double[] inputs, double[] targetOutputs, LearningAlgorithm learningAlgorithm) {
        verifyCorrectInputSize(inputs);

        List<Matrix> layerOutputs = this.accumulatePropagateInputArray(inputs);
        Matrix target = Matrix.columnOf(targetOutputs);
        Matrix outputOutputs = layerOutputs.get(layerOutputs.size() - 1);
        Matrix error = target.subtract(outputOutputs);

        CostFunction costFunction = learningAlgorithm.costFunctionForTotalError();
        double totalError = costFunction.calculateTotal(outputOutputs, target);
        System.out.println("Error: " + totalError);

        List<Matrix> gradients = new ArrayList<>();
        List<Matrix> deltas = new ArrayList<>();
        for (int i = this.weights.length - 1; i >= 0; --i) {
            ActivationFunction activationFunction = this.activationFunctions[i];
            Matrix outputs = layerOutputs.get(i + 1);
            Matrix gradient = activationFunction.getGradient(outputs).multiplyHadamard(error);
            Matrix weightsT = this.weights[i].transpose();
            error = weightsT.multiply(error);
            gradients.add(gradient);

            Matrix outputsBefore = layerOutputs.get(i);
            Matrix delta = gradient.multiply(outputsBefore.transpose());
            deltas.add(delta);
        }

        Collections.reverse(deltas);
        Collections.reverse(gradients);
        this.updateNetwork(learningAlgorithm, deltas, gradients);
    }

    private void updateNetwork(LearningAlgorithm learningAlgorithm, List<Matrix> weightDeltas, List<Matrix> gradients) {
        for (int i = 0; i < weightDeltas.size(); ++i) {
            Matrix delta = weightDeltas.get(i);
            Matrix gradient = gradients.get(i);

            this.weights[i] = learningAlgorithm.updateParameter(this.weights[i], delta);
            this.biases[i] = learningAlgorithm.updateParameter(this.biases[i], gradient);
        }
    }

    public void learn(DataSetRow row, LearningAlgorithm learningAlgorithm) {
        this.learn(row.inputs(), row.targetOutputs(), learningAlgorithm);
    }

    public void learn(DataSet dataSet, LearningAlgorithm learningAlgorithm) {
        List<DataSetRow> rows = dataSet.rowsList();
        for (DataSetRow row : rows) {
            this.learn(row, learningAlgorithm);
        }
    }
}
