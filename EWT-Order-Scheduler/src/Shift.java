import java.time.LocalTime;

/**
 * Represents a shift.
 */
public class Shift {

    /**
     * The number of workers.
     */
    protected int numWorkers;

    /**
     * The start time.
     */
    protected LocalTime timeStart;

    /**
     * The end time.
     */
    protected LocalTime timeEnd;

    /**
     * Constructor.
     */
    public Shift() {
        this.numWorkers = 0;
        this.timeStart = LocalTime.MIDNIGHT;
        this.timeEnd = LocalTime.MIDNIGHT;
    }

    public void setNumWorkers(int numWorkers) {
        this.numWorkers = numWorkers;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }
}

