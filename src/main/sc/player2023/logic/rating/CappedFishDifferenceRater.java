package sc.player2023.logic.rating;

import org.jetbrains.annotations.NotNull;
import sc.api.plugins.ITeam;
import sc.player2023.logic.gameState.ImmutableGameState;

public class CappedFishDifferenceRater implements Rater {

    @Override
    public Rating rate(@NotNull ImmutableGameState gameState) {
        ITeam ownTeam = gameState.getCurrentTeam();
        ITeam opponentTeam = ownTeam.opponent();
        int uncappedResult = gameState.getPointsForTeam(ownTeam) - gameState.getPointsForTeam(opponentTeam);
        if(uncappedResult < -5) {
            return Rating.FIVE.negate();
        }
        return new Rating(uncappedResult);
    }
}
