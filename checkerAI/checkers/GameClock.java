package checkers;

// GameClock.java

// This kind of clock does a countdown for a specified
// number of seconds.
public class GameClock
{
    private long millisLeft;   // remaining milliseconds
    private long startTime;
    private boolean timerTicking;
    private long timelimits;
    private boolean started;
    // constructor.  Parameter is how long the countdown goes for.
    public GameClock(long seconds) {
        millisLeft = seconds * 1000;
        timerTicking = false;
        timelimits = seconds;
        started = false;
    }
    // Let the sands start falling out of the hourglass.
    public void start() {
            startTime = System.currentTimeMillis();
            timerTicking = true;
    }
    // Pause the clock. It can be restarted at the same point.
    public long pause() {
        long elapsed = 0;
        if (timerTicking) {
            elapsed = System.currentTimeMillis() - startTime;
            millisLeft -= elapsed;
            timerTicking = false;
        }
        return elapsed;
    }      
    public void reset() {
        millisLeft = timelimits * 1000;
        timerTicking = false;
    }
    public long millisRemaining() {
        if (timerTicking) {
            long elapsed = System.currentTimeMillis() - startTime;
            return millisLeft - elapsed;
        }
        else
           	return millisLeft;
    }
}