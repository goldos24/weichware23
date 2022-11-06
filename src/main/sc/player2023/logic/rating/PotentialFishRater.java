package sc.player2023.logic.rating;

import org.jetbrains.annotations.NotNull;
import sc.api.plugins.ITeam;
import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.PossibleMoveStreamFactory;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.plugin2023.Move;

import java.util.stream.Stream;

public class PotentialFishRater implements Rater {

    public static Rating getPotentialFishForTeam(BoardPeek board, ITeam team) {
        Stream<Move> moveStream = PossibleMoveStreamFactory.getPossibleMoves(board, team);
        Stream<Rating> ratingStream = moveStream.map(move -> new Rating(board.get(move.getTo()).getFish()));
        return ratingStream.reduce(Rating::add).
                orElse(Rating.ZERO);
    }

    @Override
    public Rating rate(@NotNull ImmutableGameState gameState) {
        BoardPeek board = gameState.getBoard();
        ITeam team = gameState.getCurrentTeam();
        return getPotentialFishForTeam(board, team).subtract(getPotentialFishForTeam(board, team.opponent()));
    }
}