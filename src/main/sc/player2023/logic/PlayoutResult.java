package sc.player2023.logic;

import com.google.common.collect.ImmutableMap;
import sc.api.plugins.ITeam;

import javax.annotation.Nonnull;

public class PlayoutResult {
    private enum Kind {
        WIN,
        DRAW,
        LOSS
    }

    @Nonnull
    ImmutableMap<ITeam, Integer> points;
    Kind kind;

    private PlayoutResult(Kind kind, @Nonnull ImmutableMap<ITeam, Integer> points) {
        this.kind = kind;
        this.points = points;
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

        if (teamPoints > opponentPoints) {
            return new PlayoutResult(Kind.WIN, points);
        }
        if (teamPoints < opponentPoints) {
            return new PlayoutResult(Kind.LOSS, points);
        }
        return new PlayoutResult(Kind.DRAW, points);
    }

    @Nonnull
    public static PlayoutResult ofCurrentTeam(@Nonnull ImmutableGameState gameState) {
        return of(gameState, gameState.getCurrentTeam());
    }

    @Nonnull
    public static PlayoutResult ofOpponentTeam(@Nonnull ImmutableGameState gameState) {
        return of(gameState, gameState.getCurrentTeam().opponent());
    }
}
