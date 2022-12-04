package sc.player2023.logic.rating;

import sc.api.plugins.ITeam;
import sc.plugin2023.GameState;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;


public class RatingUtil {
    public static int getCombinedScoreForTeam(@Nonnull GameState gameState, ITeam team) {
        return IntStream.of(gameState.getPointsForTeam(team)).sum();
    }
}
