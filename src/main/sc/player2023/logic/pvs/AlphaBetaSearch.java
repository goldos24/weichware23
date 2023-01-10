package sc.player2023.logic.pvs;

import sc.player2023.logic.gameState.ImmutableGameState;
import sc.player2023.logic.rating.RatedMove;
import sc.player2023.logic.rating.SearchWindow;

import javax.annotation.Nonnull;

public interface AlphaBetaSearch {
    RatedMove search(@Nonnull ImmutableGameState gameState, int depth, SearchWindow searchWindow,
                  @Nonnull ConstantPVSParameters constParams);
}
