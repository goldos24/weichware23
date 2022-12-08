package sc.player2023.comparison;

import sc.player2023.logic.MCTSMoveGetter;
import sc.player2023.logic.mcts.NodeEvaluator;
import sc.player2023.logic.mcts.NodeExpander;
import sc.player2023.logic.mcts.NodeSelector;
import sc.player2023.logic.mcts.RootChildrenInitialiser;
import sc.player2023.logic.mcts.evaluators.PureUCTEvaluator;
import sc.player2023.logic.mcts.expanders.FullNodeExpander;
import sc.player2023.logic.mcts.expanders.PlayoutNodeExpansionProvider;
import sc.player2023.logic.mcts.expanders.SortedCappedNodeExpander;
import sc.player2023.logic.mcts.initialisers.AllMovesInitialiser;
import sc.player2023.logic.mcts.selectors.BasicEvaluatorSelector;
import sc.player2023.logic.rating.*;

public class MCTSComparisonProgram {

    public static Rater createSimpleCombinedRater() {
        Rater potentialFishRater = new WeightedRater(1, new PotentialFishRater());
        Rater cappedFishDifferenceRater = new WeightedRater(2, new CappedFishDifferenceRater());

        Rater[] raters = {
            potentialFishRater, cappedFishDifferenceRater
        };
        return new CompositeRater(raters);
    }

    public static MCTSMoveGetter createFirstMCTSMoveGetter() {
        NodeEvaluator evaluator = new PureUCTEvaluator(MCTSMoveGetter.THEORETICAL_EXPLORATION_WEIGHT);
        NodeSelector selector = new BasicEvaluatorSelector(evaluator);
        RootChildrenInitialiser initialiser = new AllMovesInitialiser();
        NodeExpander expander = new FullNodeExpander(new PlayoutNodeExpansionProvider());

        return new MCTSMoveGetter(selector, initialiser, expander);
    }

    public static MCTSMoveGetter createSecondMCTSMoveGetter() {
        Rater rater = createSimpleCombinedRater();
        NodeEvaluator evaluator = new PureUCTEvaluator(MCTSMoveGetter.THEORETICAL_EXPLORATION_WEIGHT);
        NodeSelector selector = new BasicEvaluatorSelector(evaluator);
        RootChildrenInitialiser initialiser = new AllMovesInitialiser();
        NodeExpander expander = new SortedCappedNodeExpander(new PlayoutNodeExpansionProvider(), rater, 6);

        return new MCTSMoveGetter(selector, initialiser, expander);
    }

    public static void main(String[] args) {
        Fighter mctsFighter = new Fighter(createFirstMCTSMoveGetter(), createSimpleCombinedRater());
        Fighter pvsFighter = new Fighter(createSecondMCTSMoveGetter(), createSimpleCombinedRater());
        BattleResult result = Battle.run(mctsFighter, pvsFighter, new BattleData(5));
        System.out.println(result);
    }
}
