package sc.player2023.logic;

import sc.api.plugins.ITeam;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;

public class PureMCTSMoveGetter implements MoveGetter {
    private int getRandomMoveIndex(List<Move> possibleMoves) {
        return (int) Math.round(Math.random() * (possibleMoves.size() - 1));
    }

    private Move findHighestRatedMove(Rating[] ratings, List<Move> possibleMoves) {
        Rating currentBest = Rating.NEGATIVE_INFINITY;
        Move currentBestMove = null;
        if (ratings.length != possibleMoves.size()) {
            throw new IllegalStateException("winCounts and possibleMoves have different sizes");
        }
        for (int i = 0; i < ratings.length; ++i) {
            if (ratings[i].isGreaterThan(currentBest)) {
                currentBest = ratings[i];
                currentBestMove = possibleMoves.get(i);
            }
        }
        return currentBestMove;
    }

    @Nonnull
    private GameStateWithContinuation playRandomMove(ImmutableGameState gameState) {
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        if (possibleMoves.size() == 0 || gameState.isOver() || timer.ranOutOfTime()) {
            return GameStateWithContinuation.stopAlgorithm(gameState);
        }
        int index = getRandomMoveIndex(possibleMoves);
        ImmutableGameState newGameState = GameRuleLogic.withMovePerformed(gameState, possibleMoves.get(index));
        return GameStateWithContinuation.continueAlgorithm(newGameState);
    }

    private final TimeMeasurer timer = new TimeMeasurer(1900);
    public static final int MAXIMAL_DEPTH = 5;

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull ITeam team, @Nonnull Rater rater) {
        timer.reset();
        List<Move> possibleMoves = GameRuleLogic.getPossibleMoves(gameState);
        double[] winCounts = new double[possibleMoves.size()];
        double[] playthroughs = new double[possibleMoves.size()];
        while (!timer.ranOutOfTime()) {
            ImmutableGameState clonedGameState = gameState;
            int initialMoveIndex = getRandomMoveIndex(possibleMoves);
            clonedGameState = GameRuleLogic.withMovePerformed(clonedGameState, possibleMoves.get(initialMoveIndex));

            /*for (int move_number = 0; move_number < MAXIMAL_DEPTH; ++move_number) {
                playRandomMove(clonedGameState);
            }*/
            while (!clonedGameState.isOver()) {
                GameStateWithContinuation gameStateWithContinuation = playRandomMove(clonedGameState);
                if (gameStateWithContinuation.shouldStopAlgorithm()) {
                    break;
                }
                clonedGameState = gameStateWithContinuation.getGameState();
            }
            if (GameRuleLogic.isTeamWinner(clonedGameState, team)) {
                winCounts[initialMoveIndex]++;
            }
            else if (GameRuleLogic.isTeamWinner(clonedGameState, team.opponent())) {
                winCounts[initialMoveIndex]--;
            }
            playthroughs[initialMoveIndex]++;
        }

        Rating[] ratings = new Rating[possibleMoves.size()];
        for (int i = 0; i < ratings.length; ++i) {
            ratings[i] = new Rating(winCounts[i] / playthroughs[i]);
        }

        return findHighestRatedMove(ratings, possibleMoves);
    }
}
