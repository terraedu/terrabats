package robotparts.autoSubsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition;

public class OuttakeClawSub extends Subsystem {
    // BOILERPLATE
    public static final OuttakeClawSub INSTANCE = new OuttakeClawSub();
    private OuttakeClawSub() {}

    // USER CODE
    public Servo claw;

    private final double START = 1;
    private final double OPEN = 0.6;

    public Command moveInit() {
        return new ServoToPosition(claw, START, this);
    }

    public Command specimenReady() {
        return new ServoToPosition(claw, OPEN, this);
    }

    public Command switcharooReady() {
        return new ServoToPosition(claw, OPEN, this);
    }

    public Command clawGrab() {
        return new ServoToPosition(claw, START, this);
    }

    public Command clawRelease() {
        return new ServoToPosition(claw, OPEN, this);
    }

    @Override
    public void initialize() {
        claw = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, "claw");
        claw.setDirection(Servo.Direction.REVERSE);
    }
}