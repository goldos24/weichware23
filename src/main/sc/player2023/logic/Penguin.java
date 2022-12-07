package sc.player2023.logic;

import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;

import javax.annotation.Nonnull;

/**
 * @author Till Fransson
 * @since 17.11.2022
 */
public record Penguin(@Nonnull Coordinates coordinates, @Nonnull Team team) {

}
