package sc.player2023.logic.move;

import sc.player2023.logic.gameState.ImmutableGameState;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.Iterator;

public class PossibleMoveIterable implements Iterable<Move> {
    @Nonnull ImmutableGameState gameState;

    public PossibleMoveIterable(@Nonnull ImmutableGameState gameState) {
        this.gameState = gameState;
    }

    @Nonnull
    @Override
    public Iterator<Move> iterator() {
        return PossibleMoveStreamFactory.getPossibleMoves(gameState.getBoard(), gameState.getCurrentTeam()).iterator();
    }
}
