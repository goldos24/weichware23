package sc.player2023.logic;

import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.Iterator;

public record PossibleMoveIterable(@Nonnull ImmutableGameState gameState) implements Iterable<Move> {

    @Nonnull
    @Override
    public Iterator<Move> iterator() {
        return PossibleMoveStreamFactory.getPossibleMoves(gameState.getBoard(), gameState.getCurrentTeam()).iterator();
    }
}
