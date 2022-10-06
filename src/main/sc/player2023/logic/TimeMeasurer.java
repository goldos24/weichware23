package sc.player2023.logic;

public class TimeMeasurer {
    private long lastTimestamp;
    private final long maxTime;

    public TimeMeasurer(long maxTime) {
        this.maxTime = maxTime;
    }

    public void reset() {
        lastTimestamp = System.currentTimeMillis();
    }

    public boolean ranOutOfTime() {
        return System.currentTimeMillis() - lastTimestamp > maxTime;
    }
}
