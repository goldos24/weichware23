package sc.player2023;

/**
 * @author Till Fransson
 * @since 12.01.2023
 */
public enum TileChange {

    HALF_TILE_X_CHANGE(1),
    FULL_TILE_X_CHANGE(2),
    FULL_TILE_Y_CHANGE(1);

    private final int change;

    TileChange(int change) {
        this.change = change;
    }

    public int getChange() {
        return change;
    }
}
