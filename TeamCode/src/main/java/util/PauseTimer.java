package util;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

public class PauseTimer extends OpMode {
    private ElapsedTime timer = new ElapsedTime();

    double waitTime = 0;
    boolean proceed;

    public boolean addPause(double seconds) {
        waitTime = seconds;
        timer.reset();
        return proceed;
    }

    @Override
    public void init() {

    }

    @Override
    public void loop() {
        if (timer.seconds() > waitTime) {
            proceed = true;
        }
    }
}
