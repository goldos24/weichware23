package sc.player2023.logic.mcts;

import com.google.common.collect.ImmutableMap;
import sc.api.plugins.ITeam;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.score.Score;

import javax.annotation.Nonnull;

public class PlayoutResult {
    public enum Kind {
        NONE,
        WIN,
        DRAW,
        LOSS
    }

    @Nonnull
    private final ImmutableMap<ITeam, Score> scores;
    private final ITeam affectedTeam;
    private final Kind kind;

    private PlayoutResult(Kind kind, ITeam affectedTeam, @Nonnull ImmutableMap<ITeam, Score> scores) {
        this.kind = kind;
        this.affectedTeam = affectedTeam;
        this.scores = scores;
    }

    @Nonnull
    public ImmutableMap<ITeam, Score> getScores() {
        return scores;
    }

    public ITeam getAffectedTeam() {
        return affectedTeam;
    }

    public Kind getKind() {
        return kind;
    }

    @Nonnull
    private static ImmutableMap<ITeam, Score> makeScoreMap(ITeam team, Score teamScore, Score opponentScore) {
        ImmutableMap.Builder<ITeam, Score> pointsBuilder = ImmutableMap.builder();
        pointsBuilder.put(team, teamScore);
        ITeam opponent = team.opponent();
        pointsBuilder.put(opponent, opponentScore);

        return pointsBuilder.build();
    }

    @Nonnull
    public static PlayoutResult of(ImmutableGameState gameState, ITeam team) {
        Score teamPoints = gameState.getScoreForTeam(team);
        ITeam opponent = team.opponent();
        Score opponentScore = gameState.getScoreForTeam(opponent);

        ImmutableMap<ITeam, Score> points = makeScoreMap(team, teamPoints, opponentScore);

        if (!gameState.isOver()) {
            return new PlayoutResult(Kind.NONE, team, points);
        }

        if (teamPoints.isGreaterThan(opponentScore)) {
            return new PlayoutResult(Kind.WIN, team, points);
        }
        if (teamPoints.isLessThan(opponentScore)) {
            return new PlayoutResult(Kind.LOSS, team, points);
        }
        return new PlayoutResult(Kind.DRAW, team, points);
    }

    @Nonnull
    public static PlayoutResult ofCurrentTeam(@Nonnull ImmutableGameState gameState) {
        return of(gameState, gameState.getCurrentTeam());
    }

    @Nonnull
    public static PlayoutResult ofOpponentTeam(@Nonnull ImmutableGameState gameState) {
        return of(gameState, gameState.getCurrentTeam().opponent());
    }

    @Override
    public String toString() {
        return "PlayoutResult{" +
                "scores=" + scores +
                ", affectedTeam=" + affectedTeam +
                ", kind=" + kind +
                '}';
    }
}
