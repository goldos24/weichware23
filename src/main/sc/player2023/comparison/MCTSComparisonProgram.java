package sc.player2023.comparison;

import sc.player2023.logic.Logic;
import sc.player2023.logic.MCTSMoveGetter;
import sc.player2023.logic.mcts.NodeEvaluator;
import sc.player2023.logic.mcts.NodeSelector;
import sc.player2023.logic.mcts.evaluators.*;
import sc.player2023.logic.mcts.selectors.BasicEvaluatorSelector;
import sc.player2023.logic.rating.*;

public class MCTSComparisonProgram {
    public static void main(String[] args) {
        Rater rater = Logic.createCombinedRater();
        NodeEvaluator pureEvaluator = new PureUCTEvaluator(MCTSMoveGetter.THEORETICAL_EXPLORATION_WEIGHT);
        NodeEvaluator scoreDiffWeightedEvaluator = new ScoreWeightedUCTEvaluator(MCTSMoveGetter.THEORETICAL_EXPLORATION_WEIGHT);

        NodeSelector basicSelector = new BasicEvaluatorSelector(pureEvaluator);
        Fighter mctsFighter = new Fighter(new MCTSMoveGetter(basicSelector), rater);

        NodeSelector otherBasicSelector = new BasicEvaluatorSelector(scoreDiffWeightedEvaluator);
        Fighter otherMctsFighter = new Fighter(new MCTSMoveGetter(otherBasicSelector), rater);

        BattleResult result = Battle.run(mctsFighter, otherMctsFighter, new BattleData(4));
        System.out.println(result);
    }
}
