package sc.player2023.logic.transpositiontable;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Till Fransson
 * @since 23.12.2022
 */
public class SimpleTransPositionTableFactoryTest {

    @Test
    void createTransPositionTableFromDepth() {
        SimpleTransPositionTableFactory factory = new SimpleTransPositionTableFactory();
        TransPositionTable actual = factory.createTransPositionTableFromDepth(2);
        TransPositionTable expected = new TransPositionTable(new HashMap<>(),
                                                             SimpleTransPositionTableFactory.GAME_STATE_HOLDER_FACTORY,
                                                             SimpleTransPositionTableFactory.GAME_STATE_SELECTOR);
        assertEquals(expected, actual, "createSimpleTransPositionTable");
    }
}