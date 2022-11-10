package sc.player2023.comparison;

import sc.player2023.logic.Logic;
import sc.player2023.logic.PureMCTSMoveGetter;
import sc.player2023.logic.rating.*;

public class MCTSComparisonProgram {
    public static void main(String[] args) {
        Rater rater = Logic.createCombinedRater();
        Fighter mctsFighter = new Fighter(new PureMCTSMoveGetter(), rater);
        double otherExplorationWeight = 3;
        Fighter otherMctsFighter = new Fighter(new PureMCTSMoveGetter(otherExplorationWeight), rater);

        BattleResult result = Battle.run(mctsFighter, otherMctsFighter, new BattleData(1));
        System.out.println(result);
    }
}
