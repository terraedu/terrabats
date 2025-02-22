package robotparts.autoSubsystems;

import com.qualcomm.robotcore.hardware.Servo;
import com.rowanmcalpin.nextftc.core.Subsystem;
import com.rowanmcalpin.nextftc.core.command.Command;
import com.rowanmcalpin.nextftc.ftc.OpModeData;
import com.rowanmcalpin.nextftc.ftc.hardware.MultipleServosToPosition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OuttakeArmSub extends Subsystem {
    // BOILERPLATE
    public static final OuttakeArmSub INSTANCE = new OuttakeArmSub();
    private OuttakeArmSub() {}

    // USER CODE
    public Servo armr, arml;
    public List<Servo> servos = new ArrayList<>();

    private final double INIT = 0;
    private final double SPECIMENREADY = 0.14;
    private final double specialgrab = 0.11;

    private final double PLACE = 0.55;
    private final double SWITCHAROO = 0.053;
    private final double BASKET = 0.5;

    public Command moveInit() {
        return new MultipleServosToPosition(servos, INIT, this);
    }

    public Command specimenReady() {
        return new MultipleServosToPosition(servos, SPECIMENREADY, this);
    }
    public Command specialahh() {
        return new MultipleServosToPosition(servos, specialgrab, this);
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