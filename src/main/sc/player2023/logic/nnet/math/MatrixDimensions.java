package sc.player2023.logic.nnet.math;

public record MatrixDimensions(int rows, int columns) {

    public MatrixDimensions transpose() {
        return new MatrixDimensions(columns, rows);
    }
}
