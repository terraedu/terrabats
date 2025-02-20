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

public class IntakeTurretSub extends Subsystem {
    // BOILERPLATE
    public static final IntakeTurretSub INSTANCE = new IntakeTurretSub();
    private IntakeTurretSub() {}

    // USER CODE
    public Servo iturret;

    private final double START = 0.97;
    private final double HORIZONTAL = 0.6;
    private final double LEFT = 0.4;
    private final double RIGHT = 0;
    private final double SWITCHAROO = 0.21;

    public Command moveInit() {
        return new ServoToPosition(iturret, START, this);
    }

    public Command specimenReady() {
        return new ServoToPosition(iturret, START, this);
    }

    public Command intakeSeek() {
        return new ServoToPosition(iturret, START, this);
    }

    public Command turretReset() {
        return new ServoToPosition(iturret, START, this);
    }

    public Command turretHorizontal() {
        return new ServoToPosition(iturret, HORIZONTAL, this);
    }

    public Command turretLeft() {
        return new ServoToPosition(iturret, LEFT, this);
    }

    public Command turretRight() {
        return new ServoToPosition(iturret, RIGHT, this);
    }

    public Command turretSwitcharoo() {
        return new ServoToPosition(iturret, SWITCHAROO, this);
    }

    @Override
    public void initialize() {
        iturret = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, "iturret");
        iturret.setDirection(Servo.Direction.FORWARD);
    }
}
