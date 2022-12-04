package sc.player2023.logic.rating;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Team;
import sc.player2023.logic.GameStateFixture;
import sc.player2023.logic.gameState.ImmutableGameState;

import static org.junit.jupiter.api.Assertions.*;

class RatingUtilTest {

    @Test
    void combineTeamRatings() {
        ImmutableGameState testGameState = GameStateFixture.createTestGameState();
        TeamRater teamRater = (gameState, team) -> team == Team.ONE ? Rating.ONE : Rating.TWO;
        Rating expected = Rating.ONE.negate();
        Rating got = RatingUtil.combineTeamRatings(testGameState, teamRater);
        assertEquals(expected, got);
    }
}