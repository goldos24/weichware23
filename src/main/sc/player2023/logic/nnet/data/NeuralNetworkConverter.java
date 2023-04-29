package sc.player2023.logic.nnet.data;

import sc.player2023.logic.nnet.NeuralNetwork;

public class NeuralNetworkConverter<I, O> {

    private final NetworkInputConverter<I> inputConverter;
    private final NetworkOutputConverter<O> outputConverter;

    private final NeuralNetwork network;

    public NeuralNetworkConverter(NetworkInputConverter<I> inputConverter, NetworkOutputConverter<O> outputConverter, NeuralNetwork network) {
        this.inputConverter = inputConverter;
        this.outputConverter = outputConverter;
        this.network = network;
    }

    public O convert(I data) {
        double[] inputData = this.inputConverter.convert(data);
        double[] outputs = this.network.propagate(inputData);
        return this.outputConverter.convert(outputs);
    }

    public NeuralNetwork getNetwork() {
        return network;
    }
}
