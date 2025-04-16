package util.control;

import subsystem.Drive;

public class SquIDrive {


    private final SquIDController xPID = new SquIDController(Math.sqrt(.01));
    private final SquIDController yPID = new SquIDController(Math.sqrt(.01));
    private final SquIDController hPID = new SquIDController(Math.sqrt(.01));


    public void follow(){


    }
}
