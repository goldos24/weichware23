package sc.player2023.logic.gameState;

import sc.api.plugins.Team;
import sc.player2023.logic.rating.RatingUtil;
import sc.player2023.logic.score.GameScore;
import sc.player2023.logic.score.PlayerScore;
import sc.player2023.logic.score.Score;
import sc.plugin2023.GameState;

import javax.annotation.Nonnull;

public class ImmutableGameStateFactory {
    @Nonnull
    public static ImmutableGameState createAny() {
        GameState gameState = new GameState();
        return createFromGameState(gameState);
    }

    @Nonnull
    public static ImmutableGameState createFromGameState(@Nonnull GameState gameState) {
        int scoreTeamOne = RatingUtil.getCombinedScoreForTeam(gameState, Team.ONE);
        PlayerScore playerScoreTeamOne = new PlayerScore(Team.ONE, new Score(scoreTeamOne));
        int scoreTeamTwo = RatingUtil.getCombinedScoreForTeam(gameState, Team.TWO);
        PlayerScore playerScoreTeamTwo = new PlayerScore(Team.TWO, new Score(scoreTeamTwo));
        GameScore score = new GameScore(playerScoreTeamOne, playerScoreTeamTwo);
        return new ImmutableGameState(gameState.clone(), score);
    }
}
