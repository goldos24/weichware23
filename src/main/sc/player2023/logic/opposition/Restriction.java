package sc.player2023.logic.opposition;

import sc.api.plugins.Coordinates;
import sc.api.plugins.Vector;
import sc.player2023.Direction;

import javax.annotation.Nonnull;

import java.util.Objects;

import static sc.player2023.Direction.LEFT;
import static sc.player2023.Direction.TOP_RIGHT;

public final class Restriction {
    private final Coordinates restrictingPenguin;
    private final Direction directionToStartPenguin;

    public Restriction(Coordinates restrictingPenguin, Direction directionToStartPenguin) {
        this.restrictingPenguin = restrictingPenguin;
        this.directionToStartPenguin = directionToStartPenguin;
    }

    public Coordinates restrictingPenguin() {
        return restrictingPenguin;
    }

    public Direction directionToStartPenguin() {
        return directionToStartPenguin;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Restriction) obj;
        return Objects.equals(this.restrictingPenguin, that.restrictingPenguin) &&
                Objects.equals(this.directionToStartPenguin, that.directionToStartPenguin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restrictingPenguin, directionToStartPenguin);
    }

    @Override
    public String toString() {
        return "Restriction[" +
                "restrictingPenguin=" + restrictingPenguin + ", " +
                "directionToStartPenguin=" + directionToStartPenguin + ']';
    }

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
