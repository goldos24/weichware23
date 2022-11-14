package sc.player2023.logic.rating;

import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;

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

    @NotNull
    static Rating getRatingForTeam(@NotNull ImmutableGameState gameState, @NotNull ITeam team) {
        final boolean[] visitedQuadrants = new boolean[QUADRANT_COUNT];
        Stream<Pair<Coordinates, Team>> penguinStream = gameState.getBoard()
                .getPenguins().stream();
        var ownPenguinStream = penguinStream
                .filter(coordinatesTeamPair -> coordinatesTeamPair.getSecond() == team);
        ownPenguinStream.forEach(coordinatesTeamPair -> {
            int index = getQuadrantIndex(coordinatesTeamPair.getFirst());
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
    public Rating rate(@NotNull ImmutableGameState gameState) {
        ITeam ownTeam = gameState.getCurrentTeam();
        ITeam opponentTeam = ownTeam.opponent();
        return getRatingForTeam(gameState, ownTeam).subtract(getRatingForTeam(gameState, opponentTeam));
    }
}
