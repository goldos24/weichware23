package sc.player2023.logic.rating;

import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;

public class DirectionalDivergenceRater implements Rater {
    static Rating rateForTeam(@Nonnull ImmutableGameState gameState, ITeam team) {
        var penguins = gameState.getBoard().getPenguins().streamForTeam((Team) team);
        double[] angles = penguins.mapToDouble(penguin -> {
            Coordinates penguinCoords = penguin.coordinates();
            Coordinates centerDifference = penguinCoords.minus(GameRuleLogic.nonReachableBoardCenter).unaryPlus();
            return Math.atan2(centerDifference.getY(), centerDifference.getX()/2.0);
        }).toArray();
        if(angles.length == 0) {
            return Rating.ZERO;
        }
        double initialAngle = angles[0];
        boolean[] covered = new boolean[4];
        for(double currentAngle : angles) {
            double index = (2 + (currentAngle-initialAngle) / Math.PI) * 2;
            covered[(int) Math.round(index)%4] = true;
        }
        int coveredCount = 0;
        for (boolean isCurrentDirectionCovered : covered) {
            if(isCurrentDirectionCovered) {
                ++coveredCount;
            }
        }
        return new Rating(coveredCount);
    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        return RatingUtil.combineTeamRatings(gameState, DirectionalDivergenceRater::rateForTeam);
    }
}
