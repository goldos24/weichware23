package sc.player2023.logic.mcts;

import sc.api.plugins.ITeam;
import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.PureMCTSMoveGetter;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ImmutableMCTSTreeNode {

    @Nonnull
    private final Statistics statistics;

    /* The move that lead to the current game state */
    @Nullable
    private final Move move;

    @Nonnull
    private final ImmutableGameState gameState;

    @Nonnull
    private final List<ImmutableMCTSTreeNode> children;

    public ImmutableMCTSTreeNode(@Nonnull ImmutableGameState gameState) {
        this.statistics = Statistics.zeroed();
        this.move = null;
        this.gameState = gameState;
        this.children = List.of();
    }

    public ImmutableMCTSTreeNode(@Nonnull Statistics statistics, @Nullable Move move, @Nonnull ImmutableGameState gameState, @Nonnull List<ImmutableMCTSTreeNode> children) {
        this.statistics = statistics;
        this.move = move;
        this.gameState = gameState;
        this.children = children;
    }

    @Nonnull
    public Statistics getStatistics() {
        return this.statistics;
    }

    @Nonnull
    public ImmutableGameState getGameState() {
        return this.gameState;
    }

    @Nullable
    public Move getMove() {
        return this.move;
    }


    @Nonnull
    public List<ImmutableMCTSTreeNode> getChildren() {
        return this.children;
    }

    public boolean hasChildren() {
        return !this.children.isEmpty();
    }

    /**
     * Traces a node in the MCTS tree structure using the list of indices provided.
     * <br>
     * For each entry in the list, this function advances layer by layer until all
     * indices are exhausted or until the last node has no children.
     *
     * @param stepsToNode The list of indices to trace the node in the tree structure
     * @return The node, at depth = length of stepsToNode
     */
    @Nullable
    public ImmutableMCTSTreeNode trace(List<Integer> stepsToNode) {
        ImmutableMCTSTreeNode currentNode = this;
        for (Integer index : stepsToNode) {
            List<ImmutableMCTSTreeNode> children = currentNode.getChildren();

            if (index > children.size()) {
                return null;
            }

            currentNode = children.get(index);
        }
        return currentNode;
    }

    /**
     * Implementation of the UCT formula, as stated on the MCTS Wikipedia page:
     * https://en.wikipedia.org/wiki/Monte_Carlo_tree_search#Exploration_and_exploitation
     *
     * Includes some personal changes for NaN avoidance, but I'm not sure if that does
     * change anything.
     */
    public double uct(int parentNodeVisits, double explorationWeight, boolean avoidNaN) {
        double nodeWins = this.statistics.wins();
        double nodeVisits = this.statistics.visits();

        double exploitation;
        if (avoidNaN) {
            // Minimal derivation from the exploitation and exploration part of the
            // original formula to avoid NaN values.
            //
            // This change has some effect on the result:

            // - For low values  -> the exploitation value is lower
            // - For high values -> the exploitation value is only slightly lower
            exploitation = nodeWins / (nodeVisits + 1);
        } else {
            exploitation = nodeWins / nodeVisits;
        }

        double exploration;
        if (avoidNaN) {
            // - For low values                           -> the exploration value is lower
            // - For high values                          -> the exploration value is only slightly lower

            // - high parent node visits, low node visits -> the exploration value is significantly lower
            // I suppose that this is actually better because it leads to the selection of
            // nodes with more promising statistics (high node visits).
            // But I might be wrong because it slightly messes with the exploration of new paths.
            exploration = Math.log(parentNodeVisits + 1) / (nodeVisits + 1);
        } else {
            exploration = Math.log(parentNodeVisits) / nodeVisits;
        }

        return exploitation + explorationWeight * exploration;
    }

    /*
     * Wrapper for the UCT formula with a default exploration weight of sqrt(2) and the NaN avoidance changes.
     */
    public double uct(int parentNodeVisits) {
        return this.uct(parentNodeVisits, PureMCTSMoveGetter.EXPLORATION_WEIGHT, true);
    }

    @Nonnull
    public ImmutableMCTSTreeNode withStatistics(@Nonnull Statistics newStatistics) {
        return new ImmutableMCTSTreeNode(newStatistics, this.move, this.gameState, this.children);
    }

    @Nonnull
    public ImmutableMCTSTreeNode withPlayoutResult(@Nonnull PlayoutResult result) {
        ITeam currentTeam = this.gameState.getCurrentTeam();
        Statistics newStatistics = this.statistics.addPlayoutResult(result, currentTeam);
        return this.withStatistics(newStatistics);
    }

    @Nonnull
    public ImmutableMCTSTreeNode withBackpropagatedChildAfterSteps(@Nonnull List<Integer> steps, @Nonnull ImmutableMCTSTreeNode childNode) {
        // No steps -> The current node is replaced by the child node
        if (steps.isEmpty()) {
            return childNode;
        }

        int nodeIndex = steps.get(0);
        List<Integer> restNodeIndices = steps.subList(1, steps.size());

        ImmutableMCTSTreeNode targetedChild = this.children.get(nodeIndex);
        ImmutableMCTSTreeNode newChild = targetedChild.withBackpropagatedChildAfterSteps(restNodeIndices, childNode);

        int prevSize = this.children.size();

        // Replace node at nodeIndex with the new child
        List<ImmutableMCTSTreeNode> children = new ArrayList<>();
        Statistics totalStatistics = Statistics.zeroed();
        for (int i = 0; i < this.children.size(); ++i) {
            ImmutableMCTSTreeNode child;
            if (i == nodeIndex) {
                child = newChild;
            } else {
                child = this.children.get(i);
            }

            Statistics childStatistics = child.getStatistics();
            totalStatistics = totalStatistics.add(childStatistics);
            children.add(child);
        }

        assert children.size() == prevSize;

        // Sum of child Statistics needs to be inverted for the parent node
        Statistics invertedTotalStatistics = totalStatistics.inverted();
        return new ImmutableMCTSTreeNode(invertedTotalStatistics, this.move, this.gameState, children);
    }

    @Nonnull
    public ImmutableMCTSTreeNode withAdditionalChildren(@Nonnull List<ImmutableMCTSTreeNode> childNodes) {
        Stream<ImmutableMCTSTreeNode> childrenStream = this.children.stream();
        Stream<ImmutableMCTSTreeNode> childrenStreamWithNewNodes = Stream.concat(childrenStream, childNodes.stream());
        List<ImmutableMCTSTreeNode> newChildren = childrenStreamWithNewNodes.toList();

        return new ImmutableMCTSTreeNode(this.statistics, this.move, this.gameState, newChildren);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImmutableMCTSTreeNode that = (ImmutableMCTSTreeNode) o;

        if (!statistics.equals(that.statistics)) return false;
        if (!Objects.equals(move, that.move)) return false;
        if (!gameState.equals(that.gameState)) return false;
        return children.equals(that.children);
    }

    @Override
    public String toString() {
        return "ImmutableMCTSTreeNode{" +
            "statistics=" + statistics +
            ", move=" + move +
            ", gameState=" + gameState +
            ", children=" + children +
            '}';
    }
}
