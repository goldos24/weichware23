package sc.player2023.logic.rating;

import sc.api.plugins.ITeam;
import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;

public interface TeamRater {
    Rating rateForTeam(@Nonnull ImmutableGameState gameState, @Nonnull ITeam team);
}
