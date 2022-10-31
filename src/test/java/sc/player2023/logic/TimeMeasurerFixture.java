package sc.player2023.logic;

public class TimeMeasurerFixture {
    public static TimeMeasurer createAlreadyRunningZeroTimeMeasurer() {
        TimeMeasurer timeMeasurer = new TimeMeasurer(0);
        timeMeasurer.reset();
        return timeMeasurer;
    }

    public static TimeMeasurer createAlreadyRunningInfiniteTimeMeasurer() {
        TimeMeasurer timeMeasurer = new TimeMeasurer(Long.MAX_VALUE - System.currentTimeMillis() - 2); // if an overflow happens, 2 is subtracted, so it is a positive value again
        timeMeasurer.reset();
        return timeMeasurer;
    }
}
