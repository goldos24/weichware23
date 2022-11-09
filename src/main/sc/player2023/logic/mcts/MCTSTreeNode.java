package sc.player2023.logic.mcts;

import sc.api.plugins.ITeam;
import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.PureMCTSMoveGetter;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MCTSTreeNode {

    @Nonnull
    private Statistics statistics;

    /* The move that lead to the current game state */
    @Nullable
    private final Move move;

    @Nonnull
    private final ImmutableGameState gameState;

    @Nonnull
    private final List<MCTSTreeNode> children;

    public MCTSTreeNode(@Nonnull ImmutableGameState gameState) {
        this.statistics = Statistics.zeroed();
        this.move = null;
        this.gameState = gameState;
        this.children = new ArrayList<>();
    }

    public MCTSTreeNode(@Nonnull Move move, @Nonnull ImmutableGameState gameState) {
        this.statistics = Statistics.zeroed();
        this.move = move;
        this.gameState = gameState;
        this.children = new ArrayList<>();
    }

    public MCTSTreeNode(@Nonnull Statistics statistics, @Nullable Move move, @Nonnull ImmutableGameState gameState, @Nonnull List<MCTSTreeNode> children) {
        this.statistics = statistics;
        this.move = move;
        this.gameState = gameState;
        this.children = new ArrayList<>(children);
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
    public List<MCTSTreeNode> getChildren() {
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
    public MCTSTreeNode trace(List<Integer> stepsToNode) {
        MCTSTreeNode currentNode = this;
        for (int index : stepsToNode) {
            List<MCTSTreeNode> children = currentNode.getChildren();

            if (index > children.size()) {
                return null;
            }

            currentNode = children.get(index);
        }
        return currentNode;
    }

    public void onTracedNodes(List<Integer> steps, Consumer<MCTSTreeNode> action) {
        MCTSTreeNode currentNode = this;

        for (int index : steps) {
            action.accept(currentNode);
            List<MCTSTreeNode> children = currentNode.getChildren();

            if (index > children.size()) {
                return;
            }

            currentNode = children.get(index);
        }
    }

    /**
     * Implementation of the UCT formula, as stated on the MCTS Wikipedia page:
     * https://en.wikipedia.org/wiki/Monte_Carlo_tree_search#Exploration_and_exploitation
     */
    public double uct(int parentNodeVisits, double explorationWeight) {
        double nodeWins = this.statistics.wins();
        double nodeVisits = this.statistics.visits();

        double exploitation = nodeWins / nodeVisits;
        double exploration  = Math.log(parentNodeVisits) / nodeVisits;
        return exploitation + explorationWeight * exploration;
    }

    /*
     * Wrapper for the UCT formula with a default exploration weight of sqrt(2).
     */
    public double uct(int parentNodeVisits) {
        return this.uct(parentNodeVisits, PureMCTSMoveGetter.EXPLORATION_WEIGHT);
    }

    public void setStatistics(@Nonnull Statistics newStatistics) {
        this.statistics = newStatistics;
    }

    public void updateStatistics() {
        Statistics totalStatistics = Statistics.zeroed();
        for (MCTSTreeNode child : this.children) {
            totalStatistics = totalStatistics.add(child.getStatistics());
        }

        this.statistics = totalStatistics.inverted();
    }

    public void addPlayoutResult(@Nonnull PlayoutResult result) {
        ITeam currentTeam = this.gameState.getCurrentTeam();
        this.statistics = this.statistics.addPlayoutResult(result, currentTeam);
    }

    @Nonnull
    public MCTSTreeNode addBackpropagatedChildrenAfterSteps(@Nonnull List<Integer> steps, @Nonnull List<MCTSTreeNode> childNodes) {
        // Example:     -> return this
        // Example: 0   -> return this.getChildren(0)
        // Example: 0 1 -> return this.getChildren(0).getChildren(1)
        MCTSTreeNode targetNode = this.trace(steps);
        assert targetNode != null;

        targetNode.addChildren(childNodes);
        this.onTracedNodes(steps, MCTSTreeNode::updateStatistics);

        return this;
    }

    public void addChildren(@Nonnull MCTSTreeNode... childNodes) {
        Stream<MCTSTreeNode> childNodesStream = Arrays.stream(childNodes);
        List<MCTSTreeNode> childNodesList = childNodesStream.toList();
        this.children.addAll(childNodesList);
    }

    public void addChildren(@Nonnull List<MCTSTreeNode> childNodes) {
        this.children.addAll(childNodes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MCTSTreeNode that = (MCTSTreeNode) o;

        if (!statistics.equals(that.statistics)) return false;
        if (!Objects.equals(move, that.move)) return false;
        if (!gameState.equals(that.gameState)) return false;
        return children.equals(that.children);
    }

    @Override
    public String toString() {
        return "MCTSTreeNode{" +
            "statistics=" + statistics +
            ", move=" + move +
            ", gameState=" + gameState +
            ", children=" + children +
            '}';
    }
}
