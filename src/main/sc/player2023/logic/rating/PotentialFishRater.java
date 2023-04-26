package sc.player2023.logic.rating;

import org.jetbrains.annotations.NotNull;
import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.board.BoardPeek;
import sc.plugin2023.Field;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static sc.player2023.logic.move.PossibleMoveStreamFactory.getPossibleMovesInNormalCase;

public class PotentialFishRater implements Rater {
/*
    public static Rating getPotentialFishForTeam(BoardPeek board, ITeam team) {
        Stream<Move> moveStream = PossibleMoveStreamFactory.getPossibleMovesInNormalCase(board, team);
        Stream<Rating> ratingStream = moveStream.map(move -> new Rating(board.get(move.getTo()).getFish()));
        return ratingStream.reduce(Rating::add).
                orElse(Rating.ZERO);
    }
*/

    public static Rating getPotentialFishForTeam(@Nonnull ImmutableGameState gameState, @Nonnull ITeam team) {
        Rating rating = Rating.ZERO;
        BoardPeek board = gameState.getBoard();
        Stream<Move> possibleMovesStream = getPossibleMovesInNormalCase(board, team);
        List<Move> possibleMoves = possibleMovesStream.collect(Collectors.toList());
        for (Move possibleMove : possibleMoves) {
            Coordinates fieldPosition = possibleMove.getTo();
            Field field = board.get(fieldPosition);
            int fish = field.getFish();
            rating = rating.add(fish);
        }
        return rating;
    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        ITeam team = gameState.getCurrentTeam();
        return getPotentialFishForTeam(gameState, team).subtract(getPotentialFishForTeam(gameState, team.opponent()));
    }
}
