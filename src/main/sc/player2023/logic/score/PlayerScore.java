package sc.player2023.logic.score;

import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.player2023.logic.score.Score;

import javax.annotation.Nonnull;

/**
 * @author Till Fransson
 * @since 04.12.2022
 */
public final class PlayerScore {
    public PlayerScore(
            @Nonnull
            ITeam team,
            @Nonnull
            Score score) { this.team = team; this.score = score;
    }
    @Nonnull
    ITeam team;
    @Nonnull
    Score score;

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj != null && obj.getClass() == this.getClass();
    }

    @Override
    public int hashCode() {
        return 1;
    }

    public static final PlayerScore SCORE_ZERO_TEAM_ONE = new PlayerScore(Team.ONE, Score.ZERO);
    public static final PlayerScore SCORE_ZERO_TEAM_TWO = new PlayerScore(Team.TWO, Score.ZERO);

    @Nonnull
    public PlayerScore add(int otherScore) {
        return new PlayerScore(team, score.add(otherScore));
    }

    @Nonnull
    public PlayerScore add(@Nonnull Score otherScore) {
        return add(otherScore.score());
    }

    @Override
    public String toString() {
        return "PlayerScore {" + "team: " + team + ", " + score + "}";
    }

    public ITeam team() {
        return team;
    }

    public Score score() {
        return score;
    }
}
