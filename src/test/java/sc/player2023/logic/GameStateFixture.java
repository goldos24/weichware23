package sc.player2023.logic;

import sc.api.plugins.Team;
import sc.player2023.logic.board.BoardFixture;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.gameState.ImmutableGameStateFactory;
import sc.player2023.logic.score.GameScore;
import sc.player2023.logic.score.PlayerScore;
import sc.player2023.logic.score.Score;
import sc.plugin2023.GameState;
import sc.plugin2023.Move;

import java.util.Optional;
import java.util.function.Function;

import static sc.player2023.logic.score.GameScore.START_SCORE;

/**
 * @author Till Fransson
 * @since 08.10.2022
 */
public class GameStateFixture {


    public static final Score SCORE_TEAM_TWO = Score.ZERO;
    public static final PlayerScore PLAYER_SCORE_TEAM_TWO = new PlayerScore(Team.TWO, SCORE_TEAM_TWO);
    public static final Score SCORE_TEAM_ONE = new Score(5);
    public static final PlayerScore PLAYER_SCORE_TEAM_ONE = new PlayerScore(Team.ONE, SCORE_TEAM_ONE);

    public static ImmutableGameState createTestGameState() {
        GameState gameState = new GameState(BoardFixture.createTestBoard());
        return ImmutableGameStateFactory.createFromGameState(gameState);
    }

    public static ImmutableGameState createTestGameStateOneFishPerField() {
        GameState gameState = new GameState(BoardFixture.createTestBoardOneFishPerField());
        GameScore score = new GameScore(PLAYER_SCORE_TEAM_ONE, PLAYER_SCORE_TEAM_TWO);
        return new ImmutableGameState(gameState, score);
    }

    public static ImmutableGameState createTestGameStateWithFirstMovePerformed() {
        ImmutableGameState currentGameState = createTestGameState();
        Optional<Move> firstMove = GameRuleLogic.getPossibleMoveStream(currentGameState).findFirst();
        assert firstMove.isPresent();
        Move move = firstMove.get();
        return GameRuleLogic.withMovePerformed(currentGameState, move);
    }

    public static ImmutableGameState createPlayedOutTestGameState(ImmutableGameState initialGameState,
                                                                  Function<ImmutableGameState, Move> movePicker) {
        ImmutableGameState currentGameState = initialGameState;
        while (!currentGameState.isOver()) {
            Move move = movePicker.apply(currentGameState);
            currentGameState = GameRuleLogic.withMovePerformed(currentGameState, move);
        }
        return currentGameState;
    }

    public static ImmutableGameState createReachableFishTestGameState() {
        return new ImmutableGameState(START_SCORE, BoardFixture.createImmutableReachableFishRaterTestBoard(),
                                      Team.ONE);
    }

}
