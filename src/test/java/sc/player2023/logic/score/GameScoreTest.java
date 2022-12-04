package sc.player2023.logic.score;

import org.junit.jupiter.api.Test;
import sc.api.plugins.Team;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Till Fransson
 * @since 04.12.2022
 */
public class GameScoreTest {

    private final Score scoreOne = new Score(14);

    private final PlayerScore playerScoreOne = new PlayerScore(Team.ONE, scoreOne);

    private final Score scoreTwo = new Score(63);

    private final PlayerScore playerScoreTwo = new PlayerScore(Team.TWO, scoreTwo);


    private final GameScore gameScore = new GameScore(scoreOne, scoreTwo);

    @Test
    void getScoreFromTeamOne() {
        PlayerScore actual = gameScore.getPlayerScoreFromTeam(Team.ONE);
        assertEquals(playerScoreOne, actual, "getPlayerScoreFromTeamOne");
    }

    @Test
    void getPlayerScoreFromTeam() {
        PlayerScore actual = gameScore.getPlayerScoreFromTeam(Team.TWO);
        assertEquals(playerScoreTwo, actual, "getPlayerScoreFromTeamTwo");

    }

    @Test
    void withPlayerScore() {
        PlayerScore newPlayerScore = new PlayerScore(Team.TWO, scoreOne);
        GameScore expected = new GameScore(playerScoreOne, newPlayerScore);
        GameScore actual = gameScore.withPlayerScore(newPlayerScore);
        assertEquals(expected, actual, "withPlayerScore");
    }

    @Test
    void testToString() {
        GameScore expectedGameScore = new GameScore(playerScoreOne, playerScoreTwo);
        String expected = expectedGameScore.toString();
        String actual = gameScore.toString();
        assertEquals(expected, actual, "toString");
    }

    @Test
    void playerScore() {
        assertEquals(playerScoreOne, gameScore.playerScore(), "playerScore()");
    }

    @Test
    void otherPlayerScore() {
        assertEquals(playerScoreTwo, gameScore.otherPlayerScore(), "otherPlayerScore()");
    }
}