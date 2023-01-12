package sc.player2023;

import sc.api.plugins.Vector;

import javax.annotation.Nonnull;

import static sc.player2023.TileChange.*;

/**
 * @author Till Fransson
 * @since 12.01.2023
 */
public enum Direction {


    LEFT(new Vector(-FULL_TILE_X_CHANGE.getChange(), 0)),
    TOP_LEFT(new Vector(-HALF_TILE_X_CHANGE.getChange(), FULL_TILE_Y_CHANGE.getChange())),
    TOP_RIGHT(new Vector(HALF_TILE_X_CHANGE.getChange(), FULL_TILE_Y_CHANGE.getChange())),
    RIGHT(new Vector(FULL_TILE_X_CHANGE.getChange(), 0)),
    BOTTOM_RIGHT(new Vector(HALF_TILE_X_CHANGE.getChange(), -FULL_TILE_Y_CHANGE.getChange())),
    BOTTOM_LEFT(new Vector(-HALF_TILE_X_CHANGE.getChange(), -FULL_TILE_Y_CHANGE.getChange()));

    @Nonnull
    private final Vector directionVector;

    Direction(@Nonnull Vector directionVector) {
        this.directionVector = directionVector;
    }

    @Nonnull
    public Vector getVector() {
        return directionVector;
    }
}
