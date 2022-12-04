package sc.player2023.logic.rating;

import sc.api.plugins.ITeam;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.plugin2023.GameState;

import javax.annotation.Nonnull;
import java.util.stream.IntStream;


public class RatingUtil {
    public static int getCombinedScoreForTeam(@Nonnull GameState gameState, ITeam team) {
        return IntStream.of(gameState.getPointsForTeam(team)).sum();
    }

    public static Rating combineTeamRatings(@Nonnull ImmutableGameState gameState, @Nonnull TeamRater teamRater) {
        var ownTeam = gameState.getCurrentTeam();
        var opponentTeam = ownTeam.opponent();
        return teamRater.rateForTeam(gameState, ownTeam).subtract(teamRater.rateForTeam(gameState, opponentTeam));
    }
}
