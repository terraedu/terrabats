package robotparts.autoSubsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.MultipleServosToPosition;

import java.util.ArrayList;
import java.util.List;

public class IntakeArmSub extends Subsystem {
    // BOILERPLATE
    public static final IntakeArmSub INSTANCE = new IntakeArmSub();
    private IntakeArmSub() {}

    // USER CODE
    public Servo iarmr, iarml;
    public List<Servo> servos = new ArrayList<>();

    private final double INIT = 1;
    private final double SPECIMENREADY = 0.81;
    private final double SPECIMEN = 0.95;
    private final double SWITCHAROO = 0.93;
    private final double TRANSFERSPECIMEN = 0.87;
    private final double STAGETRANSFER = 0.93;
    private final double SEEK = 0.54;
    private final double GRAB = 0.41;
    private final double SLIDE = 0.7;
    private final double LOW = 0.22;

    public Command moveInit() {
        return new MultipleServosToPosition(servos, INIT, this);
    }

    public Command armInit() {
        return new MultipleServosToPosition(servos, INIT, this);
    }

    public Command specimenReady() {
        return new MultipleServosToPosition(servos, SPECIMENREADY, this);
    }

    public Command yoinkSpecimen() {
        return new MultipleServosToPosition(servos, SPECIMEN, this);
    }

    public Command transferSpecimen() {
        return new MultipleServosToPosition(servos, TRANSFERSPECIMEN, this);
    }

    public Command stageTransfer() {
        return new MultipleServosToPosition(servos, STAGETRANSFER, this);
    }

    public Command electricSlide() {
        return new MultipleServosToPosition(servos, SLIDE, this);
    }


    public Command intakeSeek() {
        return new MultipleServosToPosition(servos, SEEK, this);
    }

    public Command intake() {
        return new MultipleServosToPosition(servos, GRAB, this);
    }

    public Command switcharoo() {
        return new MultipleServosToPosition(servos, SWITCHAROO, this);
    }

    public Command smallInit() {
        return new MultipleServosToPosition(servos, INIT, this);
    }

    public Command clawDown() {
        return new MultipleServosToPosition(servos, LOW, this);
    }

    public Command clawUp() {
        return new MultipleServosToPosition(servos, SEEK, this);
    }

    @Override
    public void initialize() {
        iarmr = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, "iarmr");
        iarml = OpModeData.INSTANCE.getHardwareMap().get(Servo.class, "iarml");
        servos.add(iarmr);
        servos.add(iarml);
        servos.get(0).setDirection(Servo.Direction.FORWARD);
        servos.get(1).setDirection(Servo.Direction.REVERSE);
    }
}
