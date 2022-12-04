package sc.player2023.logic.score;

import sc.api.plugins.ITeam;
import sc.api.plugins.Team;

import javax.annotation.Nonnull;

/**
 * @author Till Fransson
 * @since 04.12.2022
 */
public record PlayerScore(@Nonnull ITeam team, @Nonnull Score score) {

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
}
