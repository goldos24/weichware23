package sc.player2023.logic.pvs;

import sc.player2023.logic.TimeMeasurer;
import sc.player2023.logic.move.PossibleMoveGenerator;
import sc.player2023.logic.rating.Rater;
import sc.player2023.logic.transpositiontable.TransPositionTable;

import javax.annotation.Nonnull;

public final class ConstantPVSParameters {
    ConstantPVSParameters(    @Nonnull
                              Rater rater,
                              @Nonnull
                              TimeMeasurer timeMeasurer,
                              @Nonnull
                              TransPositionTable transPositionTable,
                              @Nonnull
                              PossibleMoveGenerator moveGenerator) {
        this.rater = rater;
        this.timeMeasurer = timeMeasurer;
        this.transPositionTable = transPositionTable;
        this.moveGenerator = moveGenerator;
    }

    @Nonnull
    Rater rater;
    @Nonnull
    TimeMeasurer timeMeasurer;
    @Nonnull
    TransPositionTable transPositionTable;
    @Nonnull
    PossibleMoveGenerator moveGenerator;

    @Override
    public boolean equals(Object obj) {
        return obj == this || obj != null && obj.getClass() == this.getClass();
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "ConstantPVSParameters[]";
    }

    public TransPositionTable transPositionTable() {
        return transPositionTable;
    }

    public PossibleMoveGenerator moveGenerator() {
        return moveGenerator;
    }

    public Rater rater() {
        return rater;
    }

    public TimeMeasurer timeMeasurer() {
        return timeMeasurer;
    }
}
