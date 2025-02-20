package robotparts.autoSubsystems;

import android.animation.IntArrayEvaluator;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.MultipleServosToPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import robotparts.hardware.Intake;

public class IntakeLinkSub extends Subsystem {
    // BOILERPLATE
    public static final IntakeLinkSub INSTANCE = new IntakeLinkSub();
    private IntakeLinkSub() {}

    // USER CODE
    public Servo linkager, linkagel;
    public List<Servo> servos = new ArrayList<>();

    private final double START = 0.31;
    private final double INIT = 0.22;
    private final double END = 0.06;
    private final double SPECIMEN = 0.21;
    private final double TRANSFERSPECIMEN = 0.38;
    private final double SEEK = 0.22;
    private final double SWITCHAROO = 0.33;
    private final double TIGHT = 0.25;

    public Command moveInit() {
        return new MultipleServosToPosition(servos, INIT, this);
    }

    public Command armInit() {
        return new MultipleServosToPosition(servos, INIT, this);
    }

    public Command specimenReady() {
        return new MultipleServosToPosition(servos, START, this);
    }

    public Command yoinkSpecimen() {
        return new MultipleServosToPosition(servos, SPECIMEN, this);
    }

    public Command transferSpecimen() {
        return new MultipleServosToPosition(servos, TRANSFERSPECIMEN, this);
    }

    public Command intakeSeek() {
        return new MultipleServosToPosition(servos, SEEK, this);
    }

    public Command switcharoo() {
        return new MultipleServosToPosition(servos, SWITCHAROO, this);
    }

    public Command clawDown() {
        return new MultipleServosToPosition(servos, START, this);
    }

    @Override
    public void initialize() {
        linkager = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, "linkager");
        linkagel = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, "linkagel");
        servos.add(linkager);
        servos.add(linkagel);
        servos.get(0).setDirection(Servo.Direction.REVERSE);
        servos.get(1).setDirection(Servo.Direction.FORWARD);
    }
}
