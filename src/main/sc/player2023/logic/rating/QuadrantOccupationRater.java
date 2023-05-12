package sc.player2023.logic.rating;

import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.Penguin;
import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;
import java.util.stream.Stream;

public class QuadrantOccupationRater implements Rater {

    static int getQuadrantIndex(Coordinates coordinates) {
        int result = 0;
        if(coordinates.getY() >= GameRuleLogic.BOARD_HEIGHT/2) {
            result += 2;
        }
        if(coordinates.getX() >= GameRuleLogic.RIGHTMOST_X/2) {
            result++;
        }
        return result;
    }

    private static final int QUADRANT_COUNT = 4; // in a parallel universe it might be 5

    @Nonnull
    static Rating getRatingForTeam(@Nonnull ImmutableGameState gameState, @Nonnull ITeam team) {
        final boolean[] visitedQuadrants = new boolean[QUADRANT_COUNT];
        Stream<Penguin> penguinStream = gameState.getBoard()
                                                 .getPenguins().streamForTeam((Team) team);
        penguinStream.forEach(coordinatesTeamPair -> {
            int index = getQuadrantIndex(coordinatesTeamPair.coordinates());
            visitedQuadrants[index] = true;
        });
        int result = 0;
        for(boolean visitedQuadrant : visitedQuadrants) {
            if(visitedQuadrant)
                ++result;
        }
        return new Rating(result);
    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        ITeam ownTeam = gameState.getCurrentTeam();
        ITeam opponentTeam = ownTeam.opponent();
        return getRatingForTeam(gameState, ownTeam).subtract(getRatingForTeam(gameState, opponentTeam));
    }
}
