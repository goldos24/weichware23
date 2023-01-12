package sc.player2023.logic.rating;

import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.api.plugins.Vector;
import sc.player2023.Direction;
import sc.player2023.logic.GameRuleLogic;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.plugin2023.Field;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static sc.player2023.Direction.*;

/**
 * @author Till Fransson
 * @since 12.01.2023
 */
public class PenguinOppositionRater implements Rater {

    @Nonnull
    private static final List<Coordinates> UNREACHABLE_COORDINATES = new ArrayList<>();


    @Nonnull
    private static final Map<Direction, List<Direction>> RIGHT_ANGLE_MAP = Map.of(LEFT,
                                                                                  List.of(BOTTOM_RIGHT, TOP_RIGHT),
                                                                                  TOP_LEFT, List.of(LEFT, RIGHT),
                                                                                  TOP_RIGHT, List.of(LEFT, RIGHT),
                                                                                  RIGHT, List.of(BOTTOM_LEFT, TOP_LEFT),
                                                                                  BOTTOM_RIGHT, List.of(LEFT, RIGHT),
                                                                                  BOTTOM_LEFT, List.of(LEFT, RIGHT));

    static void addReachableCoordsToSet(boolean[] set, @Nonnull Coordinates startCoords, @Nonnull BoardPeek board,
                                        Optional<Team> team) {
        int index = GameRuleLogic.coordsToIndex(startCoords);
        if (set[index]) {
            return;
        }
        set[index] = true;
        Stream<Direction> currentDirectionStream = GameRuleLogic
                .createCurrentDirectionStream();
        currentDirectionStream.forEach(direction -> {
            Vector directionVector = direction.getVector();
            Coordinates coordinates = startCoords.plus(directionVector);
            if (!GameRuleLogic.coordsValid(coordinates)) {
                return;
            }
            if (UNREACHABLE_COORDINATES.contains(coordinates)) {
                return;
            }
            Field field = board.get(coordinates);
            if (field.getFish() == 0) {
                if (team.isPresent()) {
                    if (field.getPenguin() == team.get().opponent()) {
                        List<Direction> rightAngle = RIGHT_ANGLE_MAP.get(direction);
                        for (Direction rightAngleDirection : rightAngle) {
                            Coordinates currentCoordinates = coordinates;
                            while (board.get(currentCoordinates).getFish() != 0) {
                                Vector rightAngleDirectionVector = rightAngleDirection.getVector();
                                currentCoordinates = currentCoordinates.plus(rightAngleDirectionVector);
                                UNREACHABLE_COORDINATES.add(currentCoordinates);
                            }
                        }
                    }
                    else {
                        return;
                    }
                }
                else {
                    return;
                }
            }
            addReachableCoordsToSet(set, coordinates, board, team);
        });
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

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        BoardPeek board = gameState.getBoard();
        ITeam ownTeam = gameState.getCurrentTeam();
        ITeam otherTeam = ownTeam.opponent();
        Map<ITeam, boolean[]> reachableCoords = Map.of(Team.ONE, new boolean[ARRAY_SIZE], Team.TWO,
                                                       new boolean[ARRAY_SIZE]);
        for (var penguin : board.getPenguins()) {
            Coordinates penguinPosition = penguin.coordinates();
            Team currentTeam = penguin.team();
            boolean[] ownSet = reachableCoords.get(currentTeam);
            Optional<Team> optionalTeam = Optional.of(currentTeam);
            addReachableCoordsToSet(ownSet, penguinPosition, board, optionalTeam);
        }
        Rating ownRating = new Rating(getReachableFishFromCoordSet(board, reachableCoords.get(ownTeam)));
        Rating opponentRating = new Rating(getReachableFishFromCoordSet(board, reachableCoords.get(otherTeam)));
        return ownRating.subtract(opponentRating);
    }
}
