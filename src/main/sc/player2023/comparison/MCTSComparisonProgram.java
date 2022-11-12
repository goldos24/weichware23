package sc.player2023.comparison;

import sc.player2023.logic.Logic;
import sc.player2023.logic.MCTSMoveGetter;
import sc.player2023.logic.mcts.NodeEvaluator;
import sc.player2023.logic.mcts.NodeSelector;
import sc.player2023.logic.mcts.RootChildrenInitialiser;
import sc.player2023.logic.mcts.evaluators.*;
import sc.player2023.logic.mcts.initialisers.*;
import sc.player2023.logic.mcts.selectors.*;
import sc.player2023.logic.rating.*;

public class MCTSComparisonProgram {
    public static void main(String[] args) {
        Rater rater = Logic.createCombinedRater();
        NodeEvaluator scoreDiffWeightedEvaluator = new ScoreDiffWeightedUCTEvaluator(MCTSMoveGetter.THEORETICAL_EXPLORATION_WEIGHT);
        NodeSelector selector = new BasicEvaluatorSelector(scoreDiffWeightedEvaluator);
        RootChildrenInitialiser initialiser = new AllMovesInitialiser();
        RootChildrenInitialiser otherInitialiser = new BestRatedMovesInitialiser(rater, 8);

        Fighter mctsFighter = new Fighter(new MCTSMoveGetter(selector, initialiser, 3), rater);
        Fighter otherMctsFighter = new Fighter(new MCTSMoveGetter(selector, otherInitialiser, 8), rater);

        BattleResult result = Battle.run(mctsFighter, otherMctsFighter, new BattleData(3));
        System.out.println(result);
    }
}
