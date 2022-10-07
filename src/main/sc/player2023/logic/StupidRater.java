package sc.player2023.logic;

import sc.api.plugins.ITeam;
import sc.plugin2023.GameState;

import javax.annotation.Nonnull;

public class StupidRater implements Rater {
    public StupidRater()
    {

    }

    @Override
    public int rate(@Nonnull ImmutableGameState immutableGameState)
    {
        GameState gameState = immutableGameState.gameState();
        if(gameState.isOver()) {
            return RatingUtil.isTeamWinnerAfterGameEnd(gameState, gameState.getCurrentTeam()) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        ITeam team = gameState.getCurrentTeam();
        ITeam opponent = team.opponent();
        int ownPoints = RatingUtil.getCombinedPointsForTeam(gameState, team);
        int opponentPoints = RatingUtil.getCombinedPointsForTeam(gameState, opponent);
        return ownPoints - opponentPoints;
    }
}
