package sc.player2023.comparison;

import sc.player2023.logic.PVSMoveGetter;
import sc.player2023.logic.PureMCTSMoveGetter;
import sc.player2023.logic.Rater;
import sc.player2023.logic.StupidRater;

public class ComparisonProgram {
    public static void main(String[] args) {
        Rater rater = new StupidRater();
        Fighter mctsFighter = new Fighter(new PureMCTSMoveGetter(), rater);
        Fighter pvsFighter = new Fighter(new PVSMoveGetter(), rater);
        BattleResult result = Battle.run(mctsFighter, pvsFighter, new BattleData(16));
        System.out.println(result);
    }
}
