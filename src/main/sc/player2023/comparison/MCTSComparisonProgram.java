package sc.player2023.comparison;

import sc.player2023.logic.Logic;
import sc.player2023.logic.PureMCTSMoveGetter;
import sc.player2023.logic.mcts.NodeEvaluator;
import sc.player2023.logic.mcts.evaluators.*;
import sc.player2023.logic.rating.*;

public class MCTSComparisonProgram {
    public static void main(String[] args) {
        Rater rater = Logic.createCombinedRater();
        NodeEvaluator nodeEvaluator = new ScoreDiffWeightedUCTEvaluator(PureMCTSMoveGetter.THEORETICAL_EXPLORATION_WEIGHT);
        Fighter mctsFighter = new Fighter(new PureMCTSMoveGetter(nodeEvaluator), Logic.createCombinedRater());
        NodeEvaluator otherNodeEvaluator = new PureUCTEvaluator(PureMCTSMoveGetter.THEORETICAL_EXPLORATION_WEIGHT);
        Fighter otherMctsFighter = new Fighter(new PureMCTSMoveGetter(otherNodeEvaluator), rater);

        BattleResult result = Battle.run(mctsFighter, otherMctsFighter, new BattleData(4));
        System.out.println(result);
    }
}
