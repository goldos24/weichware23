package sc.player2023.logic;

import kotlin.Pair;
import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.plugin2023.Board;
import sc.plugin2023.GameState;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static sc.player2023.logic.Rating.ZERO;

class DistanceRaterTest {
    private static final Coordinates TEST_COORDINATE_1 = new Coordinates(1, 1);
    private static final Coordinates TEST_COORDINATE_2 = new Coordinates(4, 2);

    @Test
    void getDistance() {
        int distance = DistanceRater.getDistance(TEST_COORDINATE_1, TEST_COORDINATE_2);
        assertEquals(4 /* adjust value accordingly if changing test values */, distance);
    }

    @Test
    void getCombinedDistancesToOtherPenguins() {
        Pair<Coordinates, Team> currentPenguin = new Pair<>(new Coordinates(4, 0), Team.ONE);
        ImmutableGameState testGameState = GameStateFixture.createTestGameState();
        int ownDistances = 2 + 4 + 6;
        GameState gameState = testGameState.gameState();
        Board board = gameState.getBoard();
        Collection<Pair<Coordinates, Team>> penguins =  board.getPenguins();
        assertEquals(ownDistances, DistanceRater.getCombinedDistancesToOtherPenguins(currentPenguin, penguins));
    }

    @Test
    void rate() {
        ImmutableGameState testGameState = GameStateFixture.createTestGameState();
        Rater rater = new DistanceRater();
        assertEquals(ZERO, rater.rate(testGameState));
    }
}
