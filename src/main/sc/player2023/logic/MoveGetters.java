package sc.player2023.logic;

import sc.player2023.logic.pvs.AspiredPVSMoveGetter;
import sc.player2023.logic.pvs.FailSoftPVSMoveGetter;
import sc.player2023.logic.pvs.NegaMaxMoveGetter;

import javax.annotation.Nonnull;

public class MoveGetters {
    @Nonnull
    public static final MoveGetter HYBRID_ASPIRATION_FAIL_SOFT_MOVE_GETTER = new EmptyFieldCountBasedHybridMoveGetter(
            new FailSoftPVSMoveGetter(),
            new AspiredPVSMoveGetter(),
            new FailSoftPVSMoveGetter(),
            16,
            32
    );


    @Nonnull
    public static final FailSoftPVSMoveGetter FAIL_SOFT_PVS_MOVE_GETTER = new FailSoftPVSMoveGetter();

    @Nonnull
    public static final NegaMaxMoveGetter NEGA_MAX_MOVE_GETTER = new NegaMaxMoveGetter();
}
