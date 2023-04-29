package sc.player2023.logic.nnet.data;

public interface NetworkInputConverter<T> {

    double[] convert(T object);
}
