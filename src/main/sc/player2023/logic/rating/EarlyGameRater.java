package sc.player2023.logic.rating;

import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;

import static sc.player2023.logic.GameRuleLogic.allPenguinsPlaced;

public final class EarlyGameRater
        implements Rater {
    public EarlyGameRater(@Nonnull
                          Rater nestedRater) {
        this.nestedRater = nestedRater;
    }

    @Nonnull Rater nestedRater;

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj != null && obj.getClass() == this.getClass();
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "EarlyGameRater[]";
    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        if(allPenguinsPlaced(gameState.getBoard(), gameState.getCurrentTeam()))
            return Rating.ZERO;
        return nestedRater.rate(gameState);
    }
}
