package robotparts.autoSubsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;

public class outtakeSub extends Subsystem {
    // BOILERPLATE
    public static final outtakeSub INSTANCE = new outtakeSub();
    private outtakeSub(){}

    public Servo claw, pivot, armr, arml;

    public String name = "claw_servo";


}

// USER CODE
