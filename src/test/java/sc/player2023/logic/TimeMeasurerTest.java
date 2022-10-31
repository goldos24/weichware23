package sc.player2023.logic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeMeasurerTest {

    // can fail on 32-bit systems in the year 2106
    @Test
    void reset() {
        TimeMeasurer timeMeasurer = TimeMeasurerFixture.createAlreadyRunningZeroTimeMeasurer();
        long timestamp = timeMeasurer.getLastTimestamp();
        assertNotEquals(0, timestamp);
    }

    @Test
    void ranOutOfTime() {
        TimeMeasurer timeMeasurer = TimeMeasurerFixture.createAlreadyRunningZeroTimeMeasurer();
        try {
            Thread.sleep(2);
        } catch (InterruptedException ignored) {}
        assertTrue(timeMeasurer.ranOutOfTime());
    }
}