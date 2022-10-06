package sc.player2023.logic;

import javax.annotation.Nonnull;
import sc.api.plugins.ITeam;
import sc.plugin2023.GameState;
import sc.plugin2023.Move;
public interface MoveGetter {
    Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull ITeam team, @Nonnull Rater rater);
}
