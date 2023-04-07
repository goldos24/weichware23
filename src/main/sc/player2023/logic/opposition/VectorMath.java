package sc.player2023.logic.opposition;

import sc.api.plugins.Vector;

import javax.annotation.Nonnull;

public class VectorMath {

    private VectorMath() {

    }

    public static double scalarProduct(Vector a, Vector b) {
        return a.getDx() * b.getDx() + a.getDy() * b.getDy();
    }

    public static double absolute(Vector vector) {
        return Math.sqrt(scalarProduct(vector, vector));
    }

    public static double angleBetweenVectors(Vector a, Vector b) {
        return Math.acos(scalarProduct(a, b) / (absolute(a) * absolute(b)));
    }

    public static boolean isCollinear(@Nonnull Vector start, @Nonnull Vector end) {
        return start.getDx() * end.getDy() == start.getDy() * end.getDx();
    }

}
