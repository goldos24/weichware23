package sc.player2023.logic.bns;

import sc.player2023.logic.rating.SearchWindow;

public interface BNSTestGuesser {

    int makeNextGuess(SearchWindow searchWindow, int subtreeCount);
}
