package sc.player2023.logic.pvs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.transpositiontable.SimpleTransPositionTableFactory;
import sc.player2023.logic.transpositiontable.TransPositionTableFactory;

/**
 * @author Till Fransson
 * @since 22.12.2022
 */
public class AspiredNegaMaxMoveGetter implements AspirationMoveGetter {

    private static final Logger log = LoggerFactory.getLogger(AspiredNegaMaxMoveGetter.class);
    private static final TransPositionTableFactory TRANS_POSITION_TABLE_FACTORY = new SimpleTransPositionTableFactory();

    @Override
    public Logger log() {
        return log;
    }

    @Override
    public TransPositionTableFactory transPositionTableFactory() {
        return TRANS_POSITION_TABLE_FACTORY;
    }

    @Override
    public AlphaBetaSearch algorithm() {
        return NegaMax::negaMax;
    }
}
