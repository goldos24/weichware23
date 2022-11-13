package sc.player2023.logic.rating;

import sc.api.plugins.ITeam;
import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;

public class StupidRater implements Rater {
    public StupidRater() {

    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState immutableGameState) {
        ITeam team = immutableGameState.getCurrentTeam();
        ITeam opponent = team.opponent();
        int ownPoints = immutableGameState.getPointsForTeam(team);
        int opponentPoints = immutableGameState.getPointsForTeam(opponent);
        int pointDifference = ownPoints - opponentPoints;
        return new Rating(pointDifference);
    }
}
