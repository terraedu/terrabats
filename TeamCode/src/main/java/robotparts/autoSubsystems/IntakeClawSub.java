package robotparts.autoSubsystems;

import android.animation.IntArrayEvaluator;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.MultipleServosToPosition;
import com.rowanmcalpin.nextftc.ftc.hardware.ServoToPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import robotparts.hardware.Intake;

public class IntakeClawSub extends Subsystem {
    // BOILERPLATE
    public static final IntakeClawSub INSTANCE = new IntakeClawSub();
    private IntakeClawSub() {}

    // USER CODE
    public Servo iclaw;

    private final double CLOSE = 0.45;
    private final double START = 0.5;
    private final double ADJUST = 0.41;
    private final double OPEN = 0.28;
    private final double OPENNN = 0;

    public Command moveInit() {
        return new ServoToPosition(iclaw, START, this);
    }

    public Command armInit() {
        return new ServoToPosition(iclaw, START, this);
    }

    public Command specimenReady() {
        return new ServoToPosition(iclaw, OPEN, this);
    }

    public Command yoinkSpecimen() {
        return new ServoToPosition(iclaw, START, this);
    }

    public Command stageTransfer() {
        return new ServoToPosition(iclaw, CLOSE, this);
    }

    public Command electricSlide() {
        return new ServoToPosition(iclaw, ADJUST, this);
    }

    public Command intakeSeek() {
        return new ServoToPosition(iclaw, START, this);
    }

    public Command clawUp() {
        return new ServoToPosition(iclaw, CLOSE, this);
    }

    public Command clawGrab() {
        return new ServoToPosition(iclaw, CLOSE, this);
    }

    public Command clawLightGrab() {
        return new ServoToPosition(iclaw, START, this);
    }

    public Command clawAdjustGrab() {
        return new ServoToPosition(iclaw, ADJUST, this);
    }

    public Command clawRelease() {
        return new ServoToPosition(iclaw, OPEN, this);
    }

    public Command clawRELEASE() {
        return new ServoToPosition(iclaw, OPENNN, this);
    }

    @Override
    public void initialize() {
        iclaw = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, "iclaw");
        iclaw.setDirection(Servo.Direction.FORWARD);
    }
}
