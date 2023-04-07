package sc.player2023.logic.opposition;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Vector;
import sc.player2023.logic.GameRuleLogic;

import static org.junit.jupiter.api.Assertions.*;

public class VectorMathTest {

    @Test
    void angleBetweenSameVectorsTest() {
        Vector vector = new Vector(3, 5);
        double actual = VectorMath.angleBetweenVectors(vector, vector);
        assertEquals(0, actual, "angleBetweenVectors");
    }

    @Test
    void angleBetweenVectorsTest() {
        Vector a = new Vector(3, 5);
        Vector b = new Vector(-5, 3);
        double actual = VectorMath.angleBetweenVectors(a, b);
        assertEquals(Math.PI/2.0, actual, "angleBetweenVectors");
    }

    @Test
    void collinearityTestSimpleVertical() {
        Vector start = new Vector(0, 2);
        Vector end = new Vector(0, 8);
        assertTrue(VectorMath.isCollinear(start, end));
    }

    @Test
    void collinearityTestSimpleHorizontal() {
        Vector start = new Vector(2, 0);
        Vector end = new Vector(8, 0);
        assertTrue(VectorMath.isCollinear(start, end));
    }

    @Test
    void collinearityTestSimpleDiagonal() {
        Vector start = new Vector(1, 1);
        Vector end = new Vector(4, 4);
        assertTrue(VectorMath.isCollinear(start, end));
    }

    @Test
    void collinearityTestNegativeDiagonal() {
        Vector start = new Vector(-1, -1);
        Vector end = new Vector(4, 4);
        assertTrue(VectorMath.isCollinear(start, end));
    }

    @Test
    void collinearityTestNegativeDiagonalFalse() {
        Vector start = new Vector(-1, 1);
        Vector end = new Vector(4, 4);
        assertFalse(VectorMath.isCollinear(start, end));
    }

    @Test
    void collinearityTestNegativeDiagonalTrue() {
        Vector start = new Vector(-1, 1);
        Vector end = new Vector(4, -4);
        assertTrue(VectorMath.isCollinear(start, end));
    }


}
