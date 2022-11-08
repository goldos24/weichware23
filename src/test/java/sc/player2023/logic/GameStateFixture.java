package sc.player2023.logic;

import sc.api.plugins.Team;
import sc.plugin2023.GameState;
import sc.plugin2023.Move;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * @author Till Fransson
 * @since 08.10.2022
 */
public class GameStateFixture {


    public static final int POINTS_TEAM_TWO = 0;
    public static final int POINTS_TEAM_ONE = 5;

    public static ImmutableGameState createTestGameState() {
        GameState gameState = new GameState(BoardFixture.createTestBoard());
        return ImmutableGameStateFactory.createFromGameState(gameState);
    }

    public static ImmutableGameState createTestGameStateOneFishPerField() {
        GameState gameState = new GameState(BoardFixture.createTestBoardOneFishPerField());
        Integer[] pointsMap = new Integer[] {POINTS_TEAM_ONE, POINTS_TEAM_TWO};
        return new ImmutableGameState(gameState, pointsMap);
    }

    public static ImmutableGameState createTestGameStateWithFirstMovePerformed() {
        ImmutableGameState currentGameState = createTestGameState();
        Optional<Move> firstMove = GameRuleLogic.getPossibleMoveStream(currentGameState).findFirst();
        assert firstMove.isPresent();
        Move move = firstMove.get();
        return GameRuleLogic.withMovePerformed(currentGameState, move);
    }

    public static ImmutableGameState createPlayedOutTestGameState(ImmutableGameState initialGameState, Function<ImmutableGameState, Move> movePicker) {
        ImmutableGameState currentGameState = initialGameState;
        while (!currentGameState.isOver()) {
            Move move = movePicker.apply(currentGameState);
            currentGameState = GameRuleLogic.withMovePerformed(currentGameState, move);
        }
        return currentGameState;
    }

    public static ImmutableGameState createReachableFishTestGameState() {
        return new ImmutableGameState(new Integer[] {0,0}, BoardFixture.createImmutableReachableFishRaterTestBoard(),
                Team.ONE);
    }

}
