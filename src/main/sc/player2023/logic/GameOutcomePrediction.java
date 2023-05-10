package sc.player2023.logic;

import sc.api.plugins.ITeam;
import sc.api.plugins.Team;

public record GameOutcomePrediction(double teamOneWinLikelihood, double teamTwoWinLikelihood,
                                    double teamOneMinPointRatio, double teamOneMaxPointRatio,
                                    double teamTwoMinPointRatio, double teamTwoMaxPointRatio) {
    public ITeam getLikelyWinner() {
        if (teamOneWinLikelihood > teamTwoWinLikelihood) {
            return Team.ONE;
        } else if (teamTwoWinLikelihood > teamOneWinLikelihood) {
            return Team.TWO;
        } else {
            return null;
        }
    }
}
