package sc.player2023.logic.mcts;

import sc.api.plugins.ITeam;
import sc.player2023.logic.ImmutableGameState;
import sc.player2023.logic.PlayoutResult;
import sc.player2023.logic.PureMCTSMoveGetter;
import sc.plugin2023.Move;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;

public class ImmutableMCTSTreeNode {
    public record Statistics(int visits, int wins) {
        public static Statistics zeroed() {
            return new Statistics(0, 0);
        }

        public Statistics addWin() {
            return new Statistics(this.visits + 1, this.wins + 1);
        }

        public Statistics addLossOrDraw() {
            return new Statistics(this.visits + 1, this.wins);
        }
    }

    @Nonnull
    private final Statistics statistics;

    /* The move that lead to the current game state */
    @Nonnull
    private final Move move;

    @Nonnull
    private final ImmutableGameState gameState;

    @Nonnull
    private final List<ImmutableMCTSTreeNode> children;

    public ImmutableMCTSTreeNode(@Nonnull Statistics statistics, @Nonnull Move move, @Nonnull ImmutableGameState gameState, @Nonnull List<ImmutableMCTSTreeNode> children) {
        this.statistics = statistics;
        this.move = move;
        this.gameState = gameState;
        this.children = children;
    }

    public ImmutableMCTSTreeNode(@Nonnull Move move, @Nonnull ImmutableGameState gameState) {
        this.statistics = Statistics.zeroed();
        this.move = move;
        this.gameState = gameState;
        this.children = List.of();
    }

    @Nonnull
    public Statistics getStatistics() {
        return this.statistics;
    }

    @Nonnull
    public ImmutableGameState getGameState() {
        return gameState;
    }

    @Nonnull
    public Move getMove() {
        return this.move;
    }

    /**
     * Implementation of the UCT formula, as stated on the MCTS Wikipedia page:
     * https://en.wikipedia.org/wiki/Monte_Carlo_tree_search#Exploration_and_exploitation
     */
    public double uct(int parentNodeVisits, double explorationWeight) {
        double nodeWins = this.statistics.wins;
        double nodeVisits = this.statistics.visits;
        double exploitation = nodeWins / nodeVisits;
        double exploration = Math.log(parentNodeVisits) / nodeVisits;
        return exploitation + explorationWeight * exploration;
    }

    /*
     * Wrapper for the UCT formula with a default exploration weight of sqrt(2).
     */
    private double uct(int parentNodeVisits) {
        return this.uct(parentNodeVisits, PureMCTSMoveGetter.EXPLORATION_WEIGHT);
    }

    @Nonnull
    public ImmutableMCTSTreeNode withStatistics(@Nonnull Statistics newStatistics) {
        return new ImmutableMCTSTreeNode(newStatistics, this.move, this.gameState, this.children);
    }

    @Nonnull
    public ImmutableMCTSTreeNode withPlayoutResult(@Nonnull PlayoutResult result) {
        ITeam currentTeam = this.gameState.getCurrentTeam();
        ITeam resultTeam = result.getAffectedTeam();

        Statistics newStatistics;
        if (resultTeam == currentTeam) {
            newStatistics = this.statistics.addWin();
        }
        else {
            newStatistics = this.statistics.addLossOrDraw();
        }

        return new ImmutableMCTSTreeNode(newStatistics, this.move, this.gameState, this.children);
    }

    @Nonnull
    public ImmutableMCTSTreeNode withChild(@Nonnull ImmutableMCTSTreeNode childNode) {
        Stream<ImmutableMCTSTreeNode> childrenStream = this.children.stream();
        Stream<ImmutableMCTSTreeNode> childrenStreamWithNewNode = Stream.concat(childrenStream, Stream.of(childNode));
        List<ImmutableMCTSTreeNode> newChildren = childrenStreamWithNewNode.toList();

        return new ImmutableMCTSTreeNode(this.statistics, this.move, this.gameState, newChildren);
    }

    @Nonnull
    public ImmutableMCTSTreeNode withChildren(@Nonnull List<ImmutableMCTSTreeNode> childNodes) {
        Stream<ImmutableMCTSTreeNode> childrenStream = this.children.stream();
        Stream<ImmutableMCTSTreeNode> childrenStreamWithNewNodes = Stream.concat(childrenStream, childNodes.stream());
        List<ImmutableMCTSTreeNode> newChildren = childrenStreamWithNewNodes.toList();

        return new ImmutableMCTSTreeNode(this.statistics, this.move, this.gameState, newChildren);
    }
}
