package sc.player2023.logic.opposition;

import sc.api.plugins.Coordinates;
import sc.api.plugins.Vector;
import sc.player2023.Direction;

import javax.annotation.Nonnull;

import static sc.player2023.Direction.LEFT;
import static sc.player2023.Direction.TOP_RIGHT;

public record Restriction(@Nonnull Coordinates restrictingPenguin, @Nonnull Direction directionToStartPenguin) {

    private static final Vector left = LEFT.getVector();
    private static final Vector topRight = TOP_RIGHT.getVector();

    public static final double HIGHEST_REACHABLE_ANGLE = VectorMath.angleBetweenVectors(left, topRight);

    public boolean isInRestriction(Coordinates coordinates) {
        Vector vector = coordinates.minus(restrictingPenguin);
        Vector directionVector = directionToStartPenguin.getVector();
        double angle = VectorMath.angleBetweenVectors(directionVector, vector);
        return angle >= HIGHEST_REACHABLE_ANGLE;
    }

}
