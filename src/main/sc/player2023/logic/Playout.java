package sc.player2023.logic;

import javax.annotation.Nonnull;

public class Playout {
	@Nonnull ImmutableGameState initialGameState;

	public Playout(@Nonnull ImmutableGameState initialGameState) {
		this.initialGameState = initialGameState;
	}

	public ImmutableGameState complete() {
		ImmutableGameState currentGameState = this.initialGameState;
		while (!currentGameState.isOver()) {
			currentGameState = GameRuleLogic.withRandomMovePerformed(currentGameState);
		}
		return currentGameState;
	}
}
