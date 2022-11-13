package sc.player2023.logic.rating;

import sc.api.plugins.Coordinates;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;

public class EdgePenguinPenalty implements Rater {

    private static Rating ratePenguin(Coordinates coordinates) {
        int halfX = coordinates.getX()/2;
        int y = coordinates.getY();
        if(halfX == 0 || halfX == GameRuleLogic.RIGHTMOST_X/2 || y == 0 || y == GameRuleLogic.BOARD_HEIGHT-1)
            return Rating.ONE;
        return Rating.ZERO;
    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        var board = gameState.getBoard();
        var penguins = board.getPenguins();
        var ownTeam = gameState.getCurrentTeam();
        Rating own = penguins.stream().filter(coordinatesTeamPair -> coordinatesTeamPair.getSecond() == ownTeam)
                .map(coordinatesTeamPair -> ratePenguin(coordinatesTeamPair.getFirst()))
                .reduce(Rating::add).orElse(Rating.ZERO);
        var otherTeam = ownTeam.opponent();
        Rating other = penguins.stream().filter(coordinatesTeamPair -> coordinatesTeamPair.getSecond() == otherTeam)
                .map(coordinatesTeamPair -> ratePenguin(coordinatesTeamPair.getFirst()))
                .reduce(Rating::add).orElse(Rating.ZERO);
        return other.subtract(own);
    }
}
