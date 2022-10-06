package sc.player2023.logic;

import sc.api.plugins.ITeam;
import sc.plugin2023.GameState;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;

public class ImmutableGameState {
    public ImmutableGameState(@Nonnull GameState gameState) {
        this.gameState = gameState.clone();
    }

    @Nonnull private GameState gameState;

    ImmutableGameState withMove(@Nonnull Move move) { // TODO completely move into GameRuleLogic
        ImmutableGameState result = new ImmutableGameState(this.gameState);
        result.gameState.performMove(move);
        return result;
    }

    public int getPointsForTeam(ITeam team) {
        return RatingUtil.getCombinedPointsForTeam(gameState, team);
    }

    GameState getGameState() {
        return gameState.clone();
    }

    boolean isOver() {
        return gameState.isOver();
    }
}
