package sc.player2023.logic;

public class TimeMeasurerFixture {
    public static TimeMeasurer createAlreadyRunningZeroTimeMeasurer() {
        TimeMeasurer timeMeasurer = new TimeMeasurer(0);
        timeMeasurer.reset();
        return timeMeasurer;
    }
}
