package sc.player2023.logic;

import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.rating.Rater;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;

public record EmptyFieldCountBasedHybridMoveGetter(
        @Nonnull MoveGetter earlyGameMoveGetter,
        @Nonnull MoveGetter midGameMoveGetter,
        @Nonnull MoveGetter lateGameMoveGetter,
        int midGameStart,
        int lateGameStart
) implements MoveGetter{
    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        int emptyFieldCount = GameRuleLogic.getEmptyFieldCountFromBoard(gameState.getBoard());
        if (emptyFieldCount < midGameStart) {
            return earlyGameMoveGetter.getBestMove(gameState, rater, timeMeasurer);
        }
        if (emptyFieldCount < lateGameStart) {
            return midGameMoveGetter.getBestMove(gameState, rater, timeMeasurer);
        }
        return lateGameMoveGetter.getBestMove(gameState, rater, timeMeasurer);
    }
}
