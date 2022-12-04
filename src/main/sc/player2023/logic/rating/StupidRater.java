package sc.player2023.logic.rating;

import sc.api.plugins.ITeam;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.score.Score;

import javax.annotation.Nonnull;

public class StupidRater implements Rater {
    public StupidRater() {

    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState immutableGameState) {
        ITeam team = immutableGameState.getCurrentTeam();
        ITeam opponent = team.opponent();
        Score ownScore = immutableGameState.getScoreForTeam(team);
        Score opponentScore = immutableGameState.getScoreForTeam(opponent);
        Score scoreDifference = ownScore.subtract(opponentScore);
        return new Rating(scoreDifference.score());
    }
}
