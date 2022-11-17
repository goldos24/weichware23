package sc.player2023.logic;

import kotlin.Pair;
import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;

import javax.annotation.Nonnull;

/**
 * @author Till Fransson
 * @since 17.11.2022
 */
public record Penguin(@Nonnull Pair<Coordinates, ITeam> penguin) {

    @Nonnull
    public Coordinates getFirst() {
        return penguin.getFirst();
    }

    @Nonnull
    public ITeam getSecond() {
        return penguin.getSecond();
    }
}
