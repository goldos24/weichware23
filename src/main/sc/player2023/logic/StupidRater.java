package sc.player2023.logic;

import sc.api.plugins.ITeam;
import sc.plugin2023.GameState;

import javax.annotation.Nonnull;

public class StupidRater implements Rater {
    public StupidRater() {

    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState immutableGameState) {
        GameState gameState = immutableGameState.getGameState();
        if (gameState.isOver()) {
            return RatingUtil.isTeamWinnerAfterGameEnd(gameState, gameState.getCurrentTeam()) ? Rating.POSITIVE_INFINITY
                    : Rating.NEGATIVE_INFINITY;
        }
        ITeam team = gameState.getCurrentTeam();
        ITeam opponent = team.opponent();
        int ownPoints = immutableGameState.getPointsForTeam(team);
        int opponentPoints = immutableGameState.getPointsForTeam(opponent);
        int pointDifference = ownPoints - opponentPoints;
        return new Rating(pointDifference);
    }
}
