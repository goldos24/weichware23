package sc.player2023.logic;

import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.player2023.logic.gameState.ImmutableGameState;

public class GameOutcomePredictor {

    public static ImmutableGameState fullyPlayout(ImmutableGameState gameState) {
        while (!gameState.isOver()) {
            gameState = GameRuleLogic.withRandomMovePerformed(gameState);
        }
        return gameState;
    }

    public static ITeam getWinner(ImmutableGameState gameState) {
        int teamOneScore = gameState.getScoreForTeam(Team.ONE).score();
        int teamTwoScore = gameState.getScoreForTeam(Team.TWO).score();
        if (teamOneScore > teamTwoScore) {
            return Team.ONE;
        } else if (teamTwoScore > teamOneScore) {
            return Team.TWO;
        }
        return null;
    }

    public static GameOutcomePrediction doPredictionForGameState(ImmutableGameState gameState, int simulatedGames) {
        assert !gameState.isOver();

        int teamOneWins = 0, teamOnePoints = 0;
        int teamTwoWins = 0, teamTwoPoints = 0;
        for (int i = 0; i < simulatedGames; ++i) {
            ImmutableGameState playout = fullyPlayout(gameState);

            ITeam winner = getWinner(playout);
            if (winner == Team.ONE)
                teamOneWins++;
            else if (winner == Team.TWO)
                teamTwoWins++;

            teamOnePoints += playout.getScoreForTeam(Team.ONE).score();
            teamTwoPoints += playout.getScoreForTeam(Team.TWO).score();
        }

        int totalPoints = teamOnePoints + teamTwoPoints;
        return new GameOutcomePrediction(
            teamOneWins / (double) simulatedGames,
            teamTwoWins / (double) simulatedGames,
            teamOnePoints / (double) totalPoints,
            teamTwoPoints / (double) totalPoints);
    }
}
