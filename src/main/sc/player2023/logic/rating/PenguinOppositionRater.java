package sc.player2023.logic.rating;

import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.api.plugins.Vector;
import sc.player2023.Direction;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.Penguin;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.opposition.Restriction;
import sc.player2023.logic.opposition.VectorMath;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Till Fransson
 * @since 12.01.2023
 */
public class PenguinOppositionRater implements Rater {

    static void addReachableCoordsToSet(boolean[] set, Coordinates startCoords, BoardPeek board, List<Restriction> restrictions) {
        int index = GameRuleLogic.coordsToIndex(startCoords);
        if (set[index]) {
            return;
        }
        for (Restriction restriction : restrictions) {
            if(restriction.isInRestriction(startCoords))
                return;
        }
        set[index] = true;
        Stream<Coordinates> directlyReachableCoords = GameRuleLogic
                .createCurrentDirectionStream().map(startCoords::plus);
        directlyReachableCoords.forEach(coordinates -> {
            if (!GameRuleLogic.coordsValid(coordinates)) {
                return;
            }
            if (board.get(coordinates).getFish() == 0) {
                return;
            }
            addReachableCoordsToSet(set, coordinates, board, restrictions);
        });
    }

    private static void findRestrictionsForPenguin(Coordinates penguin, List<Coordinates> EnemyPenguins) {
        int x = penguin.getX();
        int y = penguin.getY();
        for (Coordinates enemyPenguin : EnemyPenguins) {
            int enemyX = enemyPenguin.getX();
            int enemyY = enemyPenguin.getY();
            Vector vector = new Vector(enemyX - x, enemyY - y);
            Optional<Restriction> optionalRestriction = findRestrictionForEnemyPenguin(enemyPenguin, vector);
            optionalRestriction.ifPresent(restrictions::add);
        }
    }

    private static Optional<Restriction> findRestrictionForEnemyPenguin(@Nonnull Coordinates enemyPenguin, @Nonnull Vector vector) {
        Direction[] directions = Direction.values();
        for (Direction direction : directions) {
            if (VectorMath.angleBetweenVectors(vector.times(-1), direction.getVector()) == 0.0) {
                Restriction restriction = new Restriction(enemyPenguin, direction);
                return Optional.of(restriction);
            }
        }
        return Optional.empty();
    }

    private static int getReachableFishFromCoordSet(BoardPeek board, boolean[] coords) {
        int result = 0;
        for (int i = 0; i < coords.length; ++i) {
            if (coords[i]) {
                result += board.get(GameRuleLogic.indexToCoords(i)).getFish();
            }
        }
        return result;
    }

    private static final int ARRAY_SIZE = GameRuleLogic.BOARD_HEIGHT * GameRuleLogic.BOARD_WIDTH;

    private static final ArrayList<Restriction> restrictions = new ArrayList<>(32);

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        BoardPeek board = gameState.getBoard();
        ITeam ownTeam = gameState.getCurrentTeam();
        ITeam otherTeam = ownTeam.opponent();
        Map<ITeam, boolean[]> reachableCoords = Map.of(Team.ONE, new boolean[ARRAY_SIZE], Team.TWO,
                new boolean[ARRAY_SIZE]);
        for (var penguin : board.getPenguins()) {
            Coordinates penguinPosition = penguin.coordinates();
            var team = penguin.team();
            boolean[] ownSet = reachableCoords.get(team);
            findRestrictionsForPenguin(penguinPosition, board.getPenguins()
                    .streamForTeam(team.opponent()).map(Penguin::coordinates).collect(Collectors.toList()));
            addReachableCoordsToSet(ownSet, penguinPosition, board, restrictions);
            restrictions.clear();
        }
        Rating ownRating = new Rating(getReachableFishFromCoordSet(board, reachableCoords.get(ownTeam)));
        Rating opponentRating = new Rating(getReachableFishFromCoordSet(board, reachableCoords.get(otherTeam)));
        return ownRating.subtract(opponentRating);
    }
}
