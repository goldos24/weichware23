package sc.player2023.logic.rating;

import sc.api.plugins.ITeam;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.score.Score;

import javax.annotation.Nonnull;

public class CappedFishDifferenceRater implements Rater {

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        ITeam ownTeam = gameState.getCurrentTeam();
        ITeam opponentTeam = ownTeam.opponent();
        Score ownScore = gameState.getScoreForTeam(ownTeam);
        Score opponentScore = gameState.getScoreForTeam(opponentTeam);
        int uncappedResult = ownScore.subtract(opponentScore).score();
        if (uncappedResult < -5) {
            return Rating.FIVE.negate();
        }
        return new Rating(uncappedResult);
    }
}
