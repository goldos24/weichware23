package sc.player2023.logic;

import kotlin.Pair;
import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class DistanceRaterTest {
    private static final Coordinates testCoordinate1 = new Coordinates(1, 1);
    private static final Coordinates testCoordinate2 = new Coordinates(4, 2);

    @Test
    void getDistance() {
        int distance = DistanceRater.getDistance(testCoordinate1, testCoordinate2);
        assertEquals(4 /* adjust value accordingly if changing test values */, distance);
    }

    @Test
    void getCombinedDistancesToOtherPenguins() {
        Pair<Coordinates, Team> currentPenguin = new Pair<>(new Coordinates(4, 0), Team.ONE);
        ImmutableGameState testGameState = GameStateFixture.createTestGameState();
        int ownDistances = 2 + 4 + 6;
        Collection<Pair<Coordinates, Team>> penguins =  testGameState.gameState().getBoard().getPenguins();
        assertEquals(ownDistances, DistanceRater.getCombinedDistancesToOtherPenguins(currentPenguin, penguins));
    }

    @Test
    void rate() {
        ImmutableGameState testGameState = GameStateFixture.createTestGameState();
        Rater rater = new DistanceRater();
        assertEquals(0, rater.rate(testGameState));
    }
}