package sc.player2023.logic.score;

import sc.api.plugins.ITeam;
import sc.api.plugins.Team;

import javax.annotation.Nonnull;

import java.util.Objects;

import static sc.player2023.logic.score.PlayerScore.SCORE_ZERO_TEAM_ONE;
import static sc.player2023.logic.score.PlayerScore.SCORE_ZERO_TEAM_TWO;

/**
 * @author Till Fransson
 * @since 04.12.2022
 */
public final class GameScore {

    public GameScore(@Nonnull Score greenScore, @Nonnull Score purpleScore) {
        this(new PlayerScore(Team.ONE, greenScore), new PlayerScore(Team.TWO, purpleScore));
    }

    public GameScore(int greenScore, int purpleScore) {
        this(new Score(greenScore), new Score(purpleScore));
    }

    public static GameScore START_SCORE = new GameScore(SCORE_ZERO_TEAM_ONE, SCORE_ZERO_TEAM_TWO);
    @Nonnull
    private final PlayerScore playerScore;
    @Nonnull
    private final PlayerScore otherPlayerScore;

    /**
     *
     */
    public GameScore(@Nonnull PlayerScore playerScore, @Nonnull PlayerScore otherPlayerScore) {
        assert playerScore.team() != otherPlayerScore.team();
        this.playerScore = playerScore;
        this.otherPlayerScore = otherPlayerScore;
    }

    @Nonnull
    public Score getScoreFromTeam(@Nonnull ITeam team) {
        return playerScore.team() == team ? playerScore.score() : otherPlayerScore.score();
    }

    @Nonnull
    public PlayerScore getPlayerScoreFromTeam(@Nonnull ITeam team) {
        if (playerScore.team() == team) {
            return playerScore;
        }
        else if (otherPlayerScore.team() == team) {
            return otherPlayerScore;
        }
        throw new IllegalArgumentException("team has to be equal to one of the playerScores, but was: " + team);
    }

    @Nonnull
    public GameScore withPlayerScore(@Nonnull PlayerScore newPlayerScore) {
        if (playerScore.team() == newPlayerScore.team()) {
            return new GameScore(newPlayerScore, otherPlayerScore);
        }
        else {
            return new GameScore(playerScore, newPlayerScore);
        }
    }

    @Override
    public String toString() {
        return "GameScore {" + playerScore + ", " + otherPlayerScore + "}";
    }

    @Nonnull
    public PlayerScore playerScore() {
        return playerScore;
    }

    @Nonnull
    public PlayerScore otherPlayerScore() {
        return otherPlayerScore;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (GameScore) obj;
        return Objects.equals(this.playerScore, that.playerScore) &&
                Objects.equals(this.otherPlayerScore, that.otherPlayerScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerScore, otherPlayerScore);
    }

}
