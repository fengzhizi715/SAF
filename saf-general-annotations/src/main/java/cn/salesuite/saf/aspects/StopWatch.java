package cn.salesuite.saf.aspects;

import java.util.concurrent.TimeUnit;

/**
 * Created by Tony Shen on 16/3/22.
 */
public class StopWatch {

    private long startTime;
    private long endTime;
    private long elapsedTime;

    public StopWatch() {
    }

    private void reset() {
        startTime = 0;
        endTime = 0;
        elapsedTime = 0;
    }

    public void start() {
        reset();
        startTime = System.nanoTime();
    }

    public void stop() {
        if (startTime != 0) {
            endTime = System.nanoTime();
            elapsedTime = endTime - startTime;
        } else {
            reset();
        }
    }

    public long getTotalTimeMillis() {
        return (elapsedTime != 0) ? TimeUnit.NANOSECONDS.toMillis(endTime - startTime) : 0;
    }
}
