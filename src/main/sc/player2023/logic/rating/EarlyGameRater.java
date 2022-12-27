package sc.player2023.logic.rating;

import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;

import static sc.player2023.logic.GameRuleLogic.allPenguinsPlaced;

public record EarlyGameRater(@Nonnull Rater nestedRater) implements Rater {
    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        if(allPenguinsPlaced(gameState.getBoard(), gameState.getCurrentTeam()))
            return Rating.ZERO;
        return nestedRater.rate(gameState);
    }
}
