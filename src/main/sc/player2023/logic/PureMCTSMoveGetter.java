package sc.player2023.logic;

import sc.player2023.logic.rating.Rater;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;

public class PureMCTSMoveGetter implements MoveGetter {
    private final TimeMeasurer timer = new TimeMeasurer(1900);

	// TODO: find a more suitable exploration weight for this game
	public static final double EXPLORATION_WEIGHT = Math.sqrt(2);

    @Override
    public Move getBestMove(@Nonnull ImmutableGameState gameState, @Nonnull Rater rater, TimeMeasurer timeMeasurer) {
        timer.reset();

		ImmutableMCTSTree tree = new ImmutableMCTSTree(gameState);
		while (!timer.ranOutOfTime()) {

		}

		return tree.bestMove();
    }
}
