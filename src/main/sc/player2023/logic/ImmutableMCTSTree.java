package sc.player2023.logic;

import javax.annotation.Nonnull;
import java.util.List;

public class ImmutableMCTSTree {
	@Nonnull
	private final ImmutableMCTSTreeNode rootNode;

	public ImmutableMCTSTree(@Nonnull ImmutableGameState initialGameState) {
		ImmutableMCTSTreeNode.Statistics statistics = ImmutableMCTSTreeNode.Statistics.zeroed();
		List<ImmutableMCTSTreeNode> children = List.of();
		this.rootNode = new ImmutableMCTSTreeNode(statistics, initialGameState, children);
	}
}
