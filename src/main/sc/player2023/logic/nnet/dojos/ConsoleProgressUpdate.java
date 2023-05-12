package sc.player2023.logic.nnet.dojos;

import java.io.PrintStream;

public class ConsoleProgressUpdate {

    private final PrintStream outputStream;

    private final String purpose;
    private final int max;
    private int current;

    public ConsoleProgressUpdate(PrintStream outputStream, String purpose, int max) {
        this.outputStream = outputStream;
        this.purpose = purpose;
        this.max = max;
        this.current = 0;
    }

    public ConsoleProgressUpdate(String purpose, int max) {
        this.outputStream = System.out;
        this.purpose = purpose;
        this.max = max;
        this.current = 0;
    }

    public void printStateAndIncrement() {
        assert this.current < this.max;
        this.current++;
        double percentage = this.current / (double) this.max * 100;
        this.outputStream.printf("\r%s: %d / %d (%.3f%%)", this.purpose, this.current, this.max, percentage);

        if (this.current == this.max) {
            this.outputStream.println();
        }
    }
}
