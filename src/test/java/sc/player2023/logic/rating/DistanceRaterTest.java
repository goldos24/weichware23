package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Coordinates;
import sc.api.plugins.Team;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.Penguin;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.gameState.ImmutableGameState;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static sc.player2023.logic.rating.Rating.ZERO;

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
        Penguin currentPenguin = new Penguin(new Coordinates(4, 0), Team.ONE);
        ImmutableGameState testGameState = GameStateFixture.createTestGameState();
        int ownDistances = 2 + 4 + 6;
        BoardPeek board = testGameState.getBoard();
        Collection<Penguin> penguins =  board.getPenguins();
        assertEquals(ownDistances, DistanceRater.getCombinedDistancesToOtherPenguins(currentPenguin, penguins));
    }

    @Test
    void rate() {
        ImmutableGameState testGameState = GameStateFixture.createTestGameState();
        Rater rater = new DistanceRater();
        assertEquals(ZERO, rater.rate(testGameState));
    }
}
