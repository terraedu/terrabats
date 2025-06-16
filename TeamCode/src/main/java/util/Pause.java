package util;

import static dev.frozenmilk.sinister.util.log.LoggerImpl.INFO;

import android.util.Log;

import com.qualcomm.robotcore.util.ElapsedTime;

public class Pause {
    ElapsedTime time = new ElapsedTime();
    public boolean repeat;
    public boolean proceed;

    public boolean pause(double ms){
        proceed = false;
        proceed = time.milliseconds() >= ms;
        if(!repeat) {
        time.reset();
        repeat = true;
        return false;
        }else if(proceed && (time.milliseconds() >= ms)){
            time.reset();
            repeat = false;
            return true;
        }

        return false;
    }

    public boolean getProceed(){
        return proceed;
    }

    public double getTime(){
        return time.milliseconds();
    }
    public void reset(){
        proceed = false;
        repeat = false;
        time.reset();
    }


}
