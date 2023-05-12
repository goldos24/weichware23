package sc.player2023.logic.nnet.math;

import java.io.Serializable;

public record MatrixDimensions(int rows, int columns) implements Serializable {

    public MatrixDimensions transpose() {
        return new MatrixDimensions(columns, rows);
    }
}
