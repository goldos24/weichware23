package sc.player2023.logic.rating;

import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.api.plugins.Team;
import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.board.BoardPeek;
import sc.plugin2023.Field;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static sc.player2023.logic.PossibleMoveStreamFactory.getPossibleMovesForPenguin;
import static sc.player2023.logic.PossibleMoveStreamFactory.getPossibleMovesInNormalCase;

public class PotentialFishRater implements Rater {
/*
    public static Rating getPotentialFishForTeam(BoardPeek board, ITeam team) {
        Stream<Move> moveStream = PossibleMoveStreamFactory.getPossibleMoves(board, team);
        Stream<Rating> ratingStream = moveStream.map(move -> new Rating(board.get(move.getTo()).getFish()));
        return ratingStream.reduce(Rating::add).
                orElse(Rating.ZERO);
    }
*/

    public static Rating getPotentialFishForTeam(@Nonnull ImmutableGameState gameState, @Nonnull ITeam team) {
        Rating rating = Rating.ZERO;
        BoardPeek board = gameState.getBoard();
        Stream<Move> possibleMovesStream = getPossibleMovesInNormalCase(board, team);

        Stream<Coordinates> positionsStream = possibleMovesStream.map(Move::getTo);
        Set<Coordinates> reachablePositions = positionsStream.collect(Collectors.toSet());

        for (Coordinates position : reachablePositions) {
            Field field = board.get(position);
            int fish = field.getFish();
            rating = rating.add(fish);
        }

        return rating;
    }

    @Override
    public Rating rate(@NotNull ImmutableGameState gameState) {
        ITeam team = gameState.getCurrentTeam();
        return getPotentialFishForTeam(gameState, team).subtract(getPotentialFishForTeam(gameState, team.opponent()));
    }
}
