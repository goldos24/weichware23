package sc.player2023.comparison;

import sc.player2023.logic.PVSMoveGetter;
import sc.player2023.logic.PureMCTSMoveGetter;
import sc.player2023.logic.rating.*;

public class MCTSComparisonProgram {
    public static void main(String[] args) {
        Rater rater = new CompositeRater(new Rater[]{new WeightedRater(5, new StupidRater()), new PotentialFishRater()});
        Rater enhancedRater = new CompositeRater(new Rater[] {rater, new WeightedRater(20, new PenguinCutOffRater())});
        Rater hopefullyBetterRater = new CompositeRater(new Rater[] {enhancedRater, new ReachableFishRater()});

        Fighter mctsFighter = new Fighter(new PureMCTSMoveGetter(), enhancedRater);
        Fighter pvsFighter = new Fighter(new PVSMoveGetter(), hopefullyBetterRater);

        BattleResult result = Battle.run(mctsFighter, pvsFighter, new BattleData(1));
        System.out.println(result);
    }
}
