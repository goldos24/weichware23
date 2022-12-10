package sc.player2023.logic.transpositiontable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.rating.Rating;
import sc.plugin2023.Move;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class TransPositionTableTest {

    GameStateSelector selector = gameState -> true;
    GameStateHolderFactory factory = TrivialGameStateHolder::new;

    TransPositionTable transPositionTable;

    ImmutableGameState gameState1;
    ImmutableGameState gameState2;

    @BeforeEach
    void initTransPositionTable() {
        transPositionTable = new TransPositionTable(new HashMap<>(), factory, selector);
    }

    @BeforeEach
    void initGameStates() {
        gameState1 = GameStateFixture.createTestGameState();
        gameState2 = gameState1;
        Move[] moves = {
                new Move(new Coordinates(4, 0), new Coordinates(2, 0)),
                new Move(new Coordinates(5, 1), new Coordinates(3, 1)),
                new Move(new Coordinates(10, 0), new Coordinates(14, 0))
        };
        for (Move move : moves) {
            gameState1 = GameRuleLogic.withMovePerformed(gameState1, move);
        }
        for(int i = moves.length-1; i >= 0; --i) {
            Move move = moves[i];
            gameState2 = GameRuleLogic.withMovePerformed(gameState2, move);
        }
    }

    @Test
    void add() {
        Rating rating = new Rating(42);
        transPositionTable.add(gameState1, rating);
        assertEquals(rating, transPositionTable.getRatingForGameState(gameState1));
    }

    @Test
    void hasGameState() {
        Rating rating = new Rating(42);
        transPositionTable.add(gameState1, rating);
        assertTrue(transPositionTable.hasGameState(gameState1));
    }

    @Test
    void getRatingForGameState() {
        Rating rating = new Rating(42);
        transPositionTable.add(gameState1, rating);
        Rating got = transPositionTable.getRatingForGameState(gameState2);
        assertEquals(rating, got);
    }
}