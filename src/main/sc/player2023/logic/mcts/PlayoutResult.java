package sc.player2023.logic.mcts;

import com.google.common.collect.ImmutableMap;
import sc.api.plugins.ITeam;
import sc.player2023.logic.gameState.ImmutableGameState;

import javax.annotation.Nonnull;

public class PlayoutResult {
    public enum Kind {
        NONE,
        WIN,
        DRAW,
        LOSS
    }

    @Nonnull
    private final ImmutableMap<ITeam, Integer> points;
    private final ITeam affectedTeam;
    private final Kind kind;

    private PlayoutResult(Kind kind, ITeam affectedTeam, @Nonnull ImmutableMap<ITeam, Integer> points) {
        this.kind = kind;
        this.affectedTeam = affectedTeam;
        this.points = points;
    }

    @Nonnull
    public ImmutableMap<ITeam, Integer> getPoints() {
        return points;
    }

    public ITeam getAffectedTeam() {
        return affectedTeam;
    }

    public Kind getKind() {
        return kind;
    }

    @Nonnull
    private static ImmutableMap<ITeam, Integer> makePointsMap(ITeam team, int teamPoints, int opponentPoints) {
        ImmutableMap.Builder<ITeam, Integer> pointsBuilder = ImmutableMap.builder();
        pointsBuilder.put(team, teamPoints);
        ITeam opponent = team.opponent();
        pointsBuilder.put(opponent, opponentPoints);

        return pointsBuilder.build();
    }

    @Nonnull
    public static PlayoutResult of(ImmutableGameState gameState, ITeam team) {
        int teamPoints = gameState.getPointsForTeam(team);
        ITeam opponent = team.opponent();
        int opponentPoints = gameState.getPointsForTeam(opponent);

        ImmutableMap<ITeam, Integer> points = makePointsMap(team, teamPoints, opponentPoints);

        if (!gameState.isOver()) {
            return new PlayoutResult(Kind.NONE, team, points);
        }

        if (teamPoints > opponentPoints) {
            return new PlayoutResult(Kind.WIN, team, points);
        }
        if (teamPoints < opponentPoints) {
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
            "points=" + points +
            ", affectedTeam=" + affectedTeam +
            ", kind=" + kind +
            '}';
    }
}
