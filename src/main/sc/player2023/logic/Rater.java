package sc.player2023.logic;
import sc.api.plugins.ITeam;
import sc.plugin2023.GameState;
import javax.annotation.Nonnull;

public interface Rater {
    int rate(@Nonnull ImmutableGameState gameState);
}
