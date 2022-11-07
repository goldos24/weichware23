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
     */
    public double uct(int parentNodeVisits, double explorationWeight) {
        double nodeWins = this.statistics.wins();
        double nodeVisits = this.statistics.visits();
        double exploitation = nodeWins / nodeVisits;
        double exploration = Math.log(parentNodeVisits) / nodeVisits;
        return exploitation + explorationWeight * exploration;
    }

    /*
     * Wrapper for the UCT formula with a default exploration weight of sqrt(2).
     */
    public double uct(int parentNodeVisits) {
        return this.uct(parentNodeVisits, PureMCTSMoveGetter.EXPLORATION_WEIGHT);
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
    public ImmutableMCTSTreeNode withChild(@Nonnull ImmutableMCTSTreeNode childNode) {
        Stream<ImmutableMCTSTreeNode> childrenStream = this.children.stream();
        Stream<ImmutableMCTSTreeNode> childrenStreamWithNewNode = Stream.concat(childrenStream, Stream.of(childNode));
        List<ImmutableMCTSTreeNode> newChildren = childrenStreamWithNewNode.toList();

        return new ImmutableMCTSTreeNode(this.statistics, this.move, this.gameState, newChildren);
    }

    @Nonnull
    public ImmutableMCTSTreeNode withBackpropagatedChildAfterSteps(@Nonnull List<Integer> steps, @Nonnull ImmutableMCTSTreeNode childNode) {
        // No steps -> Child is added to the current node
        if (steps.isEmpty()) {
            Statistics newStatistics = this.statistics.add(childNode.statistics);
            return this.withStatistics(newStatistics).withChild(childNode);
        }

        int nodeIndex = steps.get(0);
        List<Integer> restNodeIndices = steps.subList(1, steps.size());

        List<ImmutableMCTSTreeNode> currentChildren = this.children;
        ImmutableMCTSTreeNode targetedChild = currentChildren.get(nodeIndex);
        ImmutableMCTSTreeNode newChild = targetedChild.withBackpropagatedChildAfterSteps(restNodeIndices, childNode);

        // Replace node at nodeIndex with the new child
        List<ImmutableMCTSTreeNode> children = new ArrayList<>();
        Statistics totalStatistics = this.statistics;

        for (int i = 0; i < currentChildren.size(); ++i) {
            ImmutableMCTSTreeNode child;
            if (i == nodeIndex) {
                child = newChild;
            } else {
                child = currentChildren.get(i);
            }
            children.add(child);
            totalStatistics = totalStatistics.add(child.getStatistics());
        }

        // Statistics need to be inverted for the parent node
        Statistics invertedTotalStatistics = totalStatistics.inverted();
        return this.withStatistics(invertedTotalStatistics).withChildren(children);
    }

    @Nonnull
    public ImmutableMCTSTreeNode withChildren(@Nonnull List<ImmutableMCTSTreeNode> childNodes) {
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
