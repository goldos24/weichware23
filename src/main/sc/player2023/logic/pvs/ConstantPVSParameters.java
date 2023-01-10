package sc.player2023.logic.pvs;

import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.move.PossibleMoveGenerator;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.transpositiontable.TransPositionTable;

import javax.annotation.Nonnull;

public record ConstantPVSParameters(@Nonnull Rater rater, @Nonnull TimeMeasurer timeMeasurer,
                                    @Nonnull TransPositionTable transPositionTable,
                                    @Nonnull PossibleMoveGenerator moveGenerator) {
}
