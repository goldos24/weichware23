package sc.player2023.logic.pvs;

import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;

/**
 * @author Till Fransson
 * @since 13.12.2022
 */
public final class MoveGetterUtil {
    
    private MoveGetterUtil() {
        
    }


    static int getRatingFactorForNextMove(@Nonnull ImmutableGameState gameState) {
        if(GameRuleLogic.anyPossibleMovesForPlayer(gameState.getBoard(), gameState.getCurrentTeam())) {
            return -1;
        }
        return 1;
    }
}
