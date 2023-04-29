package sc.player2023.logic.nnet.data;

public interface NetworkOutputConverter<T> {

    T convert(double[] outputs);
}
