package sc.player2023.logic.nnet.dojos;

import java.io.OutputStream;
import java.io.PrintStream;

public class ConsoleOutputUtil {

    private static PrintStream previousStream;

    public static void enableIO() {
        System.setOut(previousStream);
    }

    public static void disableIO() {
        previousStream = System.out;
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int i) {}
        }));
    }

    public static PrintStream getOut() {
        return previousStream;
    }
}
