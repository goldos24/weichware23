package sc.player2023.logic.enemy;

import org.jetbrains.annotations.NotNull;
import sc.api.plugins.Coordinates;
import sc.api.plugins.ITeam;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.move.PossibleMoveStreamFactory;
import sc.player2023.logic.board.BoardPeek;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.rating.Rating;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

public class MaxRating implements Rater {

    @Nonnull
    public Rating rateGameStateForTeam(@Nonnull ImmutableGameState gameState, @Nonnull ITeam team) {
        Rating rating = Rating.ZERO;
        Rating fish = new Rating(gameState.getPointsForTeam(team) - gameState.getPointsForTeam(team.opponent()));
        Rating potentialFish = this.ratePotentialFishForTeam(gameState, team);
        return rating.add(fish).add(potentialFish);
    }

    @Nonnull
    private Rating ratePotentialFishForTeam(@Nonnull ImmutableGameState gameState, @Nonnull ITeam team) {
        Rating rating = Rating.ZERO;
        List<Move> ourPotentialMoves = PossibleMoveStreamFactory.getPossibleMovesInNormalCase(gameState.getBoard(), team).toList();
        int ourFish = this.getFishFromMoves(gameState, ourPotentialMoves);
        List<Move> theirPotentialMoves = PossibleMoveStreamFactory.getPossibleMovesInNormalCase(gameState.getBoard(), team.opponent()).toList();
        int theirFish = this.getFishFromMoves(gameState, theirPotentialMoves);
        rating = rating.add(new Rating(ourFish - theirFish));
        return rating.multiply(.1);
    }

    private int getFishFromMoves(@Nonnull ImmutableGameState gameState, @Nonnull List<Move> moves) {
        int fish = 0;
        BoardPeek board = gameState.getBoard();

        Coordinates coordinates;
        for(Iterator<Move> var5 = moves.iterator(); var5.hasNext(); fish += board.get(coordinates).getFish()) {
            Move move = var5.next();
            coordinates = move.getTo();
        }

        return fish;
    }

    @Override
    public Rating rate(@Nonnull ImmutableGameState gameState) {
        return rateGameStateForTeam(gameState, gameState.getCurrentTeam());
    }
}
