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

public class IntakeClawSub extends Subsystem {
    // BOILERPLATE
    public static final IntakeClawSub INSTANCE = new IntakeClawSub();
    private IntakeClawSub() {}

    // USER CODE
    public Servo iclaw;

    private final double INIT = 0;
    private final double SPECIMENREADY = 0.2;
    private final double PLACE = 0.83;
    private final double SWITCHAROO = 0.055;
    private final double BASKET = 0.5;

    public Command moveInit() {
        return new MultipleServosToPosition(servos, INIT, this);
    }

    public Command specimenReady() {
        return new MultipleServosToPosition(servos, SPECIMENREADY, this);
    }

    public Command upSpecimen() {
        return new MultipleServosToPosition(servos, PLACE, this);
    }

    public Command switcharooReady() {
        return new MultipleServosToPosition(servos, SWITCHAROO, this);
    }

    public Command placeHigh() {
        return new MultipleServosToPosition(servos, BASKET, this);
    }

    @Override
    public void initialize() {
        armr = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, "armr");
        arml = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, "arml");
        servos.add(armr);
        servos.add(arml);
        servos.get(0).setDirection(Servo.Direction.FORWARD);
        servos.get(1).setDirection(Servo.Direction.REVERSE);
    }
}
