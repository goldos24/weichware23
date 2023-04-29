package sc.player2023.logic.nnet.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Matrix {

    private final double[][] values;
    private final MatrixDimensions dimensions;

    public Matrix(double[][] values) {
        this.values = values;
        int rows = values.length;
        assert rows > 0;

        int columns = values[0].length;
        for (int i = 1; i < rows; ++i) {
            int currentColumns = values[i].length;
            if (currentColumns != columns) {
                String message = String.format("Incorrect matrix dimensions: %d columns on row %d, %d columns expected",
                    currentColumns, i, columns);
                throw new MatrixDimensionsException(message);
            }
        }
        this.dimensions = new MatrixDimensions(rows, columns);
    }
    
    public static Matrix rowOf(double... values) {
        Matrix matrix = new Matrix(1, values.length);
        System.arraycopy(values, 0, matrix.values[0], 0, values.length);
        return matrix;
    }

    public static Matrix columnOf(double... values) {
        Matrix matrix = new Matrix(values.length, 1);
        for (int row = 0; row < values.length; ++row) {
            matrix.values[row][0] = values[row];
        }
        return matrix;
    }

    public Matrix(int rows, int columns) {
        this.values = new double[rows][columns];
        this.dimensions = new MatrixDimensions(rows, columns);
    }

    public Matrix(MatrixDimensions dimensions) {
        this(dimensions.rows(), dimensions.columns());
    }

    public static Matrix fromInitializer(MatrixDimensions dimensions, MatrixInitializer initializer) {
        Matrix matrix = new Matrix(dimensions);
        for (int row = 0; row < dimensions.rows(); ++row) {
            for (int column = 0; column < dimensions.columns(); ++column) {
                matrix.values[row][column] = initializer.getValue(row, column);
            }
        }
        return matrix;
    }

    public MatrixDimensions getDimensions() {
        return this.dimensions;
    }

    public Matrix add(Matrix other) {
        assert this.dimensions.equals(other.dimensions);

        Matrix result = new Matrix(this.dimensions);
        for (int row = 0; row < this.dimensions.rows(); ++row) {
            for (int column = 0; column < this.dimensions.columns(); ++column) {
                double thisValue = this.values[row][column];
                double otherValue = other.values[row][column];
                result.values[row][column] = thisValue + otherValue;
            }
        }

        return result;
    }

    public Matrix map(Function<Double, Double> function) {
        Matrix result = new Matrix(this.dimensions);
        for (int row = 0; row < this.dimensions.rows(); ++row) {
            for (int column = 0; column < this.dimensions.columns(); ++column) {
                result.values[row][column] = function.apply(this.values[row][column]);
            }
        }

        return result;
    }
    public Matrix negate() {
        return this.map(value -> -value);
    }

    public Matrix subtract(Matrix other) {
        return this.add(other.negate());
    }

    public Matrix scale(double scalar) {
        return this.map(value -> scalar * value);
    }

    public Matrix zip(Matrix other, BiFunction<Double, Double, Double> function) {
        assert other.dimensions.equals(this.dimensions);
        Matrix result = new Matrix(this.dimensions);
        for (int row = 0; row < this.dimensions.rows(); ++row) {
            for (int column = 0; column < this.dimensions.columns(); ++column) {
                double firstValue = this.values[row][column];
                double secondValue = other.values[row][column];
                result.values[row][column] = function.apply(firstValue, secondValue);
            }
        }

        return result;
    }

    public Matrix multiply(Matrix other) {
        int selfRows = this.dimensions.rows();
        int selfColumns = this.dimensions.columns();
        int otherRows = other.dimensions.rows();
        int otherColumns = other.dimensions.columns();

        assert selfColumns == otherRows;

        Matrix result = new Matrix(selfRows, otherColumns);

        for (int row = 0; row < selfRows; ++row) {
            for (int otherColumn = 0; otherColumn < otherColumns; ++otherColumn) {
                double sum = 0;
                for (int column = 0; column < selfColumns; ++column) {
                    sum += this.values[row][column] * other.values[column][otherColumn];
                }
                result.values[row][otherColumn] = sum;
            }
        }

        return result;
    }

    public Matrix multiplyHadamard(Matrix other) {
        return this.zip(other, (a, b) -> a * b);
    }

    public Matrix transpose() {
        Matrix result = new Matrix(this.dimensions.transpose());
        for (int row = 0; row < this.dimensions.rows(); ++row) {
            for (int column = 0; column < this.dimensions.columns(); ++column) {
                result.values[column][row] = this.values[row][column];
            }
        }

        return result;
    }

    public List<Double> flatten() {
        List<Double> values = new ArrayList<>();
        for (int row = 0; row < this.dimensions.rows(); ++row) {
            for (int column = 0; column < this.dimensions.columns(); ++column) {
                values.add(this.values[row][column]);
            }
        }

        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Matrix matrix = (Matrix) o;

        return Arrays.deepEquals(values, matrix.values);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(values);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < this.dimensions.rows(); ++row) {
            builder.append(Arrays.toString(this.values[row]));
            builder.append("\n");
        }
        return builder.toString();
    }
}
