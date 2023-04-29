package sc.player2023.logic.nnet.math;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MatrixTest {

    @Test
    void matrixHasGoodDimensions() {
        assertDoesNotThrow(() -> {
            new Matrix(new double[][]{
                {1.0, 0.25, 5.3},
                {12.0, 8.3, 1.3},
            });
        });
    }

    @Test
    void matrixGetDimensions() {
        Matrix matrix = new Matrix(new double[][]{
            {1.0, 0.25, 5.3},
            {12.0, 8.3, 1.3},
        });
        assertEquals(new MatrixDimensions(2, 3), matrix.getDimensions());
    }

    @Test
    void matrixHasBadDimensions() {
        assertThrows(MatrixDimensionsException.class, () ->
            new Matrix(new double[][]{
                {1.0, 0.25, 5.3, 0.9},
                {12.0, 8.3, 1.3},
            }), "Incorrect matrix dimensions: 3 columns on row 1, 4 columns expected");
    }

    @Test
    void matrixRawAndSizeConstructorsCreateSameMatrix() {
        Matrix first = new Matrix(new double[][]{
            {0.0, 0.0, 0.0},
            {0.0, 0.0, 0.0},
        });
        Matrix second = new Matrix(2, 3);
        assertEquals(first, second);
    }

    @Test
    void addMatrices() {
        Matrix first = new Matrix(new double[][]{
            {1.0, 0.25, 5.3},
            {12.0, 8.3, 1.3},
        });
        Matrix second = new Matrix(new double[][]{
            {6.0, 0.56, 0.9},
            {1.0, -5.2, 7.4},
        });
        Matrix expected = new Matrix(new double[][]{
            {7.0, 0.81, 6.2},
            {13.0, 3.1000000000000005, 8.700000000000001},
        });
        Matrix actual = first.add(second);
        assertEquals(expected, actual);
    }

    @Test
    void addMatricesWithDifferentDimensionsFails() {
        Matrix first = new Matrix(1, 7);
        Matrix second = new Matrix(4, 3);
        assertThrows(AssertionError.class, () -> first.add(second));
    }

    @Test
    void mapMultiplyMatrix() {
        Matrix first = new Matrix(new double[][]{
            {12, -7},
            {5.6, 0.1}
        });
        Matrix expected = new Matrix(new double[][]{
            {-12, 7},
            {-5.6, -0.1}
        });
        Matrix actual = first.negate();
        assertEquals(expected, actual);
    }

    @Test
    void negateMatrix() {
        Matrix first = new Matrix(new double[][]{
            {12, -7},
            {5.6, 0.1}
        });
        Matrix expected = new Matrix(new double[][]{
            {-12, 7},
            {-5.6, -0.1}
        });
        Matrix actual = first.negate();
        assertEquals(expected, actual);
    }

    @Test
    void subtractMatrices() {
        Matrix first = new Matrix(new double[][]{
            {7, 6},
            {1.1, 2}
        });
        Matrix second = new Matrix(new double[][]{
            {2.7, -6},
            {5, 0}
        });
        Matrix expected = new Matrix(new double[][]{
            {4.3, 12},
            {-3.9, 2}
        });
        Matrix actual = first.subtract(second);
        assertEquals(expected, actual);
    }

    @Test
    void scaleMatrix() {
        Matrix matrix = new Matrix(new double[][]{
            {1.1, 2, 7},
            {7, 1, 3.6},
            {0.9, 12, 0},
        });
        Matrix expected = new Matrix(new double[][]{
            {4.4, 8, 28},
            {28, 4, 14.4},
            {3.6, 48, 0},
        });
        Matrix actual = matrix.scale(4);
        assertEquals(expected, actual);
    }

    @Test
    void multiplyMatrices() {
        Matrix first = new Matrix(new double[][]{
            {3, 0.7, -2.5, 2},
            {12, 1, 0.8, 1}
        });
        Matrix second = new Matrix(new double[][]{
            {2, 9},
            {4.5, -1},
            {5, 2},
            {-14, 8.4}
        });
        Matrix expected = new Matrix(new double[][]{
            {-31.35, 38.1},
            {18.5, 117}
        });
        Matrix actual = first.multiply(second);
        assertEquals(expected, actual);
    }

    @Test
    void hadamardMultiplyMatrices() {
        Matrix first = new Matrix(new double[][]{
            {5, 4.3, 2, -7},
            {0, -9.1, 5, 9.4},
            {1, 2, 7, 0.1}
        });
        Matrix second = new Matrix(new double[][]{
            {0.1, 90, 57.4, 1},
            {9, -2, -5, 4},
            {1, 3.131, 6, -1}
        });
        Matrix expected = new Matrix(new double[][]{
            {0.5, 387.0, 114.8, -7},
            {0, 18.2, -25, 37.6},
            {1, 6.262, 42, -0.1}
        });
        Matrix actual = first.multiplyHadamard(second);
        assertEquals(expected, actual);
    }

    @Test
    void multiplyHadamardMatricesWithDifferentDimensionsFails() {
        Matrix first = new Matrix(9, 7);
        Matrix second = new Matrix(9, 3);
        assertThrows(AssertionError.class, () -> first.multiplyHadamard(second));
    }

    @Test
    void transposeMatrix() {
        Matrix matrix = new Matrix(new double[][]{
            {0.7, 5, -9, 4.5},
            {11, 2, 1.1, -12}
        });
        Matrix expected = new Matrix(new double[][]{
            {0.7, 11},
            {5, 2},
            {-9, 1.1},
            {4.5, -12},
        });
        Matrix actual = matrix.transpose();
        assertEquals(expected, actual);
    }

    @Test
    void frobeniusNormOfMatrix() {
        Matrix matrix = new Matrix(new double[][]{
            {-8, -9},
            {-1, -1},
            {7, 4}
        });
        double expected = 14.560219778561036;
        double actual = matrix.frobeniusNorm();

        assertEquals(expected, actual);
    }

    @Test
    void flattenMatrix() {
        Matrix matrix = new Matrix(new double[][]{
            {1.17, 0, 1},
            {71.6, 4, 9},
            {90, 6, -5},
        });
        List<Double> expected = List.of(1.17, 0.0, 1.0, 71.6, 4.0, 9.0, 90.0, 6.0, -5.0);
        List<Double> actual = matrix.flatten();
        assertEquals(expected, actual);
    }
}
