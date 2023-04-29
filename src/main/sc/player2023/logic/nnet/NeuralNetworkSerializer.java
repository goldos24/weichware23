package sc.player2023.logic.nnet;

import java.io.*;

public class NeuralNetworkSerializer {

    public static void save(String fileName, NeuralNetwork network) throws IOException {
        FileOutputStream fileStream = new FileOutputStream(fileName);
        ObjectOutputStream outStream = new ObjectOutputStream(fileStream);

        outStream.writeObject(network);

        fileStream.close();
        outStream.close();
    }

    public static NeuralNetwork load(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream(fileName);
        ObjectInputStream inStream = new ObjectInputStream(fileStream);

        NeuralNetwork network = (NeuralNetwork) inStream.readObject();

        fileStream.close();
        inStream.close();

        return network;
    }
}
