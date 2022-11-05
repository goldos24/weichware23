package sc.player2023.logic.rating;

import sc.api.plugins.ITeam;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.ImmutableGameState;

import javax.annotation.Nonnull;

public class StupidRater implements Rater {
    public StupidRater() {

    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState immutableGameState) {
        ITeam team = immutableGameState.getCurrentTeam();
        if (immutableGameState.isOver()) {
            return GameRuleLogic.isTeamWinner(immutableGameState, team) ? Rating.POSITIVE_INFINITY
                    : Rating.NEGATIVE_INFINITY;
        }
        ITeam opponent = team.opponent();
        int ownPoints = immutableGameState.getPointsForTeam(team);
        int opponentPoints = immutableGameState.getPointsForTeam(opponent);
        int pointDifference = ownPoints - opponentPoints;
        return new Rating(pointDifference);
    }
}
