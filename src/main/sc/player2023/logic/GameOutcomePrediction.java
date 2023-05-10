package sc.player2023.logic;

import sc.api.plugins.ITeam;
import sc.api.plugins.Team;

public record GameOutcomePrediction(double teamOneWinLikelihood, double teamTwoWinLikelihood,
                                    double teamOneAveragePointRatio,
                                    double teamTwoAveragePointRatio) {
    public ITeam getLikelyWinner() {
        if (teamOneWinLikelihood > teamTwoWinLikelihood && teamOneAveragePointRatio > teamTwoAveragePointRatio) {
            return Team.ONE;
        } else if (teamTwoWinLikelihood > teamOneWinLikelihood && teamTwoAveragePointRatio > teamOneAveragePointRatio) {
            return Team.TWO;
        } else {
            return null;
        }
    }
}
