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

public class IntakePivotSub extends Subsystem {
    // BOILERPLATE
    public static final IntakePivotSub INSTANCE = new IntakePivotSub();
    private IntakePivotSub() {}

    // USER CODE
    public Servo ipivot;

    private final double INIT = 0.8;
    private final double SPECIMENREADY = 0;
    private final double TRANSFERSPECIMEN = 0.34;
    private final double SEEK = 0.68;
    private final double GRAB = 0.79;
    private final double DROP = 0.4;
    private final double SMALLINIT = 0.55;
    private final double LOW = 0.15;

    public Command moveInit() {
        return new ServoToPosition(ipivot, INIT, this);
    }

    public Command armInit() {
        return new ServoToPosition(ipivot, INIT, this);
    }

    public Command iSpecimenReady() {
        return new ServoToPosition(ipivot, SPECIMENREADY, this);
    }

    public Command transferSpecimen() {
        return new ServoToPosition(ipivot, TRANSFERSPECIMEN, this);
    }

    public Command stageTransfer() {
        return new ServoToPosition(ipivot, SPECIMENREADY, this);
    }

    public Command intakeSeek() {
        return new ServoToPosition(ipivot, SEEK, this);
    }

    public Command intake() {
        return new ServoToPosition(ipivot, GRAB, this);
    }

    public Command zestyFlick() {
        return new ServoToPosition(ipivot, DROP, this);
    }

    public Command electricSlide() {
        return new ServoToPosition(ipivot, SPECIMENREADY, this);
    }

    public Command smallInit() {
        return new ServoToPosition(ipivot, SMALLINIT, this);
    }

    public Command clawDown() {
        return new ServoToPosition(ipivot, LOW, this);
    }

    public void initialize() {
        armr = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, "armr");
        arml = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, "arml");
        servos.add(armr);
        servos.add(arml);
        servos.get(0).setDirection(Servo.Direction.FORWARD);
        servos.get(1).setDirection(Servo.Direction.REVERSE);
    }
}
