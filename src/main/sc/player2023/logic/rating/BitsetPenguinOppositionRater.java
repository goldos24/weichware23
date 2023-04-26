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
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Till Fransson
 * @since 12.01.2023
 */
public class BitsetPenguinOppositionRater implements Rater {

    static long addReachableCoordsToSet(long set, Coordinates startCoords, BoardPeek board, List<Restriction> restrictions) {
        System.out.println("SBF:" + Long.toBinaryString(set));
        int index = GameRuleLogic.coordsToIndex(startCoords);
        if ((set & (1L << index)) != 0) {
            return set;
        }
        for (Restriction restriction : restrictions) {
            if(restriction.isInRestriction(startCoords))
                return set;
        }
        set |= 1L << index;
        System.out.println("set:" + Long.toBinaryString(set));
        System.out.println(startCoords);
        List<Coordinates> directlyReachableCoords = GameRuleLogic
                .createCurrentDirectionStream()
                .map(direction -> startCoords.plus(direction.getVector())).toList();
        System.out.println(directlyReachableCoords);
        for (Coordinates coordinates : directlyReachableCoords) {
            System.out.println(coordinates);
            if (!GameRuleLogic.coordsValid(coordinates)) {
                continue;
            }
            System.out.println(coordinates);
            if (board.get(coordinates).getFish() == 0) {
                continue;
            }
            System.out.println(coordinates);
            set |= addReachableCoordsToSet(set, coordinates, board, restrictions);
        }
        return set;
    }

    private static List<Restriction> findRestrictionsForPenguin(Coordinates penguin, List<Coordinates> EnemyPenguins) {
        List<Restriction> restrictions = new ArrayList<>();
        int x = penguin.getX();
        int y = penguin.getY();
        for (Coordinates enemyPenguin : EnemyPenguins) {
            int enemyX = enemyPenguin.getX();
            int enemyY = enemyPenguin.getY();
            Vector vector = new Vector(enemyX - x, enemyY - y);
            Optional<Restriction> optionalRestriction = findRestrictionForEnemyPenguin(enemyPenguin, vector);
            optionalRestriction.ifPresent(restrictions::add);
        }
        return restrictions;
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

    private static int getReachableFishFromCoordSet(BoardPeek board, long coords) {
        int result = 0;
        for (int i = 0; i < 64; ++i) {
            if ((coords & (1L << i)) != 0) {
                result += board.get(GameRuleLogic.indexToCoords(i)).getFish();
            }
        }
        return result;
    }

    private static final int ARRAY_SIZE = GameRuleLogic.BOARD_HEIGHT * GameRuleLogic.BOARD_WIDTH;

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        BoardPeek board = gameState.getBoard();
        ITeam ownTeam = gameState.getCurrentTeam();
        ITeam otherTeam = ownTeam.opponent();
        Map<ITeam, Long> reachableCoords = new HashMap<>(Map.of(Team.ONE, 0L, Team.TWO, 0L));
        for (var penguin : board.getPenguins()) {
            Coordinates penguinPosition = penguin.coordinates();
            var team = penguin.team();
            long ownSet = reachableCoords.get(team);
            List<Restriction> restrictions = findRestrictionsForPenguin(penguinPosition, board.getPenguins()
                    .streamForTeam(team.opponent()).map(Penguin::coordinates).toList());
            reachableCoords.replace(team, addReachableCoordsToSet(ownSet, penguinPosition, board, restrictions));
        }
        System.out.println(Long.toBinaryString(reachableCoords.get(Team.ONE)));
        System.out.println(Long.toBinaryString(reachableCoords.get(Team.TWO)));
        Rating ownRating = new Rating(getReachableFishFromCoordSet(board, reachableCoords.get(ownTeam)));
        Rating opponentRating = new Rating(getReachableFishFromCoordSet(board, reachableCoords.get(otherTeam)));
        return ownRating.subtract(opponentRating);
    }
}
