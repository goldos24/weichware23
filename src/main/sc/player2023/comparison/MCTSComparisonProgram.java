package sc.player2023.comparison;

import sc.player2023.logic.MCTSMoveGetter;
import sc.player2023.logic.mcts.NodeEvaluator;
import sc.player2023.logic.mcts.NodeExpander;
import sc.player2023.logic.mcts.NodeSelector;
import sc.player2023.logic.mcts.RootChildrenInitialiser;
import sc.player2023.logic.mcts.evaluators.ScoreDiffWeightedUCTEvaluator;
import sc.player2023.logic.mcts.expanders.FullNodeExpander;
import sc.player2023.logic.mcts.expanders.SimulatingNodeExpansionProvider;
import sc.player2023.logic.mcts.initialisers.AllMovesInitialiser;
import sc.player2023.logic.mcts.selectors.BasicEvaluatorSelector;
import sc.player2023.logic.pvs.PVSMoveGetter;
import sc.player2023.logic.rating.*;

public class MCTSComparisonProgram {

    public static Rater createCombinedRater() {
        CappedFishDifferenceRater cappedFishDifferenceRater = new CappedFishDifferenceRater();
        WeightedRater weightedStupidRater = new WeightedRater(5, cappedFishDifferenceRater);
        Rater weightedUselessPenguinRater = new WeightedRater(20, new PenguinCutOffRater());
        Rater[] raters = {weightedStupidRater, new WeightedRater(3, new PotentialFishRater()),
            weightedUselessPenguinRater, new WeightedRater(5, new ReachableFishRater())};
        return new CompositeRater(raters);
    }

    public static void main(String[] args) {
        NodeEvaluator scoreDiffWeightedEvaluator = new ScoreDiffWeightedUCTEvaluator(MCTSMoveGetter.THEORETICAL_EXPLORATION_WEIGHT);

        NodeSelector selector = new BasicEvaluatorSelector(scoreDiffWeightedEvaluator);
        RootChildrenInitialiser initialiser = new AllMovesInitialiser();
        NodeExpander expander = new FullNodeExpander(new SimulatingNodeExpansionProvider());

        Fighter mctsFighter = new Fighter(new MCTSMoveGetter(selector, initialiser, expander), createCombinedRater());
        Fighter pvsFighter = new Fighter(new PVSMoveGetter(), createCombinedRater());
        BattleResult result = Battle.run(mctsFighter, pvsFighter, new BattleData(1));
        System.out.println(result);
    }
}
