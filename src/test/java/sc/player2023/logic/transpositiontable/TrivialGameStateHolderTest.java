package sc.player2023.logic.transpositiontable;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Team;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.score.GameScore;
import sc.plugin2023.Board;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Till Fransson
 * @since 23.12.2022
 */
public class TrivialGameStateHolderTest {

    private final BoardPeek board = new BoardPeek(new Board());
    ImmutableGameState gameState = new ImmutableGameState(new GameScore(12, 52), board, Team.TWO);
    TrivialGameStateHolder holder = new TrivialGameStateHolder(gameState);
    
    @Test
    void getGameState() {
        assertEquals(gameState, holder.getGameState(), "getGameState");
    }
}