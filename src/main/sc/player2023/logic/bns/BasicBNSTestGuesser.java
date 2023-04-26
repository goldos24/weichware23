package sc.player2023.logic.bns;

import sc.player2023.logic.rating.SearchWindow;

public class BasicBNSTestGuesser implements BNSTestGuesser {
    @Override
    public int makeNextGuess(SearchWindow searchWindow, int subtreeCount) {
        int alpha = searchWindow.lowerBound();
        int beta = searchWindow.upperBound();
        return alpha + (beta - alpha) * (subtreeCount - 1) / subtreeCount;
    }
}
