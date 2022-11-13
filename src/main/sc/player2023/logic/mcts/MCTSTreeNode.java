package sc.player2023.logic.mcts;

import sc.api.plugins.ITeam;
import sc.player2023.logic.gameState.ImmutableGameState;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

    @Nullable
    public List<MCTSTreeNode> traceAll(List<Integer> stepsToNode) {
        MCTSTreeNode currentNode = this;

        List<MCTSTreeNode> selectedNodes = new ArrayList<>();
        selectedNodes.add(currentNode);
        for (int index : stepsToNode) {
            List<MCTSTreeNode> children = currentNode.getChildren();

            if (index > children.size()) {
                return null;
            }

            currentNode = children.get(index);
            selectedNodes.add(currentNode);
        }

        return selectedNodes;
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

    public void addBackpropagatedChildrenAfterSteps(@Nonnull List<Integer> steps, @Nonnull List<MCTSTreeNode> childNodes) {
        // Example:     -> return this
        // Example: 0   -> return this.getChildren(0)
        // Example: 0 1 -> return this.getChildren(0).getChildren(1)
        List<MCTSTreeNode> targetNodes = this.traceAll(steps);
        assert targetNodes != null;
        assert !targetNodes.isEmpty();

        MCTSTreeNode lastNode = targetNodes.get(targetNodes.size() - 1);
        lastNode.addChildren(childNodes);

        Collections.reverse(targetNodes);
        for (MCTSTreeNode node : targetNodes) {
            node.updateStatistics();
        }
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
