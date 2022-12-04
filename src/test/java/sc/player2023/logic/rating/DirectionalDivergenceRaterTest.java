package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Team;

import static org.junit.jupiter.api.Assertions.*;
import static sc.player2023.logic.rating.QuadrantOccupationRaterTest.gameState;

class DirectionalDivergenceRaterTest {
    @Test
    void rateForTeam() {
        assertEquals(new Rating(3), DirectionalDivergenceRater.rateForTeam(gameState,Team.ONE));
        assertEquals(Rating.ONE, DirectionalDivergenceRater.rateForTeam(gameState,Team.TWO));
    }

    @Test
    void rate() {
        Rater rater = new DirectionalDivergenceRater();
        assertEquals(Rating.TWO, rater.rate(gameState));
    }
}