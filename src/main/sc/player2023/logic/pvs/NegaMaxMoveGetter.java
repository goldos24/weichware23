package sc.player2023.logic.pvs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.transpositiontable.SmartTransPositionTableFactory;
import sc.player2023.logic.transpositiontable.TransPositionTableFactory;

public class NegaMaxMoveGetter implements IterativeDeepeningAlphaBetaMoveGetter {
    private static final Logger log = LoggerFactory.getLogger(NegaMaxMoveGetter.class);

    @Override
    public Logger log() {
        return log;
    }

    TransPositionTableFactory transPositionTableFactory = new SmartTransPositionTableFactory();

    @Override
    public TransPositionTableFactory transPositionTableFactory() {
        return transPositionTableFactory;
    }

    @Override
    public AlphaBetaSearch algorithm() {
        return NegaMax::negaMax;
    }
}
